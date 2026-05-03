package uk.ac.mmu.enterpriseprogrammingrest.model;


import java.util.List;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookFilterDTO;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookVO;

public interface BookDAO {

  List<BookVO> getBooks(BookFilterDTO filter);

  BookVO getBook(int id);

  void addBook(BookVO book);

  void deleteBook(int id);

  void updateBook(BookVO book);

  int countBooks(BookFilterDTO filterDTO);

  List<String> getGenres();

  List<BookVO> getAllBooks();
}