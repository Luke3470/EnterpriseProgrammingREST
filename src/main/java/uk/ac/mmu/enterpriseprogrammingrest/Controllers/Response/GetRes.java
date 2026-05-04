package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Response;

import java.util.List;
import javax.xml.bind.annotation.*;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookVO;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetRes {

  private List<BookVO> books;
  private int page;
  private int totalPages;

  public GetRes() {}

  public GetRes(List<BookVO> books, int page, int totalPages) {
    this.books = books;
    this.page = page;
    this.totalPages = totalPages;
  }

  public List<BookVO> getBooks() { return books; }
  public void setBooks(List<BookVO> books) { this.books = books; }

  public int getPage() { return page; }
  public void setPage(int page) { this.page = page; }

  public int getTotalPages() { return totalPages; }
  public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}