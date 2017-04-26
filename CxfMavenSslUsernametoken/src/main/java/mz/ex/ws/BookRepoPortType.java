package mz.ex.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

/* Con esta annotation es posible hacer que al invocar a un servicio, se agregue el Addressing en el Header del Soap */
//@Addressing

@WebService(portName = "BookRepoPort", serviceName = "BookRepoService")
public interface BookRepoPortType {
	@WebMethod(operationName = "getBook")
	@WebResult(name = "book", targetNamespace = "")
	public BookResponse getBook(@WebParam(name = "bookRequest") BookRequest bookRequest);
}
