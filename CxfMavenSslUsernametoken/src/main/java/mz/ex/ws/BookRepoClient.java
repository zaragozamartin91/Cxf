package mz.ex.ws;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.soap.SOAPBinding;

import handler.WSSecurityHeaderSOAPHandler;

/**
 * ESTE CLIENTE NO FUNCIONA. SE UTILIZA MAYORITARIAMENTE PARA PROBAR LA POSIBLE CREACION DE UN CLIENTE MANUAL DE MENSAJES SOAP. TAMBIEN
 * SE UTILIZA PARA LEER LOS MENSAJES ENVIADOS Y RESPONDIDOS POR EL SERVICIO. 
 * 
 * @author martin.zaragoza
 *
 */
public class BookRepoClient {

	public static void main(String[] args) throws Exception {
		/*
		 * Este truststore tiene que tener importado el certificado del keystore generado para encriptar las conexiones
		 * ssl de tomcat.
		 * 
		 * 
		 * Se debe agregar la siguiente configuracion al server de tomcat en conf/server.xml:
		 * 
		 * <Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol" maxThreads="150"
		 * SSLEnabled="true" keystoreFile="conf/serverKeystore.jks" keystorePass="password"> <SSLHostConfig>
		 * <Certificate certificateKeystoreFile="conf/serverKeystore.jks" certificateKeystorePassword="password"
		 * type="RSA" /> </SSLHostConfig> </Connector>
		 */
		System.setProperty("javax.net.ssl.trustStore", "cert/clienttruststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "password");

		/* ESTAS LINEAS SON PARA VISUALIZAR LOS MENSAJES SOAP ENVIADOS */
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

		
		QName serviceName = new QName("http://ws.ex.mz/", "BookRepoService");
		QName portName = new QName("http://ws.ex.mz/", "BookRepoPort");

		Service service = Service.create(serviceName);
		service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, "https://localhost:8443/CxfMavenSslUsernametoken-0.0.1/services/BookRepo");
		BookRepoPortType client = service.getPort(portName, BookRepoPortType.class);

		BindingProvider bindingProvider = (BindingProvider) client;
		@SuppressWarnings("rawtypes")
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(new WSSecurityHeaderSOAPHandler("joe", "joespassword"));
        bindingProvider.getBinding().setHandlerChain(handlerChain);

		BookRequest bookRequest = new BookRequest();
		bookRequest.setIsbn(1234L);
		BookResponse bookResponse = client.getBook(bookRequest);
		System.out.printf("Got book %s:%s %n", bookResponse.getIsbn(), bookResponse.getName());
	}

}