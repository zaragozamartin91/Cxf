package mz.ex.ws;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;


@WebServiceClient(
		name = "BookRepoService", 
		wsdlLocation = "classpath:BookRepoPortTypeImpl.wsdl", 
		targetNamespace = "http://ws.ex.mz/")
public class BookRepoService extends Service {

	public final static URL WSDL_LOCATION;

	public final static QName SERVICE = new QName("http://ws.ex.mz/", "BookRepoService");
	public final static QName BookRepoPort = new QName("http://ws.ex.mz/", "BookRepoPort");
	static {
		URL url = BookRepoPortType.class.getClassLoader().getResource("BookRepoPortTypeImpl.wsdl");
		if (url == null) {
			java.util.logging.Logger.getLogger(BookRepoPortType.class.getName()).log(java.util.logging.Level.INFO,
					"Can not initialize the default wsdl from {0}", "classpath:BookRepoPortTypeImpl.wsdl");
		}
		WSDL_LOCATION = url;
	}

	public BookRepoService(URL wsdlLocation) {
		super(wsdlLocation, SERVICE);
	}

	public BookRepoService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public BookRepoService() {
		super(WSDL_LOCATION, SERVICE);
	}

	public BookRepoService(WebServiceFeature... features) {
		super(WSDL_LOCATION, SERVICE, features);
	}

	public BookRepoService(URL wsdlLocation, WebServiceFeature... features) {
		super(wsdlLocation, SERVICE, features);
	}

	public BookRepoService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
		super(wsdlLocation, serviceName, features);
	}

	/**
	 *
	 * @return returns DoubleItPortType
	 */
	@WebEndpoint(name = "BookRepoPort")
	public BookRepoPortType getBookRepoPort() {
		return super.getPort(BookRepoPort, BookRepoPortType.class);
	}

	/**
	 * 
	 * @param features
	 *            A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in
	 *            the <code>features</code> parameter will have their default values.
	 * @return returns DoubleItPortType
	 */
	@WebEndpoint(name = "BookRepoPort")
	public BookRepoPortType getBookRepoPort(WebServiceFeature... features) {
		return super.getPort(BookRepoPort, BookRepoPortType.class, features);
	}

}
