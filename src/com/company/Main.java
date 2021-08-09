package com.company;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws SQLException {
        menu();
    }

    public static String hashPassword(String password_plaintext) {
        String hashed_password = BCrypt.hashpw(password_plaintext, BCrypt.gensalt(12));
        return(hashed_password);
    }

    public static boolean checkPassword(String password,String hashedPassword){
        if(BCrypt.checkpw(password,hashedPassword)){
            System.out.println("The password matches");
            return true;
        }else{
            System.out.println("Password doesnt match");
            return false;
        }
    }

    public static void menu() throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press 1 for Login \nPress 2 for Registration");
        int UserChoice = Integer.parseInt(scanner.nextLine());

        if(UserChoice == 1){
            Login();
        }else if(UserChoice == 2){
            Registration();
        }else {
            System.out.println("System Exit");
            System.exit(0);
        }
    }
    public static void Login () throws SQLException{
        Scanner scanner = new Scanner(System.in);
        PreparedStatement myStmt = null;
        DBConnection db = new DBConnection();
        Statement statement = db.getConnection().createStatement();
        PreparedStatement ps;
        int PriceOfItem = 0;
        String NameOfItem = " ";

        System.out.println("Enter your username:");
        String username = scanner.nextLine();
        System.out.println("Enter your password:");
        String passwordLog = scanner.nextLine();

        //Connection to Users class
        Users users = new Users();
        users.setUsername(username);
        users.setPassword(passwordLog);

        //Check for user in db
        ps = db.getConnection().prepareStatement("SELECT password FROM users WHERE username = ?");
        ps.setString(1, users.getUsername());
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.next()){
            checkPassword(users.getPassword(),resultSet.getString("password"));

        }else{
            System.out.println("Your password or login is not correct");
            menu();
        }
        ps = db.getConnection().prepareStatement("SELECT * FROM users WHERE username=?");
        ps.setString(1,users.getUsername());
        ResultSet rs2 = ps.executeQuery();
        while(rs2.next()){
            System.out.println("You have been authorised successfully!");
            System.out.println("Here is the menu of SALAM BRO");
            String sql = "SELECT * FROM shop ORDER BY id ASC";
            resultSet = statement.executeQuery(sql);
            int Quan=0;
            while(resultSet.next()){
                System.out.println(resultSet.getString("name")+", "
                        + resultSet.getInt("price")+", "
                        + resultSet.getInt("kolvo"));
            }

            //Choosing item
            System.out.println("Choose one of them,enter the name:");
            String Order = scanner.nextLine();

            //Take users money from db
            sql = "select money from users where username ='"+users.getUsername()+"'";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                int UserMoney = resultSet.getInt("money");
                users.setMoney(UserMoney);
            }

            //Buying choose item
            sql = "select * from shop where name = '"+Order+"'";
            resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                PriceOfItem = resultSet.getInt("price");
                Quan = resultSet.getInt("kolvo");
                NameOfItem = resultSet.getString("name");
            }

            //checking for having enough money for correct item
            if(users.getMoney()>=PriceOfItem){            //buying item
                users.setMoney(users.getMoney()-PriceOfItem);    //inserting updated money
                sql = "update users"
                        +" set money ='"+users.getMoney()+"'"
                        +"where username ='"+users.getUsername()+"'";
                Statement myStmt3 = db.getConnection().createStatement();
                myStmt3.executeUpdate(sql);
                Quan--;
                sql = "update shop"
                        +" set kolvo='"+Quan+"'"
                        +"where name = '"+Order+"'";
                Statement myStmt2 = db.getConnection().createStatement();
                myStmt2.executeUpdate(sql);
                sql = "update users"
                        +" set kuplenoe ='"+NameOfItem+"'"
                        + "where username = '"+ users.getUsername()+"'";
                myStmt2.executeUpdate(sql);
                users.setKuplenoe(NameOfItem);
                System.out.println("You have successfully bought the item " +NameOfItem);
                show(users);
                menu();
            }else{ // user don't have enough money
                System.out.println("You dont have enough money!");
                menu();
            }

        }
    }
    public static void Registration() throws SQLException{
        Scanner scanner = new Scanner(System.in);
        PreparedStatement myStmt = null;
        DBConnection db = new DBConnection();
        Statement statement = db.getConnection().createStatement();
        PreparedStatement ps;

        System.out.println("Please enter your username:");
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String passwordReg = scanner.nextLine();
        System.out.println("Please enter your password again:");
        String passwordReg2 = scanner.nextLine();

        passwordReg = hashPassword(passwordReg);

        //Connection to Users class
        Users users = new Users();
        users.setUsername(username);
        users.setPassword(passwordReg);

        if(checkPassword(passwordReg2,users.getPassword()) && username.isEmpty()==false){

            //Inserting user in the database
            String sql = "insert into users "
                    + "(username,password) "
                    + " values (?,?)";
            myStmt = db.getConnection().prepareStatement(sql);
            myStmt.setString(1,users.getUsername());
            myStmt.setString(2,users.getPassword());
            myStmt.executeUpdate();

            System.out.println("How much money you have on your card?");
            int money = scanner.nextInt();
            users.setMoney(money);

            //Inserting money for users account
            sql = "update users "
                    + "set money=?"
                    + "where username=?";
            myStmt = db.getConnection().prepareStatement(sql);
            myStmt.setInt(1,users.getMoney());
            myStmt.setString(2,users.getUsername());
            myStmt.executeUpdate();
            System.out.println("Registration has been completed");
            menu();
        }else{
            System.out.println("Error, your passwords is not the same");
            System.out.println("Press 1 for continue registration \n Press 2 for Exit to the menu");
            int Choice = scanner.nextInt();
            if(Choice==1){
                Registration();
            }
            else if (Choice==2){
                menu();
            }else{System.exit(0);
            }
        }
    }

    public static void show(Users users) throws SQLException{
        PreparedStatement myStmt = null;
        DBConnection db = new DBConnection();
        Statement statement = db.getConnection().createStatement();
        PreparedStatement ps;
        ResultSet resultSet = null;
        String sql = null;

        String username = null;
        int money = 0;//dengi
        String kuplenoe = null;

        sql = "select * from users where username ='"+users.getUsername()+"'";
        resultSet = statement.executeQuery(sql);
        while(resultSet.next()){
             username = resultSet.getString("username");
             money = resultSet.getInt("money");
             kuplenoe = resultSet.getString("kuplenoe");
        }
        System.out.println("User "+username+" has "+money+"kzt and has bought "+kuplenoe);
    }

}
