package mz.ex.ws;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;

/**
 * This class was generated by Apache CXF 3.1.10 2017-04-25T10:28:30.414-03:00 Generated source version: 3.1.10
 * 
 */
public class BookRepoClient {

	public static void main(String[] args) throws Exception {
		/* ESTAS LINEAS SON PARA VISUALIZAR LOS MENSAJES SOAP ENVIADOS */
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");
		
		QName serviceName = new QName("http://ws.ex.mz/", "BookRepoService");
		QName portName = new QName("http://ws.ex.mz/", "BookRepoPort");

		Service service = Service.create(serviceName);
		service.addPort(portName, SOAPBinding.SOAP11HTTP_BINDING, "http://localhost:8080/CxfMaven-0.0.1-SNAPSHOT/BookRepo");
		mz.ex.ws.BookRepo client = service.getPort(portName, mz.ex.ws.BookRepo.class);

		BookRequest bookRequest = new BookRequest();
		bookRequest.setIsbn(1234L);
		BookResponse bookResponse = client.getBook(bookRequest);
		System.out.printf("Got book %s:%s %n", bookResponse.getIsbn(), bookResponse.getName());
	}

}