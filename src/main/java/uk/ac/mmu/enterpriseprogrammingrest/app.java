package uk.ac.mmu.enterpriseprogrammingrest;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import uk.ac.mmu.enterpriseprogrammingrest.DB.DB;
import uk.ac.mmu.enterpriseprogrammingrest.DB.MySQLDB;
import uk.ac.mmu.enterpriseprogrammingrest.model.BookDAO;
import uk.ac.mmu.enterpriseprogrammingrest.model.BookDAOImpl;


@WebListener
public class app implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent sce) {

    DB db = new MySQLDB();
    BookDAO bookDAO = new BookDAOImpl(db);

    ServletContext ctx = sce.getServletContext();
    ctx.setAttribute("bookDAO", bookDAO);
  }
}