import java.sql.*;
import java.sql.DriverManager;
import java.util.Scanner;  // Import the Scanner class

class project_user1 {
  public static void main(String[] args) {
    userfunction();
  }

  static void userfunction(){
    String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db13";
    String dbUsername = "Group13";
    String dbPassword = "group13group13";

    Connection con = null;
    try{ 
        Class.forName ("com.mysql.jdbc.Driver"); 
        con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        }catch (ClassNotFoundException e) { 
        System.out.println("[Error]: Java MySQL DB Driver not found!!"); 
        System.exit(0); 
        }catch (SQLException e) { 
        System.out.println(e); }
    
    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
    System.out.println("-----Operations for user menu-----");
    System.out.println("What kind of operation would you like to perform?");
    System.out.println("1. Search for cars");
    System.out.println("2. Show loan record of a user");
    System.out.println("3. Return to the main menu");
    System.out.println("Enter your choice: ");
    int choice = myObj.nextInt();  // Read user input
    
    if(choice == 1){
    Scanner myObj1 = new Scanner(System.in);  // Create a Scanner object
    System.out.println("Choose the search criterion:");
    System.out.println("1. Call number");
    System.out.println("2. Name");
    System.out.println("3. Company");
    System.out.println("Choose the search criterion:");
    int choice1 = myObj.nextInt();  // Read user input
    while(choice1 != 1 && choice1 != 2 && choice1 != 3){
      System.out.println("Choose the search criterion:");
      choice1 = myObj.nextInt();  // Read user input
    }
    if(choice1 == 1){
      System.out.println("Type in the search keyword:");
      String keyword = myObj1.nextLine();
      keyword = keyword+"%";
      try{ 
        Class.forName ("com.mysql.jdbc.Driver"); 
        con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        String psql = "SELECT * FROM car C, car_category R, produce P, ((SELECT TEMP1.callnum AS callnum, (TEMP2.c2-TEMP1.c1) AS acopy FROM (SELECT COUNT(O.copynum) AS c1, O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_d is NULL GROUP BY O.callnum) AS TEMP1, (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP2 WHERE TEMP1.callnum = TEMP2.callnum) UNION (SELECT TEMP3.callnum As callnum, TEMP3.c2 AS acopy FROM (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP3 WHERE TEMP3.callnum NOT IN (SELECT O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_d is NULL GROUP BY O.callnum))) AS TEMP4 WHERE C.ccid = R.ccid AND C.callnum = P.callnum AND C.callnum LIKE ? AND TEMP4.callnum = C.callnum ORDER BY C.callnum";
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
    
    else if(choice1 == 2){
      System.out.println("Type in the search keyword:");
      String keyword = myObj1.nextLine();
      keyword = keyword + "%";
      try{ 
        Class.forName ("com.mysql.jdbc.Driver"); 
        con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        String psql = "SELECT * FROM car C, car_category R, produce P, ((SELECT TEMP1.callnum AS callnum, (TEMP2.c2-TEMP1.c1) AS acopy FROM (SELECT COUNT(O.copynum) AS c1, O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_d is NULL GROUP BY O.callnum) AS TEMP1, (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP2 WHERE TEMP1.callnum = TEMP2.callnum) UNION (SELECT TEMP3.callnum As callnum, TEMP3.c2 AS acopy FROM (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP3 WHERE TEMP3.callnum NOT IN (SELECT O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_d is NULL GROUP BY O.callnum))) AS TEMP4 WHERE C.ccid = R.ccid AND C.callnum = P.callnum AND C.name LIKE ? AND TEMP4.callnum = C.callnum ORDER BY C.callnum";
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
        
        
    else if(choice1 == 3){
      System.out.println("Type in the search keyword:");
      String keyword = myObj1.nextLine();
      keyword = keyword + "%";
      try{ 
        Class.forName ("com.mysql.jdbc.Driver"); 
        con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        String psql = "SELECT * FROM car C, car_category R, produce P, ((SELECT TEMP1.callnum AS callnum, (TEMP2.c2-TEMP1.c1) AS acopy FROM (SELECT COUNT(O.copynum) AS c1, O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_d is NULL GROUP BY O.callnum) AS TEMP1, (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP2 WHERE TEMP1.callnum = TEMP2.callnum) UNION (SELECT TEMP3.callnum As callnum, TEMP3.c2 AS acopy FROM (SELECT COUNT(Y.copynum) AS c2,Y.callnum FROM copy Y GROUP BY Y.callnum) AS TEMP3 WHERE TEMP3.callnum NOT IN (SELECT O.callnum From copy O, rent E WHERE O.callnum = E.callnum AND O.copynum = E.copynum AND E.return_d is NULL GROUP BY O.callnum))) AS TEMP4 WHERE C.ccid = R.ccid AND C.callnum = P.callnum AND P.cname LIKE ? AND TEMP4.callnum = C.callnum ORDER BY C.callnum";
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
        userfunction();
    }
    
    if(choice == 2){
      Scanner myObj2 = new Scanner(System.in);  // Create a Scanner object
      System.out.println("Enter the user ID:");
      String userid = myObj2.nextLine();
      try{ 
        Class.forName ("com.mysql.jdbc.Driver"); 
        con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword); 
        String psql = "SELECT * FROM car, produce, rent WHERE rent.uid = ? AND car.callnum = rent.callnum AND produce.callnum = rent.callnum ORDER BY rent.checkout_d DESC";
        PreparedStatement pstmt = con.prepareStatement(psql);
        pstmt.setString(1, userid);
        ResultSet resultSet = pstmt.executeQuery();
      if(!resultSet.isBeforeFirst())
          System.out.println("No records found.");
      else
          System.out.println("|Call Num|Copy Num|Name|Company|Check-out|Max Loan|Returned?|");
          while(resultSet.next()){
            System.out.print("|" + resultSet.getString("rent.callnum"));
            System.out.print("|" + resultSet.getString("rent.copynum"));
            System.out.print("|"+resultSet.getString("car.name"));
            System.out.print("|"+resultSet.getString("produce.cname"));
            System.out.print("|" + resultSet.getString("rent.checkout_d"));
            System.out.print("|" + resultSet.getString("car.time_rent"));
            
            if(resultSet.getString("rent.return_d")== null)
              System.out.println("|" + "No" +"|");
            else
              System.out.println("|" + "Yes" +"|");
              
          }
          System.out.println("End of Query");
        }catch (ClassNotFoundException e) { 
        System.out.println("[Error]: Java MySQL DB Driver not found!!"); 
        System.exit(0); 
        }catch (SQLException e) { 
        System.out.println(e); }

        userfunction();

        }
        if(choice == 3){
          System.out.println("chensijin"); 
        }
  }

}