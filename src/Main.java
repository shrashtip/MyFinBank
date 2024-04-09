import in.myfin.exception.CustomException;
import in.myfin.model.Account;
import in.myfin.model.Transaction;
import in.myfin.model.User;
import in.myfin.service.*;

import java.util.List;
import java.util.Scanner;



public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserServiceImpl();
    private static final AccountService accountService = new AccountServiceImpl();
    private static final TransactionService transactionService = new TransactionServiceImpl();
    private static User loggedInUser;

    public static void main(String[] args) {
        displayMainMenu();
    }

    private static void displayMainMenu() {
        while (true) {
            System.out.println("\n===== Bank Application Menu =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    System.out.println("Thank you for using the Bank Application. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        }
    }

    private static void loginUser() {
        System.out.println("\n===== User Login =====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            loggedInUser = userService.loginUser(username, password);
            System.out.println("Login successful. Welcome, " + loggedInUser.getUsername() + "!");
            displayUserMenu();
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayUserMenu() {
        if (loggedInUser.getUserType().equalsIgnoreCase("customer")) {
            displayCustomerMenu();
        } else if (loggedInUser.getUserType().equalsIgnoreCase("admin")) {
            displayAdminMenu();
        }
    }

    private static void displayCustomerMenu() {
        while (true) {
            System.out.println("\n===== Customer Menu =====");
            System.out.println("1. View Accounts");
            System.out.println("2. Create Account");
            System.out.println("3. View Transactions");
            System.out.println("4. Check Balance");
            System.out.println("5. Transfer Money");
            System.out.println("6. Apply for Loan");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewAccounts();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    viewTransactions();
                    break;
                case 4:
                    checkBalance();
                    break;
                case 5:
                    transferAmount();
                    break;
                case 6:
                    applyForLoan();
                    break;
                case 7:
                    logoutUser();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }




    private static void registerUser () {
        System.out.println("\n===== User Registration =====");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter user type (Customer/Admin): ");
        String userType = scanner.nextLine();

        try {
            User newUser = new User(username, password, userType);
            userService.registerUser(newUser);
            System.out.println("User registered successfully.");
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void viewAccounts() {
        System.out.println("\n===== User Accounts =====");
        try {
            List<Account> userAccounts = accountService.getAccountsByUserId(loggedInUser.getUserId());
            if (userAccounts.isEmpty()) {
                System.out.println("No accounts found for the user.");
            } else {
                for (Account account : userAccounts) {
                    System.out.println("Account ID: " + account.getAccountId() +
                            ", Type: " + account.getAccountType() +
                            ", Balance: " + account.getBalance());
                }
            }
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void transferAmount() {
        System.out.println("\n===== Transfer Money =====");

        try {
            // Get all available accounts for the user
            List<Account> userAccounts = accountService.getAccountsByUserId(loggedInUser.getUserId());

            // Display available accounts
            if (userAccounts.isEmpty()) {
                System.out.println("No accounts found for the user.");
                return;
            }

            System.out.println("Available Accounts:");
            for (Account account : userAccounts) {
                // Fetch username for the account
                String username = userService.getUserById(account.getUserId()).getUsername();

                System.out.println("Account ID: " + account.getAccountId() +
                        ", Type: " + account.getAccountType() +
                        ", Balance: " + account.getBalance() +
                        ", Username: " + username);
            }

            // Prompt for recipient account ID
            System.out.print("Enter recipient account ID: ");
            int recipientAccountId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Validate recipient account
            Account recipientAccount = accountService.getAccountById(recipientAccountId);
            if (recipientAccount == null) {
                System.out.println("Invalid recipient account ID.");
                return;
            }

            // Prompt for transfer amount
            System.out.print("Enter amount to transfer: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            // Get sender account
            Account senderAccount = userAccounts.stream()
                    .filter(account -> account.getBalance() >= amount)
                    .findFirst()
                    .orElse(null);

            if (senderAccount == null) {
                System.out.println("You don't have sufficient balance in any account to transfer.");
                return;
            }

            // Perform fund transfer
            senderAccount.setBalance(senderAccount.getBalance() - amount);
            recipientAccount.setBalance(recipientAccount.getBalance() + amount);

            accountService.updateAccount(senderAccount);
            accountService.updateAccount(recipientAccount);

            System.out.println("Amount transferred successfully from Account ID: " +
                    senderAccount.getAccountId() + " to Account ID: " + recipientAccount.getAccountId());
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void checkBalance() {
        System.out.println("\n===== Check Balance =====");

        try {
            // Get accounts associated with the logged-in user
            List<Account> userAccounts = accountService.getAccountsByUserId(loggedInUser.getUserId());

            // Display user's accounts
            if (userAccounts.isEmpty()) {
                System.out.println("No accounts found for the user.");
                return;
            }

            System.out.println("Your Accounts:");
            for (Account account : userAccounts) {
                System.out.println("Account ID: " + account.getAccountId() +
                        ", Type: " + account.getAccountType() +
                        ", Balance: " + account.getBalance());
            }

            // Prompt for account ID
            System.out.print("Enter account ID: ");
            int accountId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Check if the entered account ID exists in the user's accounts
            Account selectedAccount = userAccounts.stream()
                    .filter(account -> account.getAccountId() == accountId)
                    .findFirst()
                    .orElse(null);

            if (selectedAccount == null) {
                System.out.println("Invalid account ID or account doesn't belong to you.");
                return;
            }

            // Display balance of the selected account
            System.out.println("Current Balance: " + selectedAccount.getBalance());

        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void logoutUser() {
        try {
            userService.logoutUser(loggedInUser);
            System.out.println("Logout successful. Goodbye, " + loggedInUser.getUsername() + "!");
            loggedInUser = null;
            displayMainMenu();
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void applyForLoan() {
        System.out.println("\n===== Apply for Loan =====");
        // Implement loan application functionality
    }

    private static void createAccount() {
        System.out.println("\n===== Create Account =====");

        // Prompt for account type
        System.out.println("Select account type:");
        System.out.println("1. Savings");
        System.out.println("2. Current");
        System.out.print("Enter your choice: ");
        int accountTypeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String accountType;
        switch (accountTypeChoice) {
            case 1:
                accountType = "Savings";
                break;
            case 2:
                accountType = "Current";
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Savings account.");
                accountType = "Savings";
        }

        double initialBalance = 1000; // Default balance

        try {
            // Create new account with provided account type and default balance
            Account newAccount = new Account(loggedInUser.getUserId(), accountType, initialBalance);
            accountService.createAccount(newAccount, loggedInUser.getUsername());

            System.out.println("Account created successfully.");
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void viewTransactions() {
        System.out.println("\n===== View Transactions =====");

        // Get all accounts related to the logged-in user
        try {
            List<Account> userAccounts = accountService.getAccountsByUserId(loggedInUser.getUserId());

            // Display available accounts
            if (userAccounts.isEmpty()) {
                System.out.println("No accounts found for the user.");
                return;
            }

            System.out.println("Available Accounts:");
            for (Account account : userAccounts) {
                System.out.println("Account ID: " + account.getAccountId() +
                        ", Type: " + account.getAccountType() +
                        ", Balance: " + account.getBalance());
            }

            // Prompt for account ID
            System.out.print("Enter account ID to view transactions: ");
            int accountId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Check if the entered account ID belongs to the user
            if (userAccounts.stream().noneMatch(account -> account.getAccountId() == accountId)) {
                System.out.println("Invalid account ID. You don't have permission to view transactions for this account.");
                return;
            }

            // Get transactions for the selected account
            try {
                List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);

                // Display transactions
                if (transactions.isEmpty()) {
                    System.out.println("No transactions found for the account.");
                } else {
                    System.out.println("Transactions for Account ID " + accountId + ":");
                    for (Transaction transaction : transactions) {
                        System.out.println("Transaction ID: " + transaction.getTransactionId() +
                                ", Amount: " + transaction.getAmount());
                    }
                }
            } catch (CustomException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }



//-----------------Admin functionality---------------------------------------------

    private static void displayAdminMenu() {
        while (true) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. View All Customers");
            System.out.println("2. Add New Customer");
            System.out.println("3. Delete Customer");
            System.out.println("4. Update Customer");
            System.out.println("5. View All Accounts");
            System.out.println("6. Add Account to Customer");
            System.out.println("7. Delete Account from Customer");
            System.out.println("8. View All Accounts with Customers");
            System.out.println("9. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    updateUser();
                    break;
                case 5:
                    viewAllAccounts();
                    break;
                case 6:
                    addAccountToCustomer();
                    break;
                case 7:
                    deleteAccountFromCustomer();
                    break;
                case 8:
                    displayAllAccountsWithCustomers();
                    break;
                case 9:
                    logoutUser();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 9.");
            }
        }
    }

    private static void deleteAccountFromCustomer() {
        try {
            System.out.println("===== Delete Account from Customer =====");
            // Display all customers
            viewAllUsers();

            // Prompt for customer ID
            System.out.print("Enter customer ID: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Get customer by ID
            User customer = userService.getUserById(customerId);
            if (customer == null) {
                System.out.println("Customer with ID " + customerId + " not found.");
                return;
            }

            // Display customer's accounts
            List<Account> accounts = accountService.getAccountsByUserId(customerId);
            if (accounts.isEmpty()) {
                System.out.println("No accounts found for this customer.");
                return;
            }

            System.out.println("Customer's Accounts:");
            for (Account account : accounts) {
                System.out.println("Account ID: " + account.getAccountId() +
                        ", Type: " + account.getAccountType() +
                        ", Balance: " + account.getBalance());
            }

            // Prompt for account ID to delete
            System.out.print("Enter account ID to delete: ");
            int accountId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Check if the account belongs to the customer
            Account accountToDelete = accounts.stream()
                    .filter(account -> account.getAccountId() == accountId)
                    .findFirst()
                    .orElse(null);

            if (accountToDelete == null) {
                System.out.println("Account with ID " + accountId + " not found for this customer.");
                return;
            }

            // Delete account
            accountService.deleteAccount(accountId);
            System.out.println("Account deleted successfully.");
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void updateUser() {

        try {
            viewAllUsers();
            System.out.println("Enter the id to update the user");

            User user=userService.getUserById(scanner.nextInt());
            System.out.println("Customer deleted successfully!");
        } catch (CustomException e) {
            System.out.println("Unable to delete the customer "+e.getMessage());

        }
    }

    private static void addUser() {

        registerUser();
    }

    private static void deleteUser() {
        viewAllUsers();
        System.out.println("Enter the user id in order to delete the user");
        int id =scanner.nextInt();
        try {
            userService.deleteUser(id);
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());        }
    }

    private static void viewAllUsers() {
        try {
            List<User> users= userService.getAllUsers();
            for(User user : users){
                System.out.println(user);
            }
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private static void addAccountToCustomer() {
        try {
            System.out.println("===== Add Account to Customer =====");
            // Display all customers
            viewAllUsers();

            // Prompt for customer ID
            System.out.print("Enter customer ID: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Get customer by ID
            User customer = userService.getUserById(customerId);
            if (customer == null) {
                System.out.println("Customer with ID " + customerId + " not found.");
                return;
            }

            // Display customer's accounts
            List<Account> accounts = accountService.getAccountsByUserId(customerId);
            if (accounts.isEmpty()) {
                System.out.println("No accounts found for this customer.");
                return;
            }

            System.out.println("Customer's Accounts:");
            for (Account account : accounts) {
                System.out.println("Account ID: " + account.getAccountId() +
                        ", Type: " + account.getAccountType() +
                        ", Balance: " + account.getBalance());
            }

            // Prompt for account details
            System.out.println("Enter account details:");
            System.out.print("Account type (Savings/Current): ");
            String accountType = scanner.nextLine();
            System.out.print("Initial balance: ");
            double initialBalance = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            // Create account
            Account newAccount = new Account(customerId, accountType, initialBalance);
            accountService.createAccount(newAccount, customer.getUsername());
            System.out.println("Account added successfully.");
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayAllAccountsWithCustomers() {
        try {
            List<User> customers = userService.getAllCustomers();
            if (customers.isEmpty()) {
                System.out.println("No customers found.");
            } else {
                System.out.println("\n===== All Accounts with Customers =====");
                for (User customer : customers) {
                    System.out.println("Customer ID: " + customer.getUserId() + ", Username: " + customer.getUsername());
                    List<Account> accounts = accountService.getAccountsByUserId(customer.getUserId());
                    if (accounts.isEmpty()) {
                        System.out.println("No accounts found for this customer.");
                    } else {
                        for (Account account : accounts) {
                            System.out.println("    Account ID: " + account.getAccountId() +
                                    ", Type: " + account.getAccountType() +
                                    ", Balance: " + account.getBalance());
                        }
                    }
                    System.out.println(); // Empty line for separation
                }
            }
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void viewAllAccounts() {
        try {
            List<User> allCustomers = userService.getAllUsers(); // Retrieve all customers

            if (allCustomers.isEmpty()) {
                System.out.println("No customers found.");
            } else {
                System.out.println("\n===== All Accounts =====");
                for (User customer : allCustomers) {
                    System.out.println("Customer ID: " + customer.getUserId() +
                            ", Name: " + customer.getUsername());

                    List<Account> customerAccounts = accountService.getAccountsByUserId(customer.getUserId());
                    if (customerAccounts.isEmpty()) {
                        System.out.println("\tNo accounts found for this customer.");
                    } else {
                        for (Account account : customerAccounts) {
                            System.out.println("\tAccount ID: " + account.getAccountId() +
                                    ", Type: " + account.getAccountType() +
                                    ", Balance: " + account.getBalance());
                        }
                    }
                }
            }
        } catch (CustomException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}