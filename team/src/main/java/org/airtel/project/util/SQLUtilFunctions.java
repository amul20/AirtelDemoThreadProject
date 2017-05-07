package org.airtel.project.util;

/**
 * Created by amulyam on 06/05/17.
 */

import org.airtel.project.connector.MySqlConnector;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Helper class which does all the logic
 * Contains functions to query mysql and get the data accordingly
 */
public class SQLUtilFunctions {

    public int writeTextToTable(Connection connection,String text, String start_date) throws SQLException {
        String query = "insert into team(text_data,start_date,completed) values(?,?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,text);
        statement.setString(2,start_date);
        statement.setInt(3,1);
        int res = MySqlConnector.getInstance().insertDeleteUpdateQuery(statement);
        if(res<1) return -1;
        String query2 = "select max(id) from team";
        Statement statement1 = connection.createStatement();
        ResultSet rs = MySqlConnector.getInstance().selectUsingStatement(statement1,query2);
        rs.next();
        int id = rs.getInt(1);
        return id;
    }

    public synchronized int updateCompletedTask(Connection connection, int id) throws SQLException {
        String query1 = "select completed from team where id = "+id;
        Statement st1 = connection.createStatement();
        ResultSet rs = MySqlConnector.getInstance().selectUsingStatement(st1,query1);
        rs.next();
        int completed = rs.getInt(1);
        completed++;
        String query2 = "update team set completed = ? where id = "+id;
        PreparedStatement st2 = connection.prepareStatement(query2);
        st2.setInt(1,completed);
        int rs2 = MySqlConnector.getInstance().insertDeleteUpdateQuery(st2);
        if(completed==5) updateEndTime(connection,id);
        return rs2;
    }

    public synchronized int updateWordCountTask(Connection connection, int id, String wordCountJson) throws SQLException {
        String query2 = "update team set word_freq = ? where id ="+id;
        PreparedStatement st2 = connection.prepareStatement(query2);
        st2.setString(1,wordCountJson);
        return MySqlConnector.getInstance().insertDeleteUpdateQuery(st2);
    }

    public synchronized int updateVowelCountTask(Connection connection, int id, String vowelCountJson) throws SQLException {
        String query2 = "update team set vowel_freq=? where id ="+id;
        PreparedStatement st2 = connection.prepareStatement(query2);
        st2.setString(1,vowelCountJson);
        return MySqlConnector.getInstance().insertDeleteUpdateQuery(st2);
    }

    public synchronized int updateSentimentTask(Connection connection, int id, String sentiment) throws SQLException {
        String query2 = "update team set sentiment =? where id ="+id;
        PreparedStatement st2 = connection.prepareStatement(query2);
        st2.setString(1,sentiment);
        return MySqlConnector.getInstance().insertDeleteUpdateQuery(st2);
    }

    public synchronized int updateTextTask(Connection connection, int id, String text) throws SQLException {
        String query2 = "update team set text_data=? where id ="+id;
        PreparedStatement st2 = connection.prepareStatement(query2);
        st2.setString(1,text);
        return MySqlConnector.getInstance().insertDeleteUpdateQuery(st2);
    }

    private synchronized int updateEndTime(Connection connection, int id) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy");
        String date = sdf.format(new Date());
        String query2 = "update team set end_date=? where id ="+id;
        PreparedStatement st2 = connection.prepareStatement(query2);
        st2.setString(1,date);
        return MySqlConnector.getInstance().insertDeleteUpdateQuery(st2);
    }

    public ResultSet getARow(Connection connection,int id) throws SQLException {
        String sql = "select * from team where id = "+id;
        Statement statement = connection.createStatement();
        ResultSet rs = MySqlConnector.getInstance().selectUsingStatement(statement,sql);
        return rs;
    }






}
