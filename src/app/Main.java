package app;

import java.util.Scanner;

import domain.Account;
import services.BankService;
import services.impl.BankServiceImpl;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Boolean running = true;
        BankService bankService = new BankServiceImpl();
        System.out.println("<-----Welcome TO Console BAnk---->");
        while (running) {
            System.out.println("""
                
                1) Open Account
                2) Deposit
                3) Withdraw
                4) Transfer
                5) Account Statement
                6) List Accounts
                7) Search Account By User Name
                0) Exit
                
                """);
                System.out.println("Select The Option");
                String select = input.nextLine().trim();
                System.out.println("Selected :" +select);


                switch (select) {
                                              
                    
                    case "1" : openAccount(input,bankService);
                    break;
                    case "2" : deposite(input,bankService);
                    break;
                    case "3" : withdraw(input,bankService);
                    break;
                    case "4" : transfer(input,bankService);
                    break;
                    case "5" : accountStatement(input,bankService);
                    break;
                    case "6" : listAccounts(input,bankService);
                    break;
                    case "7" : searchAccount(input,bankService);
                    break;
                    case "0" : running = false; 

                    default:
                        break;
                }
        }
        
        
}
    private static void openAccount(Scanner input,BankService bankService) {
        System.out.println("Enter Name");
        String name = input.nextLine().trim();
        System.out.println("Enter Email");
        String email = input.nextLine().trim();
        System.out.println("Enter Account Type Current,Savings");
        String accountType = input.nextLine().trim();
        System.out.println("Enter Minimum Balance (0 for O minimum balance) ");
        String initialStr = input.nextLine().trim();
        Double initial = initialStr.isEmpty() ? 0.0 : Double.valueOf(initialStr);
        String accountNumber = bankService.openAccount(name,email,accountType,initial);
        System.out.println("<--------------------------------------------------------------------------------------------------------------->");
        System.out.println("Account Created With Account Number :"+accountNumber+"   |   Name : "+name+"   |   Type : "+accountType+"   |   Balance : "+initial);
        System.out.println("<================================================================================================================>");
    }
        

    private static void deposite(Scanner input,BankService bankService) {
        System.out.println("Enter Account Number");
        String accountNumber = input.nextLine().trim();
        System.out.println("Enter The Amount To be Deposited");
        Double amount = Double.valueOf(input.nextLine().trim());
        Account account = bankService.deposit(accountNumber,amount,"Deposite");
        System.out.println("<--------------------------------------------------------------------------------------------------------------->");
        System.out.println("""
            Deposited Amount : %.3f 
            
            Details : %s | %.3f
        """.formatted(
            amount,
            account.getAccountNumber(),
            account.getBalance()
        ));
         System.out.println("<================================================================================================================>");

        
    }

    private static void withdraw(Scanner input,BankService bankService) {
        System.out.println("Enter Account Number");
        String accountNumber = input.nextLine().trim();
        System.out.println("Enter The Amount To be Withdrawn");
        Double amount = Double.valueOf(input.nextLine().trim());
        System.out.println("Enter Note");
        String note = input.nextLine().trim();
        Account account = bankService.withdraw(accountNumber,amount,"Withdrawal ("+note+")");
        System.out.println("<--------------------------------------------------------------------------------------------------------------->");
        System.out.println("""
            Withdrawn Amount : %.5f 
            
            Details : %s | %.5f
        """.formatted(
            amount,
            account.getAccountNumber(),
            account.getBalance()
        ));
        System.out.println("<===================================================================================================================>");
    
    }

    private static void transfer(Scanner input,BankService bankService) {
        System.out.println("Enter Senders Account Number");
        String accountNumber = input.nextLine().trim();
        System.out.println("Enter Recipent Account number");
        String RecipentAccountNumber = input.nextLine().trim();
        System.out.println("Enter Amount To Be Sent");
        Double amount = Double.valueOf(input.nextLine().trim());
        Account account = bankService.transfer(accountNumber,RecipentAccountNumber,amount);
        System.out.println("<--------------------------------------------------------------------------------------------------------------->");
        System.out.println("""
            Transfered  %.5f From %s To %s
            
            Details : %s | %.5f
        """.formatted(
            amount,
            account.getAccountNumber(),
            RecipentAccountNumber,
            account.getAccountNumber(),
            account.getBalance()
        ));
        System.out.println("<=====================================================================================================================>");
    }

    private static void accountStatement(Scanner input,BankService bankService) {
        System.out.println("Enter Account NUmber");
        String accountnumber = input.nextLine().trim();
        System.out.println("<--------------------------------------------------------------------------------------------------------------->");
        System.out.println("///////////Account Statement//////////// " );
        System.out.println("Name : "+bankService.getNameByID(bankService.findByAccountNumber(accountnumber).getCustomerID())+"   |    Account NUmber : "+accountnumber);
        System.out.println();
        bankService.getStatement(accountnumber).forEach(t ->{       
            System.out.println(t.getAmount()+"   |   "+t.getNote()+"   |   "+t.getTimestamp());
        });
        System.out.println("<=====================================================================================================================>");
    }

    private static void listAccounts(Scanner input,BankService bankService) {
        System.out.println("<--------------------------------------------------------------------------------------------------------------->");
        bankService.listAccounts().forEach(a -> {    
             System.out.println(bankService.getNameByID(a.getCustomerID())+"   |   "+a.getAccountNumber()+"   |   "+a.getAccountType()+"   |   "+a.getBalance() );
        });
            System.out.println("<=====================================================================================================================>");

    }

    private static void searchAccount(Scanner input,BankService bankService) {
        System.out.println("Enter The Full Name or Part Of It That You Want To Search");
        String query = input.nextLine().trim();
        System.out.println("<--------------------------------------------------------------------------------------------------------------->");
        bankService.searchAccountByCustomerName(query)
        .forEach(account -> System.out.println(bankService.getNameByID(account.getCustomerID())+"   |   "+account.getAccountNumber()+"   |   "+account.getCustomerID()+"   |   "+account.getAccountType()+"   |   "+account.getBalance()));
        System.out.println("<=====================================================================================================================>");

    }
}

