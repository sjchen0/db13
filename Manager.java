import java.util.*;
import java.sql.*;
import java.time.LocalDate;



public class Manager {
    //static Scanner scan = new Scanner(System.in);
    Connection conn = null;
    public Manager(){
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db13";
        String dbUsername = "Group13";
        String dbPassword = "group13group13";

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        }catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        }catch (SQLException e){
            System.out.println(e);
        }
        
    }

    public void run(){
        Scanner scan = new Scanner(System.in);
        String choice;
        do{
            System.out.print("\n-----Operations for manager menu-----\n");
            System.out.println("What kinds of operations would you like to perform?");
            System.out.println("1. Car Renting");
            System.out.println("2. Car Returning");
            System.out.println("3. List all un-returned car copies which are checked-out within a period");
            System.out.println("4. Return to the main menu");
            System.out.print("Enter your choice: ");
            choice = scan.nextLine();

            if (choice.equals("1"))
                carRent();
            else if (choice.equals("2"))
                carReturn();
            else if (choice.equals("3"))
                carList();
            else if (! choice.equals("4"))
            System.out.println("[Error]: Input must be an integer from 1 to 4");

        }while(! choice.equals("4"));
        

    }

    private void carRent(){
        Scanner scan = new Scanner(System.in);
        String userID, callNum;
        int copyNum;
        System.out.print("Enter The User ID: ");
        userID = scan.next();
        System.out.print("Enter The Call Number: ");
        callNum = scan.next();
        System.out.print("Enter The Copy Number: ");
        copyNum = scan.nextInt();
        
        
        try{
            Statement stmt = conn.createStatement();

            //check whether the car copy exists
            String query = String.format( "SELECT * FROM copy C WHERE C.callnum='%s' AND C.copynum=%d;",callNum,copyNum);
            ResultSet rs = stmt.executeQuery(query);

            if (rs.isBeforeFirst()){
                query = String.format( "SELECT * FROM rent R WHERE R.callnum='%s' AND R.copynum=%d AND R.return_date is null;",callNum,copyNum);
                rs = stmt.executeQuery(query);
                if (!rs.isBeforeFirst()){ // the car copy is available, insert a new rent record

                    //check whether the user exceed the max # of rent cars
                    query = String.format( "SELECT * FROM user U, user_category C WHERE U.ucid = C.ucid AND U.uid = '%s' AND C.max > (SELECT COUNT(*) FROM rent R WHERE R.uid = '%s' AND R.return_date is null);", userID, userID);
                    rs = stmt.executeQuery(query);
                    if (rs.isBeforeFirst()){
                        String date = LocalDate.now().toString();
                        query = String.format( "INSERT INTO rent VALUE('%s','%s', %d, '%s', null)",userID, callNum, copyNum, date);
                        stmt.executeUpdate(query);
                        System.out.println("Car renting performed \u001B[32msuccessfully\u001B[0m.");

                        // update car rent times
                        query = String.format( "UPDATE car SET time_rent = time_rent + 1 WHERE car.callnum ='%s';",callNum);
                        stmt.executeUpdate(query);
                    } else {
                        // exceed max of the user
                        System.out.println("[\u001B[31merror\u001B[0m] Reached the maximum number of cars that the user can rent. Please return a car first.");
                    }
                }else{
                    // return_date == NULL, haven't been returned
                    System.out.println("[\u001B[31merror\u001B[0m]This car copy has been rented.");
                }

            } else {
                // no such car copy
                System.out.println("[\u001B[31merror\u001B[0m]No matching car copy found.");
            }
            //check whether the car copy is available
            
        }catch (SQLException e){
            System.out.println(e);
        }

        
    }

    private void carReturn(){
        Scanner scan = new Scanner(System.in);
        String userID, callNum;
        int copyNum;
        System.out.print("Enter The User ID: ");
        userID = scan.next();
        System.out.print("Enter The Call Number: ");
        callNum = scan.next();
        System.out.print("Enter The Copy Number: ");
        copyNum = scan.nextInt();

        try{
            Statement stmt = conn.createStatement();
            String query = String.format( "SELECT * FROM rent R WHERE R.callnum='%s' AND R.copynum=%d AND R.return_date is null;",callNum,copyNum);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.isBeforeFirst()){ // the car copy can be returned
                String date = LocalDate.now().toString();
                query = String.format( "UPDATE rent R SET return_date = '%s' WHERE R.callnum = '%s'AND R.copynum ='%d'AND R.return_date is null;",date, callNum, copyNum);
                stmt.executeUpdate(query);
                System.out.println("Car renting performed \u001B[32msuccessfully\u001B[0m.");
                
            }else{
                // no rent record, can't return
                System.out.println("[\u001B[31merror\u001B[0m] No matching rent record found.");
            }

        }catch (SQLException e){
            System.out.println(e);
        }
        
        
    }

    private void carList(){
        Scanner scan = new Scanner(System.in);
        //input start and end date and change format to yyyy-mm-dd
        String start, end;
        System.out.print("Type in the \u001B[36mstarting\u001B[0m date [dd/mm/yyyy]: ");
        start = scan.next();
        start  = String.format("%s-%s-%s", start.substring(6,10),start.substring(3,5),start.substring(0,2));
        System.out.print("Type in the \u001B[36mending\u001B[0m date [dd/mm/yyyy]: ");
        end = scan.next();
        end = String.format("%s-%s-%s", end.substring(6,10),end.substring(3,5),end.substring(0,2));
        try{
            Statement stmt = conn.createStatement();
            String query = String.format( "SELECT * FROM rent R WHERE R.return_date is null AND R.checkout <= '%s' AND R.checkout >= '%s' ORDER BY R.checkout DESC;",end, start);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.isBeforeFirst()){
                //display the records within the period (excluding start/end date)
                System.out.println("List of unreturned cars:");
                System.out.println("|UID|CallNum|CopyNum|Checkout|");
                while(rs.next()){
                    System.out.printf("|%s|%s|%d|%s|\n",rs.getString("uid"),rs.getString("callnum"),rs.getInt("copynum"),rs.getString("checkout"));
                }
                System.out.println("End of Query.");
            }else{//No records found within the period
                System.out.println("No records found within the period.");
            }
                
        }catch (SQLException e){
            System.out.println(e);
        }

    }

}
