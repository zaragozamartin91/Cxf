package mz.ex.ws;

import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(portName = "BookRepoPort", serviceName = "BookRepoService")
public class BookRepoImpl {
	@WebMethod(operationName = "GetBook")
    @WebResult(name = "book", targetNamespace = "")
	public BookResponse getBook(BookRequest bookRequest) {
		BookResponse bookResponse = new BookResponse(bookRequest.getIsbn(), "Dark tower");
		return bookResponse;
	}
}
