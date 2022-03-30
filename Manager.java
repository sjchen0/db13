import java.util.*;
import java.sql.*;
import java.time.LocalDate;



public class Manager {
    static Scanner scan = new Scanner(System.in);
    static void run(Statement stmt){
        
        int choice;
        do{
            System.out.print("\n-----Operations for manager menu-----\n");
            System.out.println("What kinds of operations would you like to perform?");
            System.out.println("1. Car Renting");
            System.out.println("2. Car Returning");
            System.out.println("3. List all un-returned car copies which are checked-out within a period");
            System.out.println("4. Return to the main menu");
            System.out.println("Enter your choice:");
            choice = scan.nextInt();

            if (choice == 1)
                carRent(stmt);
            else if (choice == 2)
                carReturn(stmt);
            else if (choice == 3)
                carList(stmt);

        }while(choice != 4);
        

    }

    static void carRent(Statement stmt){
        String userID, callNum;
        int copyNum;
        System.out.println("Enter The User ID:");
        userID = scan.next();
        System.out.println("Enter The Call Nummber:");
        callNum = scan.next();
        System.out.println("Enter The Copy Number:");
        copyNum = scan.nextInt();
        
        
        try{
            //check whether the car copy is available
            String query = String.format( "SELECT * FROM Rent R WHERE R.callnum='%s' and R.copynum=%d and R.return_date is null;",callNum,copyNum);
            ResultSet rs = stmt.executeQuery(query);

            if (!rs.isBeforeFirst()){ // the car copy is available, insert a new rent record
                String date = LocalDate.now().toString();
                query = String.format( "INSERT INTO Rent VALUE('%s','%s', %d, '%s', null)",userID, callNum, copyNum, date);
                stmt.executeUpdate(query);
                System.out.println("Car renting performed \u001B[32msuccessfully\u001B[0m.");

                // update car rent times
                query = String.format( "UPDATE Car SET time_rent = time_rent + 1 WHERE Car.callnum ='%s';",callNum);
                stmt.executeUpdate(query);
            }else{
                // return_date == NULL, haven't been returned
                System.out.println("[\u001B[31merror\u001B[0m]This car copy has been rented.");
            }
        }catch (SQLException e){
            System.out.println(e);
        }

        
    }

    static void carReturn(Statement stmt){
        String userID, callNum;
        int copyNum;
        System.out.println("Enter The User ID:");
        userID = scan.next();
        System.out.println("Enter The Call Nummber:");
        callNum = scan.next();
        System.out.println("Enter The Copy Number:");
        copyNum = scan.nextInt();

        try{
            String query = String.format( "SELECT * FROM Rent R WHERE R.callnum='%s' and R.copynum=%d and R.return_date is null;",callNum,copyNum);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.isBeforeFirst()){ // the car copy can be returned
                String date = LocalDate.now().toString();
                query = String.format( "UPDATE Rent R SET return_date = '%s' WHERE R.callnum = '%s'and R.copynum ='%d'and R.return_date is null;",date, callNum, copyNum);
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

    static void carList(Statement stmt){
        //input start and end date and change format to yyyy-mm-dd
        String start, end;
        System.out.println("Type in the \u001B[34mstarting\u001B[0m date [dd/mm/yyyy]:");
        start = scan.next();
        start  = String.format("%s-%s-%s", start.substring(6,10),start.substring(3,5),start.substring(0,2));
        System.out.println("Type in the \u001B[34mending\u001B[0m date [dd/mm/yyyy]:");
        end = scan.next();
        end = String.format("%s-%s-%s", end.substring(6,10),end.substring(3,5),end.substring(0,2));
        try{
            String query = String.format( "SELECT * FROM Rent R WHERE R.return_date is null and R.checkout < '%s' and R.checkout > '%s' ORDER BY R.checkout DESC;",end, start);
            ResultSet rs = stmt.executeQuery(query);
            if (rs.isBeforeFirst()){
                //display the records within the period
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
