package com.olegmng;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcStarter {
    public static void main(String[] args) throws SQLException {

        String sql = """
                --create database book
                --create schema book
                                
                --CREATE TABLE IF NOT EXISTS book(
                --id SERIAL PRIMARY KEY,
                --name VARCHAR(255)
                                
                -- INSERT INTO book (name) VALUES 
                --('Исток'),
                --('Викинги'),
                --('Семь'),
                --('Библия'),
                --('Зодчество');
                                
                --UPDATE book
                --SET name = 'Книга Иллая'
                --WHERE id = 5;
                                
                --SELECT *
                --FROM book;
                                
                INSERT INTO book (name)
                VALUES 
                ('best_book')
                  
                """;

//       try(Connection connection = ConnectionManager.open();
//           Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)){
//           System.out.println(connection.getTransactionIsolation());
//           System.out.println(connection.getSchema());
//
//           int executeResult = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
//           ResultSet generatedKeys = statement.getGeneratedKeys();
//
//           if(generatedKeys.next()){
//               var generatedId = generatedKeys.getInt("id");
//               System.out.println(generatedId);
//
//           }

//           ResultSet executeResult = statement.executeQuery(sql);
////           System.out.println(executeResult);
////           System.out.println(statement.getUpdateCount());
//           while (executeResult.next()){
//               System.out.println(executeResult.getInt("id"));
//               System.out.println(executeResult.getString("name"));
//               System.out.println("---");
////               executeResult.afterLast();
//           }

//       }
        String b = "name";
        var booksById = getBooksById(b);
        System.out.println(booksById);

        String q1 = "Исток";
        String q2 = "autogenerate";
        var booksByIdPr = getBooksByIdWithPreparedStatement(q1, q2);
        System.out.println(booksByIdPr);

    }

    private static List<Integer> getBooksById(String name) throws SQLException {
        String sqlOne = """
                SELECT id
                FROM book
                WHERE name = %s;
                              
                """.formatted(name);
        List<Integer> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.open(); Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sqlOne);
            while (resultSet.next()) {
//                result.add(resultSet.getInt("id"));
                result.add(resultSet.getObject("id", Integer.class));//null safe
            }

        }
        return result;

    }

    // use PreparedStatement
    private static List<Integer> getBooksByIdWithPreparedStatement(String name1, String name2) throws SQLException {
        String sqlPrep = """
                SELECT id
                FROM book
                WHERE name = ? OR name = ?
                              
                """;
        List<Integer> result = new ArrayList<>();

        try (Connection connection = ConnectionManager.open();
             var preparedStatement = connection.prepareStatement(sqlPrep)) {

            preparedStatement.setFetchSize(2);
            preparedStatement.setQueryTimeout(10);
            preparedStatement.setMaxRows(4);

            preparedStatement.setString(1, name1);
            preparedStatement.setString(2, name2);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                result.add(resultSet.getInt("id"));
                result.add(resultSet.getObject("id", Integer.class));//null safe
            }

        }
        return result;

    }
}
