package mz.ex.ws;

public class BookRepoImpl implements BookRepo {
	@Override
	public BookResponse getBook(BookRequest bookRequest) {
		BookResponse bookResponse = new BookResponse(bookRequest.getIsbn(), "Dark tower");
		return bookResponse;
	}
}
