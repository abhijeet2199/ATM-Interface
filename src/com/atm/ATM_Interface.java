package com.atm;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class User {
    private int pin;
    private float balance;
    private List<String> transactionHistory;

    public User(int pin, float balance) {
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public int getPin() {
        return pin;
    }

    public float getBalance() {
        return balance;
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void addToTransactionHistory(String transaction) {
        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        transactionHistory.add(formattedDateTime + " - " + transaction);
    }

    public void deposit(float amount) {
        balance += amount;
    }

    public boolean withdraw(float amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

class Atm {
    private Scanner sc = new Scanner(System.in);
    private Map<Integer, User> users;

    public Atm() {
        users = new HashMap<>();
        
        users.put(1234, new User(1234, 50000));
        users.put(5678, new User(5678, 70000)); 
    }

    public void checkPin() {
        System.out.println("Enter your pin");
        int enteredPin = sc.nextInt();
        User user = users.getOrDefault(enteredPin, null);
        if (user != null) {
            menu(user);
        } else {
            System.err.println("Invalid PIN Try again");
            checkPin();
        }
    }

    public void menu(User user) {
        System.out.println("*********************************************************************************");
        System.err.println("1) Check A/C Balance 2) Cash Withdrawal 3) Cash Deposit 4) Transfer Money 5) Show Transaction History 6) Exit");
        System.err.println("Enter Your Choice");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                checkBalance(user);
                break;
            case 2:
                cashWithdrawal(user);
                break;
            case 3:
                cashDeposit(user);
                break;
            case 4:
                transferMoney(user);
                break;
            case 5:
                transaction(user);
                break;
            case 6:
                exit();
                break;
            default:
                System.err.println("Invalid Choice!");
                menu(user);
        }
    }

    public void checkBalance(User user) {
        System.out.println("Balance of your account is ₹" + user.getBalance());
        returnBack(user);
    }

    public void cashWithdrawal(User user) {
        System.out.println("Enter amount you want to withdraw");
        float withdrawAmount = sc.nextFloat();
        if (user.withdraw(withdrawAmount)) {
            System.out.println("Balance withdrawal successful");
            user.addToTransactionHistory("Cash Withdrawal done successfully - " + withdrawAmount);
        } else {
            System.out.println("You don't have sufficient balance");
        }
        returnBack(user);
    }

    public void cashDeposit(User user) {
        System.out.println("Enter amount you want to deposit");
        float depositAmount = sc.nextFloat();
        user.deposit(depositAmount);
        System.out.println("Cash Deposited successfully");
        user.addToTransactionHistory("Cash Deposited successfully - " + depositAmount);
        returnBack(user);
    }

    public void transferMoney(User user) {
        System.out.println("Enter recipient's account PIN:");
        int recipientPin = sc.nextInt();
        User recipient = users.getOrDefault(recipientPin, null);
        if (recipient != null) {
            System.out.println("Enter amount to transfer:");
            float amount = sc.nextFloat();
            if (user.withdraw(amount)) {
                recipient.deposit(amount);
                user.addToTransactionHistory("Transferred ₹" + amount + " to account " + recipientPin);
                recipient.addToTransactionHistory("Received ₹" + amount + " from account " + user.getPin());
                System.out.println("Transfer successful");
            } else {
                System.out.println("Insufficient balance for transfer");
            }
        } else {
            System.out.println("Recipient account not found");
        }
        returnBack(user);
    }

    public void transaction(User user) {
        System.out.println("Transaction History:");
        for (String transaction : user.getTransactionHistory()) {
            System.out.println(transaction);
        }
        System.out.println("                              Total Balance: ₹" + user.getBalance());
        returnBack(user);
    }

    public void exit() {
        System.out.println("Thank you! Visit again.");
        return;
    }

    public void returnBack(User user) {
        System.out.println("If you want to operate again then press 1, else press 0 to exit:");
        int opt = sc.nextInt();
        if (opt == 1) {
            menu(user);
        } else if (opt == 0) {
            exit();
        } else {
            System.err.println("Enter Valid Choice");
            returnBack(user);
        }
    }
}

public class ATM_Interface {
    public static void main(String[] args) {
        Atm atm = new Atm();
        atm.checkPin();
    }
}
