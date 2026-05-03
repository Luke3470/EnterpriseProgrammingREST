package uk.ac.mmu.enterpriseprogrammingrest.Controllers;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/bookAPI")
public class RestBookServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    PrintWriter out = response.getWriter();
    out.println("<html><body>");
    out.println("<h1>" +  "</h1>");
    out.println("</body></html>");
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    PrintWriter out = response.getWriter();
    out.println("<html><body>");
    out.println("<h1>" +  "</h1>");
    out.println("</body></html>");
  }

  @Override
  public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();

  }

  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();


  }
}