import java.sql.*;
import java.sql.DriverManager;
import java.util.Scanner;  // Import the Scanner class
import java.time.LocalDate;

public class User {
    Connection con = null;
    String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db13";
    String dbUsername = "Group13";
    String dbPassword = "group13group13";
    
    public User(){
        try{ 
            Class.forName ("com.mysql.jdbc.Driver"); 
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        }catch (ClassNotFoundException e) { 
            System.out.println("[Error]: Java MySQL DB Driver not found!!"); 
            System.exit(0); 
        }catch (SQLException e) { 
            System.out.println(e);
        }
    }


  public void run(){
    String choice;
    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
    do{
        System.out.println("-----Operations for user menu-----");
        System.out.println("What kind of operation would you like to perform?");
        System.out.println("1. Search for cars");
        System.out.println("2. Show loan record of a user");
        System.out.println("3. Return to the main menu");
        System.out.print("Enter your choice: ");
        choice = myObj.nextLine();  // Read user input
    while(!choice.equals("1") && !choice.equals("2")  && !choice.equals("3")){
      System.out.println("[Error]: Input must be an integer from 1 to 3");
      System.out.print("Enter your choice: ");
      choice = myObj.nextLine();  // Read user input
    }
    if(choice.equals("1")){
    Scanner myObj1 = new Scanner(System.in);  // Create a Scanner object
    System.out.println("Choose the search criterion:");
    System.out.println("1. Call number");
    System.out.println("2. Name");
    System.out.println("3. Company");
    System.out.print("Enter your choice: ");
    String choice1 = myObj1.nextLine();  // Read user input
    while(!choice1.equals("1") && !choice1.equals("2")  && !choice1.equals("3")){
      System.out.println("[Error]: Input must be an integer from 1 to 3");
      System.out.print("Enter your choice: ");
      choice1 = myObj1.nextLine();  // Read user input
    }
    if(choice1.equals("1")){
      Scanner myObj11 = new Scanner(System.in);  // Create a Scanner object
      System.out.print("Type in the search keyword: ");
      String keyword = myObj11.nextLine();
      keyword = "%" + keyword + "%";
      try{ 
        Class.forName ("com.mysql.jdbc.Driver"); 
        con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        String psql = "SELECT * FROM car C, car_category R, produce P, ((SELECT TEMP1.callnum AS callnum, (TEMP2.c2-TEMP1.c1) AS acopy FROM (SELECT COUNT(O.copynum) AS c1, O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_date is NULL GROUP BY O.callnum) AS TEMP1, (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP2 WHERE TEMP1.callnum = TEMP2.callnum) UNION (SELECT TEMP3.callnum As callnum, TEMP3.c2 AS acopy FROM (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP3 WHERE TEMP3.callnum NOT IN (SELECT O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_date is NULL GROUP BY O.callnum))) AS TEMP4 WHERE C.ccid = R.ccid AND C.callnum = P.callnum AND C.callnum LIKE ? AND TEMP4.callnum = C.callnum ORDER BY C.callnum";
        PreparedStatement pstmt = con.prepareStatement(psql);
        pstmt.setString(1, keyword);
        ResultSet resultSet = pstmt.executeQuery();
        if(!resultSet.isBeforeFirst())
          System.out.println("No records found.");
        else
          System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
          while(resultSet.next()){
            System.out.print("|" + resultSet.getString("C.callnum"));
            System.out.print("|" + resultSet.getString("C.name"));
            System.out.print("|"+resultSet.getString("R.ccname"));
            System.out.print("|"+resultSet.getString("P.cname"));
            System.out.println("|" + resultSet.getString("TEMP4.acopy")+"|");
          }
        }catch (ClassNotFoundException e) { 
        System.out.println("[Error]: Java MySQL DB Driver not found!!"); 
        System.exit(0); 
        }catch (SQLException e) { 
        System.out.println(e); }
      
    }
    
    else if(choice1.equals("2")){
      Scanner myObj12 = new Scanner(System.in);  // Create a Scanner object
      System.out.print("Type in the search keyword: ");
      String keyword = myObj12.nextLine();
      keyword = "%" + keyword + "%";
      try{ 
        Class.forName ("com.mysql.jdbc.Driver"); 
        con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        String psql = "SELECT * FROM car C, car_category R, produce P, ((SELECT TEMP1.callnum AS callnum, (TEMP2.c2-TEMP1.c1) AS acopy FROM (SELECT COUNT(O.copynum) AS c1, O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_date is NULL GROUP BY O.callnum) AS TEMP1, (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP2 WHERE TEMP1.callnum = TEMP2.callnum) UNION (SELECT TEMP3.callnum As callnum, TEMP3.c2 AS acopy FROM (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP3 WHERE TEMP3.callnum NOT IN (SELECT O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_date is NULL GROUP BY O.callnum))) AS TEMP4 WHERE C.ccid = R.ccid AND C.callnum = P.callnum AND C.name LIKE ? AND TEMP4.callnum = C.callnum ORDER BY C.callnum";
        PreparedStatement pstmt = con.prepareStatement(psql);
        pstmt.setString(1, keyword);
        ResultSet resultSet = pstmt.executeQuery();
        if(!resultSet.isBeforeFirst())
          System.out.println("No records found.");
        else
          System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
          while(resultSet.next()){
            System.out.print("|" + resultSet.getString("C.callnum"));
            System.out.print("|" + resultSet.getString("C.name"));
            System.out.print("|"+resultSet.getString("R.ccname"));
            System.out.print("|"+resultSet.getString("P.cname"));
            System.out.println("|" + resultSet.getString("TEMP4.acopy")+"|");
          }
          System.out.println("End of Query");
        }catch (ClassNotFoundException e) { 
        System.out.println("[Error]: Java MySQL DB Driver not found!!"); 
        System.exit(0); 
        }catch (SQLException e) { 
        System.out.println(e); }
        }
        
        
    else if(choice1.equals("3")){
      Scanner myObj13 = new Scanner(System.in);  // Create a Scanner object
      System.out.print("Type in the search keyword: ");
      String keyword = myObj13.nextLine();
      keyword = "%" + keyword + "%";
      try{ 
        Class.forName ("com.mysql.jdbc.Driver"); 
        con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        String psql = "SELECT * FROM car C, car_category R, produce P, ((SELECT TEMP1.callnum AS callnum, (TEMP2.c2-TEMP1.c1) AS acopy FROM (SELECT COUNT(O.copynum) AS c1, O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_date is NULL GROUP BY O.callnum) AS TEMP1, (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP2 WHERE TEMP1.callnum = TEMP2.callnum) UNION (SELECT TEMP3.callnum As callnum, TEMP3.c2 AS acopy FROM (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP3 WHERE TEMP3.callnum NOT IN (SELECT O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_date is NULL GROUP BY O.callnum))) AS TEMP4 WHERE C.ccid = R.ccid AND C.callnum = P.callnum AND P.cname LIKE ? AND TEMP4.callnum = C.callnum ORDER BY C.callnum";
      PreparedStatement pstmt = con.prepareStatement(psql);
      pstmt.setString(1, keyword);
      ResultSet resultSet = pstmt.executeQuery();
      if(!resultSet.isBeforeFirst())
          System.out.println("No records found.");
      else
          System.out.println("|Call Num|Name|Car Category|Company|Available No. of Copy|");
          while(resultSet.next()){
            System.out.print("|" + resultSet.getString("C.callnum"));
            System.out.print("|" + resultSet.getString("C.name"));
            System.out.print("|"+resultSet.getString("R.ccname"));
            System.out.print("|"+resultSet.getString("P.cname"));
            System.out.println("|" + resultSet.getString("TEMP4.acopy")+"|");
          }
          System.out.println("End of Query");
        }catch (ClassNotFoundException e) { 
        System.out.println("[Error]: Java MySQL DB Driver not found!!"); 
        System.exit(0); 
        }catch (SQLException e) { 
        System.out.println(e); }
        }
    }
    
    else if(choice.equals("2")){
      String checkout;
      int maxloan;
      Scanner myObj2 = new Scanner(System.in);  // Create a Scanner object
      System.out.print("Enter the user ID: ");
      String userid = myObj2.nextLine();
      try{ 
        Class.forName ("com.mysql.jdbc.Driver"); 
        con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        String psql = "SELECT * FROM car, produce, rent WHERE rent.uid = ? AND car.callnum = rent.callnum AND produce.callnum = rent.callnum ORDER BY rent.checkout DESC";
        PreparedStatement pstmt = con.prepareStatement(psql);
        pstmt.setString(1, userid);
        ResultSet resultSet = pstmt.executeQuery();
      if(!resultSet.isBeforeFirst())
          System.out.println("No records found.");
      else
          System.out.println("|Call Num|Copy Num|Name|Company|Check-out|Returned?|");
          while(resultSet.next()){
            System.out.print("|" + resultSet.getString("rent.callnum"));
            System.out.print("|" + resultSet.getString("rent.copynum"));
            System.out.print("|"+resultSet.getString("car.name"));
            System.out.print("|"+resultSet.getString("produce.cname"));
            System.out.print("|" + resultSet.getString("rent.checkout"));
            if(resultSet.getString("rent.return_date")== null)
              System.out.println("|" + "No" + "|");
            else
              System.out.println("|" + "Yes" + "|");
          }
          System.out.println("End of Query");
        }catch (ClassNotFoundException e) { 
        System.out.println("[Error]: Java MySQL DB Driver not found!!"); 
        System.exit(0); 
        }catch (SQLException e) { 
        System.out.println(e); }
        }
        
        }while(!choice.equals("3"));
        System.out.println("Byebye!"); 
        
  }
} 
