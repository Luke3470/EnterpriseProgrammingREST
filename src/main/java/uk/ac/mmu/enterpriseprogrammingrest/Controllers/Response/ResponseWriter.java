package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Response;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class ResponseWriter {

  public static void write(HttpServletResponse res, String type, String body) throws IOException {
    res.setContentType(type);
    res.setCharacterEncoding("UTF-8");
    res.getWriter().write(body);
  }

  public static void created(HttpServletResponse res, String location, String type, String body) throws IOException {
    res.setStatus(HttpServletResponse.SC_CREATED);
    res.setHeader("Location", location);
    write(res, type, body);
  }

  public static void noContent(HttpServletResponse res) {
    res.setStatus(HttpServletResponse.SC_NO_CONTENT);
    res.setContentLength(0);
  }
}