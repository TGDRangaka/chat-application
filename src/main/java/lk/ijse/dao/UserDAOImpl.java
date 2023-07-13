package lk.ijse.dao;

import lk.ijse.entity.User;
import lk.ijse.service.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO{
    @Override
    public Boolean checkUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ? && password = md5(?)";

        ResultSet rs = CrudUtil.execute(sql, username, password);
        return rs.next();
    }

    @Override
    public boolean saveUser(User user) throws SQLException {
        String sql = "INSERT INTO user VALUES(?,?,md5(?))";

        return CrudUtil.execute(sql, user.getUserId(), user.getUsername(), user.getPassword());
    }

    @Override
    public String getLastId() throws SQLException {
        String sql = "SELECT * FROM user ORDER BY userId DESC LIMIT 1";

        ResultSet rs = CrudUtil.execute(sql);

        if (rs.next()) {
            return rs.getString(1);
        } else {
            return "U001";
        }
    }
}
