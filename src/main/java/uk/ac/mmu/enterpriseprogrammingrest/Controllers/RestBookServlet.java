package uk.ac.mmu.enterpriseprogrammingrest.Controllers;

import java.io.*;
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

  private final ContentRegistry registry = new ContentRegistry();
  private BookDAO bookDAO;

  @Override
  public void init() {
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
    Serializer serializer = registry.getSerializer(outType);

    BookFilterDTO filter = BookFilterDTO.fromRequest(request);

    List<BookVO> books = bookDAO.getBooks(filter);
    int count = bookDAO.countBooks(filter);
    int totalPages = (int) Math.ceil((double) count / filter.getSize());
    int currentPage = filter.getPage();

    GetRes data = new GetRes(
        books,
        currentPage,
        totalPages
    );

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

    String contentType = request.getContentType();
    String accept = request.getHeader("Accept");

    Decoder decoder = registry.getDecoder(contentType);

    if (decoder == null) {
      response.sendError(415, "Unsupported input type");
      return;
    }



    String outType = ContentNegotiator.negotiate(
        accept,
        registry.supportedTypes()
    );

    if (outType == null) {
      response.sendError(406, "Unsupported output type");
      return;
    }

    Serializer serializer = registry.getSerializer(outType);

    response.setContentType(outType);
    response.setCharacterEncoding("UTF-8");

  }

  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String contentType = request.getContentType();

    Decoder decoder = registry.getDecoder(contentType);

    if (decoder == null) {
      response.sendError(415, "Unsupported input type");
      return;
    }




    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }

  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String id = request.getParameter("id");

    if (id == null) {
      response.sendError(400, "Missing id");
      return;
    }


    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
  }
}