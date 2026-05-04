package uk.ac.mmu.enterpriseprogrammingrest.Controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Response.GetRes;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer.Serializer;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders.Decoder;
import uk.ac.mmu.enterpriseprogrammingrest.model.BookDAO;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookFilterDTO;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookVO;

@WebServlet("/bookAPI")
public class RestBookServlet extends HttpServlet {

  private ContentRegistry registry;
  private BookDAO bookDAO;

  @Override
  public void init() {
    this.registry = new ContentRegistry();
    this.bookDAO = (BookDAO) getServletContext().getAttribute("bookDAO");
  }


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String accept = request.getHeader("Accept");

    String outType = ContentNegotiator.negotiate(
        accept,
        registry.supportedTypes()
    );

    if (outType == null) {
      response.sendError(406, "Unsupported output type");
      return;
    }

    Serializer<?> serializer = registry.getSerializer(outType);

    BookFilterDTO filter = BookFilterDTO.fromRequest(request);

    List<BookVO> books = bookDAO.getBooks(filter);
    int count = bookDAO.countBooks(filter);
    int totalPages = (int) Math.ceil((double) count / filter.getSize());

    GetRes data = new GetRes(books, filter.getPage(), totalPages);

    response.setContentType(outType);
    response.setCharacterEncoding("UTF-8");

    try {
      response.getWriter().write(
          ((Serializer<Object>) serializer).serialize(data)
      );
    } catch (Exception e) {
      response.sendError(500, e.getMessage());
    }
  }


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String contentType = cleanContentType(request.getContentType());
    String accept = request.getHeader("Accept");

    Decoder<BookVO> decoder = registry.getDecoder(contentType);

    if (decoder == null) {
      response.sendError(415, "Unsupported input type");
      return;
    }

    BookVO data;

    try {
      data = decoder.decode(readBody(request), BookVO.class);
      if (data.getId() != null) {
        throw new IllegalArgumentException("No ID is required for Post");
      }
      data.validate();
    } catch (IllegalArgumentException e) {
      response.sendError(400, e.getMessage());
      return;
    } catch (Exception e) {
      response.sendError(400, "Invalid request body: " + e.getMessage());
      return;
    }

    String outType = ContentNegotiator.negotiate(accept, registry.supportedTypes());

    if (outType == null) {
      response.sendError(406, "Unsupported output type");
      return;
    }

    Serializer<?> serializer = registry.getSerializer(outType);

    try {
      BookVO newBook = bookDAO.addBook(data);

      response.setStatus(HttpServletResponse.SC_CREATED);
      response.setContentType(outType);
      response.setCharacterEncoding("UTF-8");

      response.setHeader("Location", "/bookAPI?id=" + newBook.getId());

      response.getWriter().write(
          ((Serializer<Object>) serializer).serialize(newBook)
      );

    } catch (Exception e) {
      response.sendError(500, "Failed to create book: " + e.getMessage());
    }
  }

  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String contentType = cleanContentType(request.getContentType());

    Decoder<BookVO> decoder = registry.getDecoder(contentType);

    if (decoder == null) {
      response.sendError(415, "Unsupported input type");
      return;
    }

    BookVO data;

    try {
      data = decoder.decode(readBody(request), BookVO.class);

      if (data.getId() == null || data.getId() <= 0) {
        throw new IllegalArgumentException("Valid ID is required for update");
      }

      data.validate();

    } catch (IllegalArgumentException e) {
      response.sendError(400, e.getMessage());
      return;
    } catch (Exception e) {
      response.sendError(400, "Invalid request body: " + e.getMessage());
      return;
    }

    try {
      bookDAO.updateBook(data);

      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      response.setContentLength(0);

    } catch (Exception e) {
      response.sendError(500, "Failed to update book: " + e.getMessage());
    }
  }

  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String idParam = request.getParameter("id");

    if (idParam == null) {
      response.sendError(400, "Missing id");
      return;
    }

    try {
      int id = Integer.parseInt(idParam);
      bookDAO.deleteBook(id);

      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      response.setContentLength(0);

    } catch (Exception e) {
      response.sendError(500, "Failed to delete book: " + e.getMessage());
    }
  }


  private String readBody(HttpServletRequest request) throws IOException {
    return request.getReader()
        .lines()
        .reduce("", (a, b) -> a + b);
  }

  private String cleanContentType(String contentType) {
    if (contentType == null) return null;
    return contentType.split(";")[0];
  }
}