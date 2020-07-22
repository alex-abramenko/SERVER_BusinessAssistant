package Data.JDBC;

import java.sql.*;

class JDBC {
    protected static final String PATH = "src/Data/JDBC/SQLite.db";
    protected Connection conn;
    protected Statement statmt;
    protected ResultSet resSet;

    protected void connection() throws ClassNotFoundException, SQLException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + PATH);
        statmt = conn.createStatement();
    }

    protected void closeConnection() throws SQLException {
        conn.close();
        statmt.close();
    }
}
