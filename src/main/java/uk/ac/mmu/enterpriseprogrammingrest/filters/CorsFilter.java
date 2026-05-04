package uk.ac.mmu.enterpriseprogrammingrest.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CorsFilter implements Filter {

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletResponse response = (HttpServletResponse) res;

    response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
    response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept");
    response.setHeader("Access-Control-Allow-Credentials", "true");

    if (((javax.servlet.http.HttpServletRequest) req).getMethod().equals("OPTIONS")) {
      response.setStatus(HttpServletResponse.SC_OK);
      return;
    }
    chain.doFilter(req, res);
  }
}