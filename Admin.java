import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.sql.*;

// HOW TO COMPILE: rm -f *.class; javac Start.java; javac Admin.java; java -cp .:mysql-connector-java-5.1.47.jar Start
// SQL LOGIN: mysql --host=projgw --port=2633 -u Group13 -p
public class Admin {
    Connection conn = null;
    public Admin(){
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db13";
        String dbUsername = "Group13";
        String dbPassword = "group13group13";
        // Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        }catch(ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private void createAllTables(){
        try{
            Statement stmt = conn.createStatement();
            System.out.print("Processing...");
            stmt.executeUpdate(
                "CREATE TABLE user_category ("+
                    "ucid INTEGER NOT NULL, "+
                    "max INTEGER NOT NULL, "+
                    "period INTEGER NOT NULL, "+
                    "PRIMARY KEY (ucid)"+
                ")"
            );
            stmt.executeUpdate(
                "CREATE TABLE user ("+
                    "uid VARCHAR(12) NOT NULL, "+
                    "name VARCHAR(25) NOT NULL, "+
                    "age INTEGER NOT NULL, "+
                    "occupation VARCHAR(20) NOT NULL, "+
                    "ucid INTEGER NOT NULL, "+
                    "PRIMARY KEY (uid), "+
                    "FOREIGN KEY (ucid) REFERENCES user_category(ucid)"+
                ")"
            );
            stmt.executeUpdate(
                "CREATE TABLE car_category ("+
                    "ccid INTEGER NOT NULL, "+
                    "ccname VARCHAR(20) NOT NULL, "+
                    "PRIMARY KEY (ccid)"+
                ")"
            );
            stmt.executeUpdate(
                "CREATE TABLE car ("+
                    "callnum VARCHAR(8) NOT NULL, "+
                    "name VARCHAR(10) NOT NULL, "+
                    "manufacture DATE NOT NULL, "+
                    "time_rent INTEGER NOT NULL, "+
                    "ccid INTEGER NOT NULL, "+
                    "PRIMARY KEY (callnum), "+
                    "FOREIGN KEY (ccid) REFERENCES car_category(ccid)"+
                ")"
            );
            stmt.executeUpdate(
                "CREATE TABLE copy ("+
                    "callnum VARCHAR(8) NOT NULL, "+
                    "copynum INTEGER NOT NULL,"+
                    "PRIMARY KEY (callnum, copynum),"+
                    "FOREIGN KEY (callnum) REFERENCES car(callnum)"+
                ")"
            );
            stmt.executeUpdate(
                "CREATE TABLE rent ("+
                    "uid VARCHAR(12) NOT NULL, "+
                    "callnum VARCHAR(8) NOT NULL, "+
                    "copynum INTEGER NOT NULL, "+
                    "checkout DATE NOT NULL, "+
                    "return_date DATE, "+
                    "PRIMARY KEY (uid,callnum,copynum,checkout), "+
                    "FOREIGN KEY (uid) REFERENCES user(uid), "+
                    "FOREIGN KEY (callnum,copynum) REFERENCES copy(callnum,copynum)"+
                ")"
            );
            stmt.executeUpdate(
                "CREATE TABLE produce ("+
                    "cname VARCHAR(25) NOT NULL, "+
                    "callnum VARCHAR(8) NOT NULL, "+
                    "PRIMARY KEY (cname, callnum), "+
                    "FOREIGN KEY (callnum) REFERENCES car(callnum)"+
                ")"
            );
            System.out.println("Done. Database is initialized.");
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private void deleteAllTables(){
        try{
            Statement stmt = conn.createStatement();
            System.out.print("Processing...");
            stmt.executeUpdate("DROP TABLE IF EXISTS rent");
            stmt.executeUpdate("DROP TABLE IF EXISTS produce");
            stmt.executeUpdate("DROP TABLE IF EXISTS copy");
            stmt.executeUpdate("DROP TABLE IF EXISTS car");
            stmt.executeUpdate("DROP TABLE IF EXISTS car_category");
            stmt.executeUpdate("DROP TABLE IF EXISTS user");
            stmt.executeUpdate("DROP TABLE IF EXISTS user_category");
            System.out.println("Done. Database is removed.");
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private void loadFromDatafile(){
        Scanner reader = new Scanner(System.in);
        System.out.print("Type in the Source Data Folder Path: ");
        String datapath = reader.nextLine();
        System.out.println(datapath);
        File fobj = null;
        Scanner freader = null;
        try{
            System.out.print("Processing...");
            // load user_category
            fobj = new File(datapath + "/user_category.txt");
            freader = new Scanner(fobj);
            while(freader.hasNextLine()){
                PreparedStatement pstmt = conn.prepareStatement("insert into user_category values (?,?,?)");
                pstmt.setInt(1, freader.nextInt());
                pstmt.setInt(2, freader.nextInt());
                pstmt.setInt(3, freader.nextInt());
                pstmt.execute();
            }
            freader.close();
            
            // load user
            fobj = new File(datapath + "/user.txt");
            freader = new Scanner(fobj);
            while(freader.hasNextLine()){
                String[] parts = freader.nextLine().split("\t");
                String uid = parts[0];
                String name = parts[1];
                int age = Integer.parseInt(parts[2]);
                String occupation = parts[3];
                int ucid = Integer.parseInt(parts[4]);
                PreparedStatement pstmt = conn.prepareStatement("insert into user values (?,?,?,?,?)");
                pstmt.setString(1, uid);
                pstmt.setString(2, name);
                pstmt.setInt(3, age);
                pstmt.setString(4, occupation);
                pstmt.setInt(5, ucid);
                pstmt.execute();
            }
            freader.close();

            // load car_category
            fobj = new File(datapath + "/car_category.txt");
            freader = new Scanner(fobj);
            while(freader.hasNextLine()){
                String[] parts = freader.nextLine().split("\t");
                int ccid = Integer.parseInt(parts[0]);
                String ccname = parts[1];
                PreparedStatement pstmt = conn.prepareStatement("insert into car_category values (?,?)");
                pstmt.setInt(1, ccid);
                pstmt.setString(2, ccname);
                pstmt.execute();
            }
            freader.close();
            
            // load car, copy, and produce
            fobj = new File(datapath + "/car.txt");
            freader = new Scanner(fobj);
            while(freader.hasNextLine()){
                String[] parts = freader.nextLine().split("\t");
                String callnum = parts[0];
                int num_of_copies = Integer.parseInt(parts[1]);
                String carName = parts[2];
                String companyName = parts[3];
                Date manufacture = Date.valueOf(parts[4]);
                int time_rent = Integer.parseInt(parts[5]);
                int ccid = Integer.parseInt(parts[6]);
                PreparedStatement pstmt = conn.prepareStatement("insert into car values (?,?,?,?,?)");
                pstmt.setString(1, callnum);
                pstmt.setString(2, carName);
                pstmt.setDate(3, manufacture);
                pstmt.setInt(4, time_rent);
                pstmt.setInt(5, ccid);
                pstmt.execute();
                PreparedStatement qstmt = conn.prepareStatement("insert into produce values (?,?)");
                qstmt.setString(1, companyName);
                qstmt.setString(2, callnum);
                qstmt.execute();
                for(int i = 1; i <= num_of_copies; i++){
                    PreparedStatement rstmt = conn.prepareStatement("insert into copy values (?,?)");
                    rstmt.setString(1, callnum);
                    rstmt.setInt(2, i);
                    rstmt.execute();
                }
            }
            freader.close();

            // load rent
            fobj = new File(datapath + "/rent.txt");
            freader = new Scanner(fobj);
            while(freader.hasNextLine()){
                String[] parts = freader.nextLine().split("\t");
                String callnum = parts[0];
                int copynum = Integer.parseInt(parts[1]);
                String uid = parts[2];
                Date checkout_d = Date.valueOf(parts[3]);
		        Date return_d = null;
                if(!parts[4].equals("NULL")) return_d = Date.valueOf(parts[4]);
                PreparedStatement pstmt = conn.prepareStatement("insert into rent values (?,?,?,?,?)");
                pstmt.setString(1, uid);
                pstmt.setString(2, callnum);
                pstmt.setInt(3, copynum);
                pstmt.setDate(4, checkout_d);
                pstmt.setDate(5, return_d);
                pstmt.execute();
            }
            freader.close();
            System.out.println("Done. Data is input to the database.");
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private void showNumOfRecords(){
        try{
            String[] dbnames = {"user_category","user","car_category","car","copy","rent","produce"};
            Statement stmt = conn.createStatement();
            System.out.println("Number of records in each table:");
            for(String dbname : dbnames){
                ResultSet resultSet = stmt.executeQuery("select count(*) as \"num_rows\" from " + dbname);
		resultSet.first();
                int num_rows = resultSet.getInt("num_rows");
                System.out.println(dbname + ": " + num_rows);
            }
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    public void run(){
        Scanner reader = new Scanner(System.in);
        String choice;
        do{
            System.out.println("-----Operations for administrator menu-----\nWhat kind of operation would you like to perform?");
            System.out.println("1. Create all tables");
            System.out.println("2. Delete all tables");
            System.out.println("3. Load from datafile");
            System.out.println("4. Show number of records in each table");
            System.out.println("5. Return to the main menu");
            System.out.print("Enter Your Choice: ");
            choice = reader.nextLine();
                if(choice.equals("1")) createAllTables();
                else if(choice.equals("2")) deleteAllTables();
                else if(choice.equals("3")) loadFromDatafile();
                else if(choice.equals("4")) showNumOfRecords();
                else if (!choice.equals("5")) System.out.println("[Error]: Input must be an integer from 1 to 5");
        }while(!choice.equals("5"));
    }
}
