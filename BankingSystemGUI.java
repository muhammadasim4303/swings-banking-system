package com.mycompany.bankingsystemgui;
/*
 *
 * @author Muhammad Asim
 */
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class BankingSystemGUI {

    private static final String USERS_FILE = "users.txt";
    private static final String ADMIN_FILE = "admin.txt";
    private static final String COMPLAINTS_FILE = "complaints.txt";
    private static final String TRANSACTIONS_FILE = "transactions.txt";

    public static void main(String[] args) {
        initializeFiles();
        showMainFrame();
    }

    private static void initializeFiles() {
        try {
            File usersFile = new File(USERS_FILE);
            if (!usersFile.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(usersFile))) {
                    writer.write("asim,123,1000.0,false\n");
                }
            }

            File adminFile = new File(ADMIN_FILE);
            if (!adminFile.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(adminFile))) {
                    writer.write("admin,123\n");
                }
            }

            File complaintsFile = new File(COMPLAINTS_FILE);
            if (!complaintsFile.exists()) {
                complaintsFile.createNewFile();
            }

            File transactionsFile = new File(TRANSACTIONS_FILE);
            if (!transactionsFile.exists()) {
                transactionsFile.createNewFile();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error initializing files: " + e.getMessage());
        }
    }

    private static void showMainFrame() {
        JFrame mainFrame = new JFrame("Banking System");
        mainFrame.setSize(400, 300);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.getContentPane().setBackground(new Color(33, 53, 85)); // Air Force Blue

        JLabel header = new JLabel("Welcome to the Banking System");
        header.setBounds(50, 20, 300, 30);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setForeground(Color.getHSBColor(25, 16, 85));
        header.setFont(new Font("Arial", Font.BOLD, 18));

        mainFrame.add(header);

        JButton adminBtn = createStyledButton("Admin View");
        adminBtn.setBounds(125, 90, 150, 30);
        adminBtn.addActionListener(e -> showLoginWindow(true));

        JButton customerBtn = createStyledButton("Customer View");
        customerBtn.setBounds(125, 130, 150, 30);
        customerBtn.addActionListener(e -> showLoginWindow(false));

        mainFrame.add(adminBtn);
        mainFrame.add(customerBtn);

        mainFrame.setVisible(true);
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        Color defaultColor = new Color(216, 196, 182); // Default color
        Color hoverColor = new Color(100, 149, 237);  // Hover color
        addHoverEffect(button, defaultColor, hoverColor);
        button.setForeground(Color.BLACK);
        return button;
    }

    private static void addHoverEffect(JButton button, Color defaultColor, Color hoverColor) {
        button.setBackground(defaultColor);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(defaultColor);
            }
        });
    }

    private static void showLoginWindow(boolean isAdminLogin) {
        JFrame loginFrame = new JFrame(isAdminLogin ? "Admin Login" : "Customer Login");
        loginFrame.setSize(400, 300);
        loginFrame.setLayout(null);
        loginFrame.getContentPane().setBackground(new Color(33, 53, 85)); // Air Force Blue

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 25);
        usernameLabel.setForeground(Color.WHITE);
        loginFrame.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 50, 150, 25);
        usernameField.setBackground(Color.getHSBColor(25, 16, 85));
        usernameField.setForeground(Color.BLACK);
        loginFrame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 90, 100, 25);
        passwordLabel.setForeground(Color.WHITE);
        loginFrame.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 150, 25);
        passwordField.setBackground(Color.getHSBColor(25, 16, 85));
        passwordField.setForeground(Color.BLACK);
        loginFrame.add(passwordField);

        JButton loginBtn = createStyledButton("Login");
        loginBtn.setBounds(200, 130, 100, 30);
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (isAdminLogin) {
                if (authenticateAdmin(username, password)) {
                    showAdminMenu();
                    loginFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid admin credentials!");
                }
            } else {
                User user = authenticateUser(username, password);
                if (user != null) {
                    showCustomerMenu(user);
                    loginFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid customer credentials!");
                }
            }
        });
        loginFrame.add(loginBtn);

        loginFrame.setVisible(true);
    }

    private static boolean authenticateAdmin(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading admin file: " + e.getMessage());
        }
        return false;
    }

    private static User authenticateUser(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username) && parts[1].equals(password)) {
                    return new User(parts[0], parts[1], Double.parseDouble(parts[2]), Boolean.parseBoolean(parts[3]));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading users file: " + e.getMessage());
        }
        return null;
    }

    private static void showAdminMenu() {
        JFrame adminFrame = new JFrame("Admin Menu");
        adminFrame.setSize(400, 300);
        adminFrame.setLayout(null);
        adminFrame.getContentPane().setBackground(new Color(33, 53, 85));

        JButton addUserBtn = createStyledButton("Add User");
        addUserBtn.setBounds(125, 50, 150, 30);
        addUserBtn.addActionListener(e -> showAddUserWindow());
        adminFrame.add(addUserBtn);

        JButton viewComplaintsBtn = createStyledButton("Manage Complaints");
        viewComplaintsBtn.setBounds(125, 90, 150, 30);
        viewComplaintsBtn.addActionListener(e -> manageComplaints());
        adminFrame.add(viewComplaintsBtn);

        JButton blockUserBtn = createStyledButton("Block/Unblock User");
        blockUserBtn.setBounds(125, 130, 150, 30);
        blockUserBtn.addActionListener(e -> blockUnblockUser());
        adminFrame.add(blockUserBtn);

        JButton backBtn = createStyledButton("Back");
        backBtn.setBounds(125, 170, 150, 30);
        backBtn.addActionListener(e -> adminFrame.dispose());
        adminFrame.add(backBtn);

        adminFrame.setVisible(true);
    }

    private static void showAddUserWindow() {
        JFrame addUserFrame = new JFrame("Add User");
        addUserFrame.setSize(400, 300);
        addUserFrame.setLayout(null);
        addUserFrame.getContentPane().setBackground(new Color(33, 53, 85));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 50, 100, 25);
        usernameLabel.setForeground(Color.WHITE);
        addUserFrame.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(150, 50, 150, 25);
        usernameField.setBackground(new Color(255, 255, 255)); // Cerulean Blue
        usernameField.setForeground(Color.BLACK);
        addUserFrame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 90, 100, 25);
        passwordLabel.setForeground(Color.WHITE);
        addUserFrame.add(passwordLabel);

        JTextField passwordField = new JTextField();
        passwordField.setBounds(150, 90, 150, 25);
        passwordField.setBackground(new Color(255, 255, 255)); // Cerulean Blue
        passwordField.setForeground(Color.BLACK);
        addUserFrame.add(passwordField);

        JButton createBtn = createStyledButton("Create");
        createBtn.setBounds(200, 130, 100, 30);
        createBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (!username.isEmpty() && !password.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
                    writer.write(username + "," + password + ",0.0,false\n");
                    JOptionPane.showMessageDialog(addUserFrame, "User added successfully!");
                    addUserFrame.dispose();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(addUserFrame, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(addUserFrame, "Please fill in all fields.");
            }
        });
        addUserFrame.add(createBtn);

        addUserFrame.setVisible(true);
    }

    private static void manageComplaints() {
        JFrame complaintsFrame = new JFrame("Manage Complaints");
        complaintsFrame.setSize(600, 400);
        complaintsFrame.setLayout(null);
        complaintsFrame.getContentPane().setBackground(new Color(33, 53, 85));

        DefaultListModel<String> complaintsListModel = new DefaultListModel<>();
        JList<String> complaintsList = new JList<>(complaintsListModel);
        complaintsList.setBounds(50, 50, 400, 200);
        complaintsList.setBackground(new Color(216, 196, 182));
        complaintsList.setForeground(Color.BLACK);

        try (BufferedReader reader = new BufferedReader(new FileReader(COMPLAINTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                complaintsListModel.addElement(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading complaints file: " + e.getMessage());
        }

        JScrollPane scrollPane = new JScrollPane(complaintsList);
        scrollPane.setBounds(50, 50, 400, 200);
        complaintsFrame.add(scrollPane);

        JButton replyBtn = createStyledButton("Reply");
        replyBtn.setBounds(460, 50, 100, 30);
        replyBtn.addActionListener(e -> {
            String selectedComplaint = complaintsList.getSelectedValue();
            if (selectedComplaint != null) {
                String response = JOptionPane.showInputDialog("Enter your response:");
                if (response != null && !response.isEmpty()) {
                    updateComplaint(selectedComplaint, response);
                    complaintsListModel.setElementAt(selectedComplaint + " - Response: " + response, complaintsList.getSelectedIndex());
                    JOptionPane.showMessageDialog(null, "Response added to complaint.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a complaint to reply.");
            }
        });
        complaintsFrame.add(replyBtn);

        JButton closeComplaintBtn = createStyledButton("Close");
        closeComplaintBtn.setBounds(460, 90, 100, 30);
        closeComplaintBtn.addActionListener(e -> {
            String selectedComplaint = complaintsList.getSelectedValue();
            if (selectedComplaint != null) {
                closeComplaint(selectedComplaint);
                complaintsListModel.removeElement(selectedComplaint);
                JOptionPane.showMessageDialog(null, "Complaint closed.");
            } else {
                JOptionPane.showMessageDialog(null, "Please select a complaint to close.");
            }
        });
        complaintsFrame.add(closeComplaintBtn);

        JButton closeBtn = createStyledButton("Back");
        closeBtn.setBounds(460, 130, 100, 30);
        closeBtn.addActionListener(e -> complaintsFrame.dispose());
        complaintsFrame.add(closeBtn);

        complaintsFrame.setVisible(true);
    }

    private static void closeComplaint(String complaint) {
        ArrayList<String> remainingComplaints = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(COMPLAINTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(complaint)) {
                    remainingComplaints.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading complaints file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COMPLAINTS_FILE))) {
            for (String remainingComplaint : remainingComplaints) {
                writer.write(remainingComplaint + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to complaints file: " + e.getMessage());
        }
    }

    private static void updateComplaint(String complaint, String response) {
        ArrayList<String> updatedComplaints = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(COMPLAINTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(complaint)) {
                    updatedComplaints.add(line + " - Response: " + response);
                } else {
                    updatedComplaints.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading complaints file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COMPLAINTS_FILE))) {
            for (String updatedComplaint : updatedComplaints) {
                writer.write(updatedComplaint + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to complaints file: " + e.getMessage());
        }
    }

    private static void blockUnblockUser() {
        String username = JOptionPane.showInputDialog(null, "Enter username to block/unblock:");
        if (username == null || username.isEmpty()) {
            return;
        }

        try {
            ArrayList<String> users = new ArrayList<>();
            boolean found = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(username)) {
                        parts[3] = parts[3].equals("false") ? "true" : "false";
                        found = true;
                    }
                    users.add(String.join(",", parts));
                }
            }

            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
                    for (String user : users) {
                        writer.write(user + "\n");
                    }
                }
                JOptionPane.showMessageDialog(null, "User status updated.");
            } else {
                JOptionPane.showMessageDialog(null, "User not found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private static void showCustomerMenu(User user) {
        JFrame customerFrame = new JFrame("Customer Menu");
        customerFrame.setSize(400, 500);
        customerFrame.setLayout(null);
        customerFrame.getContentPane().setBackground(new Color(33, 53, 85));

        JTextArea transactionHistory = new JTextArea();
        transactionHistory.setBounds(43, 50, 300, 150);
        transactionHistory.setEditable(false);
        transactionHistory.setBackground(new Color(216, 196, 182));
        transactionHistory.setForeground(Color.BLACK);
        transactionHistory.setText(getTransactionHistory(user.getUsername()));
        customerFrame.add(transactionHistory);

        JButton depositBtn = createStyledButton("Deposit");
        depositBtn.setBounds(115, 210, 150, 30);
        depositBtn.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog(null, "Enter amount to deposit:");
            try {
                double amount = Double.parseDouble(amountStr);
                user.deposit(amount);
                updateUserBalance(user);
                addTransaction(user.getUsername(), "Deposited: " + amount);
                transactionHistory.setText(getTransactionHistory(user.getUsername()));
                JOptionPane.showMessageDialog(customerFrame, "Deposit successful.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(customerFrame, "Invalid amount.");
            }
        });
        customerFrame.add(depositBtn);

        JButton withdrawBtn = createStyledButton("Withdraw");
        withdrawBtn.setBounds(115, 250, 150, 30);
        withdrawBtn.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog(null, "Enter amount to withdraw:");
            try {
                double amount = Double.parseDouble(amountStr);
                if (user.withdraw(amount)) {
                    updateUserBalance(user);
                    addTransaction(user.getUsername(), "Withdrew: " + amount);
                    transactionHistory.setText(getTransactionHistory(user.getUsername()));
                    JOptionPane.showMessageDialog(customerFrame, "Withdraw successful.");
                } else {
                    JOptionPane.showMessageDialog(customerFrame, "Insufficient balance or blocked account.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(customerFrame, "Invalid amount.");
            }
        });
        customerFrame.add(withdrawBtn);

        JButton viewBalanceBtn = createStyledButton("View Balance");
        viewBalanceBtn.setBounds(115, 290, 150, 30);
        viewBalanceBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(customerFrame, "Your balance is: " + user.getBalance());
        });
        customerFrame.add(viewBalanceBtn);

        JButton addComplaintBtn = createStyledButton("Add Complaint");
        addComplaintBtn.setBounds(115, 330, 150, 30);
        addComplaintBtn.addActionListener(e -> {
            String complaint = JOptionPane.showInputDialog(null, "Enter your complaint:");
            if (complaint != null && !complaint.isEmpty()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(COMPLAINTS_FILE, true))) {
                    writer.write(user.getUsername() + ": " + complaint + "\n");
                    JOptionPane.showMessageDialog(customerFrame, "Complaint submitted successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(customerFrame, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(customerFrame, "Complaint cannot be empty.");
            }
        });
        customerFrame.add(addComplaintBtn);

        JButton viewComplaintsBtn = createStyledButton("View Complaints");
        viewComplaintsBtn.setBounds(115, 370, 150, 30);
        viewComplaintsBtn.addActionListener(e -> {
            JTextArea complaintsArea = new JTextArea();
            complaintsArea.setEditable(false);
            complaintsArea.setBackground(new Color(216, 196, 182));
            complaintsArea.setForeground(Color.BLACK);
            complaintsArea.setText(getUserComplaints(user.getUsername()));

            JScrollPane scrollPane = new JScrollPane(complaintsArea);
            scrollPane.setBounds(40, 50, 300, 200);

            JFrame complaintViewFrame = new JFrame("Your Complaints");
            complaintViewFrame.setSize(400, 300);
            complaintViewFrame.setLayout(null);
            complaintViewFrame.getContentPane().setBackground(new Color(33, 53, 85));
            complaintViewFrame.add(scrollPane);
            complaintViewFrame.setVisible(true);
        });
        customerFrame.add(viewComplaintsBtn);

        JButton logoutBtn = createStyledButton("Logout");
        logoutBtn.setBounds(115, 410, 150, 30);
        logoutBtn.addActionListener(e -> customerFrame.dispose());
        customerFrame.add(logoutBtn);

        customerFrame.setVisible(true);
    }

    private static String getUserComplaints(String username) {
        StringBuilder complaints = new StringBuilder("Your Complaints with Responses:\n");
        try (BufferedReader reader = new BufferedReader(new FileReader(COMPLAINTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ":")) {
                    complaints.append(line.substring(username.length() + 2)).append("\n");
                }
            }
        } catch (IOException e) {
            complaints.append("Error reading complaints.");
        }
        return complaints.toString();
    }

    private static void addTransaction(String username, String transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            writer.write(username + ": " + transaction + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing transaction: " + e.getMessage());
        }
    }

    private static String getTransactionHistory(String username) {
        StringBuilder history = new StringBuilder("Transaction History:\n");
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ":")) {
                    history.append(line.substring(username.length() + 2)).append("\n");
                }
            }
        } catch (IOException e) {
            history.append("Error reading transactions.");
        }
        return history.toString();
    }

    private static void updateUserBalance(User user) {
        try {
            ArrayList<String> users = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts[0].equals(user.getUsername())) {
                        parts[2] = String.valueOf(user.getBalance());
                    }
                    users.add(String.join(",", parts));
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
                for (String userData : users) {
                    writer.write(userData + "\n");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    static class User {

        private final String username;
        private final String password;
        private double balance;
        private boolean blocked;

        public User(String username, String password, double balance, boolean blocked) {
            this.username = username;
            this.password = password;
            this.balance = balance;
            this.blocked = blocked;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public double getBalance() {
            return balance;
        }

        public boolean isBlocked() {
            return blocked;
        }

        public void deposit(double amount) {
            this.balance += amount;
        }

        public boolean withdraw(double amount) {
            if (!blocked && balance >= amount) {
                this.balance -= amount;
                return true;
            }
            return false;
        }
    }
}
