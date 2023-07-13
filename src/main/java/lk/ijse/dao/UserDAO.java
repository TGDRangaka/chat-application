package lk.ijse.dao;

import lk.ijse.entity.User;

import java.sql.SQLException;

public interface UserDAO {
    Boolean checkUser(String username, String password) throws SQLException;
    boolean saveUser(User user) throws SQLException;

    String getLastId() throws SQLException;
}
