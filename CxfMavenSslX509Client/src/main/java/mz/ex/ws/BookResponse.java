package mz.ex.ws;

public class BookResponse {
	private long isbn;
	private String name;

	public BookResponse() {
	}

	public BookResponse(long isbn, String name) {
		this.isbn = isbn;
		this.name = name;
	}

	public long getIsbn() {
		return isbn;
	}

	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
