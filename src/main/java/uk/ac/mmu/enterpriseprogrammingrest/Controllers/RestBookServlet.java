package uk.ac.mmu.enterpriseprogrammingrest.Controllers;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Response.GetRes;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Serializer.Serializer;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.decoders.Decoder;
import uk.ac.mmu.enterpriseprogrammingrest.Service.BookService;
import uk.ac.mmu.enterpriseprogrammingrest.model.BookDAO;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookFilterDTO;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookVO;

@WebServlet("/bookAPI")
public class RestBookServlet extends HttpServlet {

  private ContentRegistry registry;
  private BookService bookService;

  @Override
  public void init() {
    this.registry = new ContentRegistry();
    BookDAO bookDAO = (BookDAO) getServletContext().getAttribute("bookDAO");
    this.bookService = new BookService(bookDAO);
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

    Serializer<GetRes> serializer = registry.getSerializer(outType);

    BookFilterDTO filter = BookFilterDTO.fromRequest(request);

    GetRes data = bookService.getBooks(filter);

    response.setContentType(outType);
    response.setCharacterEncoding("UTF-8");

    try {
      response.getWriter().write(serializer.serialize(data));
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
    } catch (Exception e) {
      response.sendError(400, "Invalid request body: " + e.getMessage());
      return;
    }

    String outType = ContentNegotiator.negotiate(accept, registry.supportedTypes());

    if (outType == null) {
      response.sendError(406, "Unsupported output type");
      return;
    }

    Serializer<BookVO> serializer = registry.getSerializer(outType);

    try {
      BookVO newBook = bookService.createBook(data);

      response.setStatus(HttpServletResponse.SC_CREATED);
      response.setContentType(outType);
      response.setCharacterEncoding("UTF-8");

      response.setHeader(
          "Location",
          request.getRequestURL().toString() + "?id=" + newBook.getId()
      );

      response.getWriter().write(serializer.serialize(newBook));

    } catch (IllegalArgumentException e) {
      response.sendError(400, e.getMessage());
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
    } catch (Exception e) {
      response.sendError(400, "Invalid request body: " + e.getMessage());
      return;
    }

    try {
      boolean updated = bookService.updateBook(data);

      if (!updated) {
        response.sendError(404, "Book not found");
        return;
      }

      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      response.setContentLength(0);

    } catch (IllegalArgumentException e) {
      response.sendError(400, e.getMessage());
    } catch (Exception e) {
      response.sendError(500, "Failed to update book: " + e.getMessage());
    }
  }

  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String idParam = request.getParameter("id");

    if (idParam == null || idParam.isBlank()) {
      response.sendError(400, "Missing id");
      return;
    }

    try {
      int id = Integer.parseInt(idParam);

      bookService.deleteBook(id);

      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
      response.setContentLength(0);

    } catch (NumberFormatException e) {
      response.sendError(400, "Invalid id format");
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