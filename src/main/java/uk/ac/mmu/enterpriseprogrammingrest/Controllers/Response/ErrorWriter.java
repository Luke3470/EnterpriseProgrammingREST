package uk.ac.mmu.enterpriseprogrammingrest.Controllers.Response;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class ErrorWriter {

  public static void badRequest(HttpServletResponse res, String msg) throws IOException {
    res.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
  }

  public static void notFound(HttpServletResponse res, String msg) throws IOException {
    res.sendError(HttpServletResponse.SC_NOT_FOUND, msg);
  }

  public static void unsupportedMedia(HttpServletResponse res) throws IOException {
    res.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
  }

  public static void serverError(HttpServletResponse res, String msg) throws IOException {
    res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
  }
}