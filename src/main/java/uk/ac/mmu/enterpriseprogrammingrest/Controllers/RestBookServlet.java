package uk.ac.mmu.enterpriseprogrammingrest.Controllers;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Response.ErrorWriter;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Response.GetRes;
import uk.ac.mmu.enterpriseprogrammingrest.Controllers.Response.ResponseWriter;
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

    String outType = registry.negotiate(request);

    if (outType == null) {
      ErrorWriter.unsupportedMedia(response);
      return;
    }

    Serializer<GetRes> serializer = registry.getSerializer(outType);

    BookFilterDTO filter = BookFilterDTO.fromRequest(request);

    GetRes data = bookService.getBooks(filter);

    try {
      ResponseWriter.write(response, outType, serializer.serialize(data));
    } catch (Exception e) {
      ErrorWriter.serverError(response, e.getMessage());
    }
  }


  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String contentType = cleanContentType(request.getContentType());
    String outType = registry.negotiate(request);

    if (outType == null) {
      ErrorWriter.unsupportedMedia(response);
      return;
    }

    Serializer<BookVO> serializer = registry.getSerializer(outType);
    Decoder<BookVO> decoder = registry.requireDecoder(contentType);

    BookVO data;

    try {
      data = decoder.decode(readBody(request), BookVO.class);
    } catch (Exception e) {
      ErrorWriter.badRequest(response, "Invalid request body: " + e.getMessage());
      return;
    }

    try {
      BookVO newBook = bookService.createBook(data);
      ResponseWriter.created(response, request.getRequestURL().toString() + "?id=" + newBook.getId(),outType,serializer.serialize(newBook));

    } catch (IllegalArgumentException e) {
      ErrorWriter.badRequest(response, e.getMessage());
    } catch (Exception e) {
      ErrorWriter.serverError(response,"Failed to create book: " + e.getMessage());
    }
  }

  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String contentType = cleanContentType(request.getContentType());

    Decoder<BookVO> decoder = registry.requireDecoder(contentType);

    BookVO data;

    try {
      data = decoder.decode(readBody(request), BookVO.class);
    } catch (Exception e) {
      ErrorWriter.badRequest(response, e.getMessage());
      return;
    }

    try {
      boolean updated = bookService.updateBook(data);

      if (!updated) {
        ErrorWriter.notFound(response, "Book not found");
        return;
      }

      ResponseWriter.noContent(response);

    } catch (IllegalArgumentException e) {
      ErrorWriter.badRequest(response, e.getMessage());
    } catch (Exception e) {
      ErrorWriter.serverError(response, "Failed to update book: " + e.getMessage());
    }
  }

  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String idParam = request.getParameter("id");
    try {
      bookService.deleteBook(idParam);

      ResponseWriter.noContent(response);

    } catch (NumberFormatException e) {
      ErrorWriter.badRequest(response, "Invalid id");
    } catch (Exception e) {
      ErrorWriter.serverError(response, "Failed to delete book: " + e.getMessage());
    }
  }


  private String readBody(HttpServletRequest request) throws IOException {
    return request.getReader()
        .lines()
        .reduce("", (a, b) -> a + b + "\n");
  }

  private String cleanContentType(String contentType) {
    if (contentType == null) return null;
    return contentType.split(";")[0];
  }

}