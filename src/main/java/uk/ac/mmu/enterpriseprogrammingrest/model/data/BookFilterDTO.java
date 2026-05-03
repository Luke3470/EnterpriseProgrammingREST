package uk.ac.mmu.enterpriseprogrammingrest.model.data;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class BookFilterDTO {

  private String query;
  private String dateFrom;
  private String dateTo;
  private List<String> genres = new ArrayList<>();

  private int page = 1;
  private final int size = 12;

  public static BookFilterDTO fromRequest(HttpServletRequest req) {

    BookFilterDTO filter = new BookFilterDTO();

    filter.query = trim(req.getParameter("books"));
    filter.dateFrom = trim(req.getParameter("dateFrom"));
    filter.dateTo = trim(req.getParameter("dateTo"));
    String[] genreArr = req.getParameterValues("genre");
    if (genreArr != null) {
      filter.genres = Arrays.stream(genreArr)
          .filter(g -> g != null && !g.trim().isEmpty())
          .toList();
    }

    String pageParam = req.getParameter("page");
    if (pageParam != null) {
      try {
        filter.page = Integer.parseInt(pageParam);
      } catch (NumberFormatException e) {
        filter.page = 1;
      }
    }

    filter.validateAndNormalize();
    return filter;
  }

  public void validateAndNormalize() {

    query = clean(query);
    dateFrom = clean(dateFrom);
    dateTo = clean(dateTo);
    genres = genres.stream()
        .filter(g -> g != null && !g.trim().isEmpty())
        .collect(Collectors.toList());

    if (page < 1) page = 1;
  }


  private static String trim(String value) {
    return value != null ? value.trim() : null;
  }

  private static String clean(String value) {
    return (value == null || value.trim().isEmpty()) ? null : value.trim();
  }

  public int getOffset() {
    return (page - 1) * size;
  }

  public int getLimit() {
    return size;
  }


  public String getQuery() { return query; }
  public String getDateFrom() { return dateFrom; }
  public String getDateTo() { return dateTo; }
  public List<String> getGenres() { return genres; }
  public int getPage() { return page; }
  public int getSize() { return size; }
}