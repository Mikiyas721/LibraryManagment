import java.sql.*;

public class DatabaseManager {
    private static DatabaseManager ourInstance = null;

    public static DatabaseManager getInstance() {
        if (ourInstance == null) ourInstance = new DatabaseManager();
        return ourInstance;
    }

    private String dBURL = "jdbc:sqlite:Database\\books.db";

    public String getdBURL() {
        return dBURL;
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }

    private Connection connection;
    private Statement statement;

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(dBURL);
            statement = connection.createStatement();
            statement.execute("create table if not exists books(Book text not null primary KEY ,Author text not null ,Edition int,Available int)");
            statement.execute("create table if not exists accounts(Username text,Password text)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
