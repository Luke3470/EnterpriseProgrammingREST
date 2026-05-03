package uk.ac.mmu.enterpriseprogrammingrest.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDB implements DB{
    private final String user = "";
    private final String password = "";
    private final String conn_string = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+ user;
    private final int retries = 3;

    @Override
    public Connection createCon() {
        return connectionRetry();
    }

    private Connection connectionRetry() {
        int attempts = 0;

        while (attempts < retries) {
            try {
                attempts++;
                //System.out.println("Attempt " + attempts + " to connect...");

                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(conn_string, user, password);
               // System.out.println("Connected successfully!");
                return conn;

            } catch (SQLException | ClassNotFoundException e) {
                System.err.println("Connection failed: " + e.getMessage());

                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            }
        }

        System.err.println("All connection attempts failed.");
        throw new RuntimeException("Could Not Connect to DB");
    }

}
