package bank.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import bank.util.DatabaseConnection;

public class LoginFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        setTitle("Bank Management System - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add components
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("User ID:"), gbc);
        
        gbc.gridx = 1;
        userIdField = new JTextField(20);
        mainPanel.add(userIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        mainPanel.add(loginButton, gbc);
        
        // Create Account button
        gbc.gridy = 3;
        JButton createAccountButton = new JButton("Create New Account");
        createAccountButton.addActionListener(e -> openCreateAccount());
        mainPanel.add(createAccountButton, gbc);
        
        add(mainPanel);
    }
    
    private void handleLogin() {
        String userId = userIdField.getText();
        String password = new String(passwordField.getPassword());
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM users WHERE user_id = ? AND password = ?")) {
            
            stmt.setString(1, userId);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                openMainMenu(userId);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }
    
    private void openCreateAccount() {
        new CreateAccountFrame().setVisible(true);
        this.dispose();
    }
    
    private void openMainMenu(String userId) {
        new MainMenuFrame(userId).setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
