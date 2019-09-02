package com.qh.client.dao;

import com.qh.client.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;

/**
 * Author:qh
 * Created:2019/8/24
 *
 */

public class AccountDao extends BasedDao {
    //用户注册 insert
    public boolean userReg(User user){
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection = getConnection();
            String sql = "INSERT INTO user(username,password,brief)VALUES (?,?,?)";
            statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setString(2, DigestUtils.md5Hex(user.getPassword())); //加密
            statement.setString(3, user.getBrief());
            int rows = statement.executeUpdate();
            if(rows == 1){  //一个人只能注册一个
                return true;
            }
        }catch(SQLException e){
            System.out.println("用户注册失败");
            e.printStackTrace();
        }finally {
            closeResources(connection,statement);
        }
        return false;
    }

    //用户登录 -- 查询
    public User userLogin(String userName, String password){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            connection = getConnection();
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1,userName);
            statement.setString(2,DigestUtils.md5Hex(password));
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                User user = getUser(resultSet);
                return user;
            }
        }catch (SQLException e){
            System.out.println("用户登录失败");
            e.printStackTrace();
        }finally {
            closeResources(connection,statement,resultSet);
        }
        return null;
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setBrief(resultSet.getString("brief"));
        return user;
    }
}
