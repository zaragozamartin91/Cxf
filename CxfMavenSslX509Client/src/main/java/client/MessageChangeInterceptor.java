package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.binding.soap.interceptor.SoapPreProtocolOutInterceptor;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Puede interceptar mensajes de entrada o de salida.
 * 
 * http://www.mastertheboss.com/jboss-web-services/apache-cxf-interceptors
 * http://stackoverflow.com/questions/6915428/how-to-modify-the-raw-xml-message-of-an-outbound-cxf-request
 * 
 */
public class MessageChangeInterceptor extends AbstractPhaseInterceptor<Message> {

	public MessageChangeInterceptor() {
		super(Phase.PRE_STREAM);
		addBefore(SoapPreProtocolOutInterceptor.class.getName());
	}

	private Logger getLogger() {
		return LoggerFactory.getLogger(this.getClass());
	}

	private String changeOutboundMessage(String currentEnvelope) {
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
}