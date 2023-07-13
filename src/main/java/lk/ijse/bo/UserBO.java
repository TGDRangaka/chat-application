package lk.ijse.bo;

import lk.ijse.model.UserDTO;

import java.sql.SQLException;

public interface UserBO {
    Boolean checkUser(String username, String password) throws SQLException;
    boolean saveUser(UserDTO user) throws SQLException;
    String getlastId() throws SQLException;
}
