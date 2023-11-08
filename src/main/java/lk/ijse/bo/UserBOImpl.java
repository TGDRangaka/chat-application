package lk.ijse.bo;

import lk.ijse.dao.UserDAO;
import lk.ijse.dao.UserDAOImpl;
import lk.ijse.entity.User;
import lk.ijse.model.UserDTO;

import java.sql.SQLException;

public class UserBOImpl implements UserBO {

    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public Boolean checkUser(String username, String password) throws SQLException {
        return userDAO.checkUser(username, password);
    }

    @Override
    public boolean saveUser(UserDTO user) throws SQLException {
        return userDAO.saveUser(new User(user.getUserId(), user.getUsername(), user.getPassword()));
    }

    @Override
    public String getlastId() throws SQLException {
        return userDAO.getLastId();
    }
}
