package uk.ac.mmu.enterpriseprogrammingrest.Service;

import java.util.List;
import uk.ac.mmu.enterpriseprogrammingrest.model.BookDAO;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookFilterDTO;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookVO;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Response.GetRes;

public class BookService {

  private final BookDAO bookDAO;

  public BookService(BookDAO bookDAO) {
    this.bookDAO = bookDAO;
  }

  public GetRes getBooks(BookFilterDTO filter) {

    List<BookVO> books = bookDAO.getBooks(filter);
    int count = bookDAO.countBooks(filter);
    int totalPages = (int) Math.ceil((double) count / filter.getSize());

    return new GetRes(books, filter.getPage(), totalPages);
  }

  public BookVO createBook(BookVO data) {

    if (data.getId() != null) {
      throw new IllegalArgumentException("No ID is required for Post");
    }

    data.validate();
    return bookDAO.addBook(data);
  }

  public boolean updateBook(BookVO data) {

    if (data.getId() == null || data.getId() <= 0) {
      throw new IllegalArgumentException("Valid ID is required for update");
    }

    data.validate();
    return bookDAO.updateBook(data);
  }

  public void deleteBook(String id) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("id Cannot be null or blank");
    }

    int parsedId;
    try {
      parsedId = Integer.parseInt(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid id format");
    }

    bookDAO.deleteBook(parsedId);
  }
}