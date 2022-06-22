package uz.hibernate.exceptions;

import java.sql.SQLException;

public class CustomSQLException extends Exception{

    public CustomSQLException(String message) {
        super(message);
    }

    public CustomSQLException(SQLException e) {
        this(e.getMessage());
    }
}
