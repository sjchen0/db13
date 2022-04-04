import java.util.Scanner;
public class Start {
    public static void main(String[] args){
        Scanner reader = new Scanner(System.in);
        String choice;
        do{
            System.out.println("Welcome to Car Renting System!\n\n-----Main menu-----");
            System.out.println("What kinds of operations would you like to perform?");
            System.out.println("1. Operations for Administrator");
            System.out.println("2. Operations for User");
            System.out.println("3. Operations for Manager");
            System.out.println("4. Exit this program");
            System.out.print("Enter Your Choice: ");
            choice = reader.nextLine();
            if(choice.equals("1")){Admin admin = new Admin(); admin.run();}
            else if(choice.equals("2")){User user = new User(); user.run();}
            else if(choice.equals("3")){}
            else if (!choice.equals("4")) System.out.println("[Error]: Input must be an integer from 1 to 4");
        }while(!choice.equals("4"));
    }
}