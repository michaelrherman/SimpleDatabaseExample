package com.company;

import java.sql.*;
import java.util.*;

public class Main {
    private static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static String protocol = "jdbc:derby:";
    private static String dbName = "Cubes";
    private static final String USER = "username";
    private static final String PASS = "password";

    public static void main(String[] args) throws SQLException {
        Statement statement = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psDelete = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(protocol + dbName + ";create=true", USER, PASS);
            statement = conn.createStatement();

            try {
                String createTableSQL = "CREATE TABLE Cubes (Name varchar(50), Time float)";
                statement.executeUpdate(createTableSQL);
                System.out.println("Created table");
            } catch (SQLException e) {
                System.out.println("SQL Error");
                System.out.println(e);
            }

            String prepStatInsert = "INSERT INTO Cubes VALUES ( ?, ? )";
            psInsert = conn.prepareStatement(prepStatInsert);
            psInsert.setString(1, "Cubestormer II robot");
            psInsert.setFloat(2, (float) 5.270);
            psInsert.executeUpdate();
            psInsert.setString(1, "Fakhri Raihaan");
            psInsert.setFloat(2, (float) 27.93);
            psInsert.executeUpdate();
            psInsert.setString(1, "Ruxin Liu");
            psInsert.setFloat(2, (float) 99.33);
            psInsert.executeUpdate();
            psInsert.setString(1, "Mats Valk");
            psInsert.setFloat(2, (float) 6.27);
            psInsert.executeUpdate();

            String printAll = "Select * From Cubes";
            rs = statement.executeQuery(printAll);

            while (rs.next()){
                String name = rs.getString(1);
                float time = rs.getFloat(2);
                System.out.println(name+" "+time);
            }

            while (true) {
                Scanner scanner = new Scanner(System.in);
                int response = -1;
                System.out.println("Enter your choice \n 1 - Add new solver & time \n 2 - Update solver time \n" +
                        " 3 - Delete solver \n 4 - Quit \n");
                response = Integer.parseInt(scanner.nextLine());

                if (response == 1) {
                    System.out.println("Enter name of new solver to add");
                    String addName = scanner.nextLine();
                    System.out.println("Enter new solver's time");
                    float addTime = Float.parseFloat(scanner.nextLine());
                    psInsert.setString(1, addName);
                    psInsert.setFloat(2, addTime);
                    psInsert.executeUpdate();

                    rs = statement.executeQuery(printAll);
                    while (rs.next()) {
                        String name = rs.getString(1);
                        float time = rs.getFloat(2);
                        System.out.println(name + " " + time);
                    }
                    System.out.println();

                } else if (response == 2) {
                    System.out.println("Enter name of solver to update");
                    String updateName = scanner.nextLine();
                    System.out.println("Enter new solver's time");
                    float updateTime = Float.parseFloat(scanner.nextLine());

                    String prepStatUpdate = "UPDATE Cubes SET TIME = ? Where Name = ?";
                    psUpdate = conn.prepareStatement(prepStatUpdate);
                    psUpdate.setFloat(1, updateTime);
                    psUpdate.setString(2, updateName);
                    psUpdate.executeUpdate();

                    rs = statement.executeQuery(printAll);
                    while (rs.next()) {
                        String name = rs.getString(1);
                        float time = rs.getFloat(2);
                        System.out.println(name + " " + time);
                    }
                    System.out.println();

                } else if (response == 3) {
                    System.out.println("Enter name of solver to delete");
                    String deleteName = scanner.nextLine();

                    String prepStateDelete = "Delete From Cubes Where Name = ?";
                    psDelete = conn.prepareStatement(prepStateDelete);
                    psDelete.setString(1, deleteName);
                    psDelete.executeUpdate();

                    rs = statement.executeQuery(printAll);
                    while (rs.next()) {
                        String name = rs.getString(1);
                        float time = rs.getFloat(2);
                        System.out.println(name + " " + time);
                    }
                    System.out.println();

                } else if (response == 4) {
                    System.out.println("Program closing");
                    break;

                } else {
                    System.out.println("Input not valid - Re-enter");
                }
            }
            String deleteTableSQL = "DROP TABLE Cubes";
            statement.executeUpdate(deleteTableSQL);
            System.out.println("Deleted users table");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    System.out.println("ResultSet closed");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (statement != null) {
                    statement.close();
                    System.out.println("Statement closed");
                }
            } catch (SQLException se){
                se.printStackTrace();
            }
            try {
                if (psInsert != null) {
                    psInsert.close();
                    System.out.println("Prepared Statement closed");
                }
            } catch (SQLException se){
                se.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("Database connection closed");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}

