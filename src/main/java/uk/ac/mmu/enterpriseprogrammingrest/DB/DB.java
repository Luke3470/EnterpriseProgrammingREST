package uk.ac.mmu.enterpriseprogrammingrest.DB;

import java.sql.Connection;
import java.sql.SQLException;

public interface DB {
    Connection createCon() throws SQLException;
}
