package mz.ex.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(portName = "BookRepoPort", serviceName = "BookRepoService")
public class BookRepoImpl implements BookRepo {
	@WebMethod(operationName = "getBook")
	@WebResult(name = "book")
	public BookResponse getBook(@WebParam(name = "bookRequest") BookRequest bookRequest) {
		BookResponse bookResponse = new BookResponse(bookRequest.getIsbn(), "Dark tower");
		return bookResponse;
	}
}
