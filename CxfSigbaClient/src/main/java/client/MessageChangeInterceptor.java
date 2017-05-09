package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.binding.soap.interceptor.SoapPreProtocolOutInterceptor;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Puede interceptar mensajes de entrada o de salida.
 * 
 * http://www.mastertheboss.com/jboss-web-services/apache-cxf-interceptors
 * http://stackoverflow.com/questions/6915428/how-to-modify-the-raw-xml-message-of-an-outbound-cxf-request
 * 
 */
public class MessageChangeInterceptor extends AbstractPhaseInterceptor<Message> {
	private static final NamespaceContext NAMESPACE_CONTEXT = new NamespaceContext() {
		@SuppressWarnings("serial")
		HashMap<String, String> namespaces = new HashMap<String, String>() {
			{
				put("wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
				put("wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
			}
		};

		public String getNamespaceURI(String prefix) {
			return namespaces.get(prefix);
		}

		public Iterator getPrefixes(String val) {
			List<String> prefixes = new ArrayList<>();
			Set<Entry<String, String>> entries = namespaces.entrySet();
			for (Entry<String, String> entry : entries) {
				if (entry.getValue().equals(val)) {
					prefixes.add(entry.getKey());
				}
			}
			return prefixes.iterator();
		}

		public String getPrefix(String uri) {
			Set<Entry<String, String>> entries = namespaces.entrySet();
			for (Entry<String, String> entry : entries) {
				if (entry.getValue().equals(uri)) {
					return entry.getKey();
				}
			}
			return null;
		}
	};

	public MessageChangeInterceptor() {
		super(Phase.PRE_STREAM);
		addBefore(SoapPreProtocolOutInterceptor.class.getName());
	}
	
	

	private Logger getLogger() {
		return LoggerFactory.getLogger(this.getClass());
	}

	private String changeOutboundMessage(String currentEnvelope) {
		try {
			Document document = stringToDocument(currentEnvelope);

			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			xpath.setNamespaceContext(NAMESPACE_CONTEXT);

			// XPathExpression binarySecurityTokenIdExpr =
			// xpath.compile("/Envelope/Header/Security/BinarySecurityToken/@Id");
			// String binarySecurityTokenId = binarySecurityTokenIdExpr.evaluate(document,
			// XPathConstants.STRING).toString();

			XPathExpression securityTokenReferenceExpr = xpath.compile("//wsse:SecurityTokenReference");
			Element securityTokenReferenceNode = (Element) securityTokenReferenceExpr.evaluate(document, XPathConstants.NODE);

			XPathExpression keyIdentifierExpr = xpath.compile("//wsse:KeyIdentifier");
			Element keyIdentifierExprNode = (Element) keyIdentifierExpr.evaluate(document, XPathConstants.NODE);

			securityTokenReferenceNode.removeChild(keyIdentifierExprNode);

			XPathExpression binarySecurityTokenIdExpr = xpath.compile("//wsse:BinarySecurityToken/@wsu:Id");
			String binarySecurityTokenId = binarySecurityTokenIdExpr.evaluate(document, XPathConstants.STRING).toString();

			Element referenceElement = document.createElementNS(NAMESPACE_CONTEXT.getNamespaceURI("wsse"), "wsse:Reference");
			referenceElement.setAttribute("URI", "#" + binarySecurityTokenId);
			referenceElement.setAttribute("ValueType", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509v3");

			securityTokenReferenceNode.appendChild(referenceElement);

			return documentToString(document);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentEnvelope;
	}

	private String changeInboundMessage(String currentEnvelope) {
		return currentEnvelope;
	}

	public void handleMessage(Message message) {
		boolean isOutbound = false;
		isOutbound = message == message.getExchange().getOutMessage() || message == message.getExchange().getOutFaultMessage();

		if (isOutbound) {
			OutputStream os = message.getContent(OutputStream.class);

			CachedStream cs = new CachedStream();
			message.setContent(OutputStream.class, cs);

			message.getInterceptorChain().doIntercept(message);

			try {
				cs.flush();
				IOUtils.closeQuietly(cs);
				CachedOutputStream csnew = (CachedOutputStream) message.getContent(OutputStream.class);

				String currentEnvelopeMessage = IOUtils.toString(csnew.getInputStream(), "UTF-8");
				csnew.flush();
				IOUtils.closeQuietly(csnew);

				String res = changeOutboundMessage(currentEnvelopeMessage);
				if (res == null || res.equals(currentEnvelopeMessage)) {
					res = currentEnvelopeMessage;
					System.out.printf("Outbound message has not changed: %s%n", res);
				} else {
					System.out.printf("Outbound message has been changed: %s%n", res);
				}

				InputStream replaceInStream = IOUtils.toInputStream(res, "UTF-8");

				IOUtils.copy(replaceInStream, os);
				replaceInStream.close();
				IOUtils.closeQuietly(replaceInStream);

				os.flush();
				message.setContent(OutputStream.class, os);
				IOUtils.closeQuietly(os);

			} catch (IOException ioe) {
				getLogger().warn("Unable to perform change.", ioe);
				throw new RuntimeException(ioe);
			}
		} else {
			try {
				InputStream is = message.getContent(InputStream.class);
				String currentEnvelopeMessage = IOUtils.toString(is, "UTF-8");
				IOUtils.closeQuietly(is);

				if (getLogger().isDebugEnabled()) {
					getLogger().debug("Inbound message: " + currentEnvelopeMessage);
				}

				String res = changeInboundMessage(currentEnvelopeMessage);
				if (res != null) {
					if (getLogger().isDebugEnabled()) {
						getLogger().debug("Inbound message has been changed: " + res);
					}
				}
				res = res != null ? res : currentEnvelopeMessage;

				is = IOUtils.toInputStream(res, "UTF-8");
				message.setContent(InputStream.class, is);
				IOUtils.closeQuietly(is);
			} catch (IOException ioe) {
				getLogger().warn("Unable to perform change.", ioe);

				throw new RuntimeException(ioe);
			}
		}
	}

	public void handleFault(Message message) {
	}

	private class CachedStream extends CachedOutputStream {
		public CachedStream() {
			super();
		}

		protected void doFlush() throws IOException {
			currentStream.flush();
		}

		protected void doClose() throws IOException {
		}

		protected void onWrite() throws IOException {
		}
	}

	private Document stringToDocument(String stringInput) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder db = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(stringInput));

		return db.parse(is);
	}

	private String documentToString(Document doc) {
		try {
			StringWriter sw = new StringWriter();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			transformer.transform(new DOMSource(doc), new StreamResult(sw));
			return sw.toString();
		} catch (Exception ex) {
			throw new RuntimeException("Error converting to String", ex);
		}
	}
}