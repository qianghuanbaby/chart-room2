package com.qh;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.qh.util.CommUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

/**
 * Author:qh
 * Created:2019/8/23
 */

public class JDBCTest {
    private static DruidDataSource dataSource;
    static {
        Properties props = CommUtils.loadProperties("datasource.properties");
        try {
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogin(){
        String userName = "test' --";
        //String userName = "test";
        String password = "1234";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            connection = (Connection) dataSource.getPooledConnection();
            String sql = "SELECT * FROM user WHERE username = '"+userName+"'" +
                    "AND password = '"+password+"'" ;
            System.out.println(sql);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                System.out.println("登录成功");
            }else{
                System.out.println("登录失败");
            }
        }catch (SQLException e){

        }finally {
            closeResources(connection,statement,resultSet);
        }
    }

    //关闭资源:更新、删除、插入
    public void closeResources(Connection connection, Statement statement){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //测试查询操作
    @Test
    public void testQuery(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            //获取数据源的连接
            connection = (Connection) dataSource.getPooledConnection();
            String sql = "SELECT *FROM user WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(sql);
            String user = "test' --";
            String pass = "1234";
            statement.setString(1, user);
            statement.setString(2,pass);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                System.out.println("登录成功");
            }else{
                System.out.println("登录失败");
            }
        }catch (SQLException e){

        }finally {
            //最后要关闭资源
            closeResources(connection,statement,resultSet);
        }
    }

    //测试插入操作
    @Test
    public void testInsert(){
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection = (Connection) dataSource.getPooledConnection();

            //插入的密码做加密操作
            String password = DigestUtils.md5Hex("123");
            String sql = "INSERT INTO user(username,password,brief)" +"values (?,?,?)";
            statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,"test1");
            statement.setString(2, password);
            statement.setString(3, "还是帅");
            //返回的是受影响的行数
            int rows = statement.executeUpdate();
            Assert.assertEquals(1,rows);
        }catch (SQLException e){

        }finally {
            closeResources(connection,statement);
        }
    }

    //关闭资源：查询
    public void closeResources(Connection connection,Statement statement,ResultSet resultSet){
        closeResources(connection,statement);
        if(resultSet!= null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
