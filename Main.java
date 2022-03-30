import java.util.*;
import java.sql.*;


public class Main {
    static Scanner scan = new Scanner(System.in);
    static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db13";
    static String dbUsername = "Group13";
    static String dbPassword = "group13group13";
    public static void main(String[] args){

        //Set up database connection
        Statement stmt = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
            stmt = con.createStatement();
        }catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        }catch (SQLException e){
            System.out.println(e);
        }

        //Car Renting System Start
        System.out.println("Welcome to Car Renting System!\n");
        int choice;
        do{
            System.out.println("-----Main menu-----");
            System.out.println("What kinds of operations would you like to perform?");
            System.out.println("1. Operation for Administrator");
            System.out.println("2. Operation for User");
            System.out.println("3. Operation for Manager");
            System.out.println("4. Exit this program");
            System.out.println("Enter your Choice:");
            choice = scan.nextInt();

            if(choice == 3){
                Manager.run(stmt);
            }


        }while(choice != 4);
        scan.close();
        
    }
}

