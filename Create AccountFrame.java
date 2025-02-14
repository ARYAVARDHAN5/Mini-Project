// CreateAccountFrame.java
package bank.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Random;
import bank.util.DatabaseConnection;
import bank.model.User;

public class CreateAccountFrame extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, phoneField;
    private JTextField dobField, nationalityField, addressField, pincodeField;
    private JTextField districtField, stateField, userIdField;
    private JPasswordField passwordField;
    private JComboBox<String> genderCombo;
    
    public CreateAccountFrame() {
        setTitle("Create New Account");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel with GridBagLayout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Initialize components
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        dobField = new JTextField(20);
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        nationalityField = new JTextField(20);
        addressField = new JTextField(20);
        pincodeField = new JTextField(20);
        districtField = new JTextField(20);
        stateField = new JTextField(20);
        userIdField = new JTextField(20);
        passwordField = new JPasswordField(20);
        
        // Add components to panel
        addFormField(mainPanel, gbc, "First Name:", firstNameField, 0);
        addFormField(mainPanel, gbc, "Last Name:", lastNameField, 1);
        addFormField(mainPanel, gbc, "Email:", emailField, 2);
        addFormField(mainPanel, gbc, "Phone:", phoneField, 3);
        addFormField(mainPanel, gbc, "Date of Birth:", dobField, 4);
        addFormField(mainPanel, gbc, "Gender:", genderCombo, 5);
        addFormField(mainPanel, gbc, "Nationality:", nationalityField, 6);
        addFormField(mainPanel, gbc, "Address:", addressField, 7);
        addFormField(mainPanel, gbc, "Pincode:", pincodeField, 8);
        addFormField(mainPanel, gbc, "District:", districtField, 9);
        addFormField(mainPanel, gbc, "State:", stateField, 10);
        addFormField(mainPanel, gbc, "User ID:", userIdField, 11);
        addFormField(mainPanel, gbc, "Password:", passwordField, 12);
        
        // Submit button
        gbc.gridx = 0; gbc.gridy = 13; gbc.gridwidth = 2;
        JButton submitButton = new JButton("Create Account");
        submitButton.addActionListener(e -> createAccount());
        mainPanel.add(submitButton, gbc);
        
        // Back button
        gbc.gridy = 14;
        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            this.dispose();
        });
        mainPanel.add(backButton, gbc);
        
        // Add scroll pane
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane);
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
    
    private void createAccount() {
        // Validate input fields
        if (!validateFields()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Generate account details
            String accountNumber = generateAccountNumber();
            String debitCard = generateDebitCard();
            String expiry = generateExpiry();
            String cvv = generateCVV();
            
            // Insert into database
            String sql = "INSERT INTO users (first_name, last_name, email, phone, date_of_birth, " +
                        "gender, nationality, address, pincode, district, state, user_id, password, " +
                        "account_number, balance, debit_card, card_expiry, cvv) VALUES " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, firstNameField.getText());
            stmt.setString(2, lastNameField.getText());
            stmt.setString(3, emailField.getText());
            stmt.setString(4, phoneField.getText());
            stmt.setString(5, dobField.getText());
            stmt.setString(6, genderCombo.getSelectedItem().toString());
            stmt.setString(7, nationalityField.getText());
            stmt.setString(8, addressField.getText());
            stmt.setString(9, pincodeField.getText());
            stmt.setString(10, districtField.getText());
            stmt.setString(11, stateField.getText());
            stmt.setString(12, userIdField.getText());
            stmt.setString(13, new String(passwordField.getPassword()));
            stmt.setString(14, accountNumber);
            stmt.setDouble(15, 5000.0); // Default balance
            stmt.setString(16, debitCard);
            stmt.setString(17, expiry);
            stmt.setString(18, cvv);
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, 
                "Account created successfully!\nAccount Number: " + accountNumber +
                "\nDebit Card: " + debitCard + "\nExpiry: " + expiry + "\nCVV: " + cvv);
                
            // Open main menu
            new MainMenuFrame(userIdField.getText()).setVisible(true);
            this.dispose();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error creating account: " + ex.getMessage());
        }
    }
    
    private boolean validateFields() {
        // Add validation logic here
        return true; // Simplified for example
    }
    
    private String generateAccountNumber() {
        Random rand = new Random();
        return String.format("%012d", Math.abs(rand.nextLong() % 1000000000000L));
    }
    
    private String generateDebitCard() {
        Random rand = new Random();
        return String.format("%015d", Math.abs(rand.nextLong() % 1000000000000000L));
    }
    
    private String generateExpiry() {
        // Generate expiry date 5 years from now
        return "12/28"; // Simplified for example
    }
    
    private String generateCVV() {
        Random rand = new Random();
        return String.format("%03d", rand.nextInt(1000));
    }
}
