package uk.ac.mmu.enterpriseprogrammingrest.Controllers;

import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("bookAPI")
public class RestBookServlet extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");

    PrintWriter out = response.getWriter();
    out.println("<html><body>");
    out.println("<h1>" +  "</h1>");
    out.println("</body></html>");
  }

}