package mz.ex.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService
public interface BookRepo {
	@WebMethod
	@WebResult(name = "bookResponse")
	public BookResponse getBook(@WebParam(name = "bookRequest") BookRequest bookRequest);
}