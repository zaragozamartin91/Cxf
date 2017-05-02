package mz.ex.ws;

/**
 * This class was generated by Apache CXF 3.1.10 2017-04-25T10:28:30.414-03:00 Generated source version: 3.1.10
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

		BookRepoService bookRepoService = new BookRepoService();
		/*
		 * DADO QUE EL WSDL ESPECIFICA <wsam:Addressing /> o <wsam:UsingAddressing /> SE ESPERA QUE EL MENSAJE DE
		 * ENTRADA CONTENGA UN <Address/>. PARA QUE EL MENSAJE ENVIADO POR EL CLIENTE CONTENGA UN <Address/>, SE PUEDE
		 * OBTENER EL PORT USANDO new org.apache.cxf.ws.addressing.WSAddressingFeature() O SE PUEDE AGREGAR LA
		 * ANNOTATION @Addressing A LA INTERFAZ DEL SERVICIO.
		 */
		BookRepo port = bookRepoService.getBookRepoPort(new org.apache.cxf.ws.addressing.WSAddressingFeature());
		BookRequest bookRequest = new BookRequest();
		bookRequest.setIsbn(1234L);
		port.getBook(bookRequest);
	}

}