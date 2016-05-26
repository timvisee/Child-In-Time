package me.timovandenboom.databaseconnection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel{

    private JButton login;
    private JTextField username;
    private JPasswordField password;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private ClickListener listener;

    public LoginPanel(){

        setLayout(null);
        createLabels();
        createTextFields();
        createButton();
    }

    private void createButton(){
        this.login = new JButton("Login");
        this.login.setSize(100, 20);
        this.login.setLocation(100, 120);
        this.listener = new ClickListener();
        this.login.addActionListener(this.listener);
        add(this.login);
    }

    private void createTextFields(){
        this.username = new JTextField();
        this.password = new JPasswordField();
        this.username.setSize(100, 20);
        this.password.setSize(100, 20);
        this.username.setLocation(100, 50);
        this.password.setLocation(100, 80);
        add(this.username);
        add(this.password);
    }

    private void createLabels(){
        this.usernameLabel = new JLabel("Username:");
        this.usernameLabel.setLocation(25, 50);
        this.usernameLabel.setSize(100, 20);
        this.passwordLabel = new JLabel("Password:");
        this.passwordLabel.setLocation(25, 80);
        this.passwordLabel.setSize(100, 20);
        add(this.usernameLabel);
        add(this.passwordLabel);
    }

    class ClickListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent ae) {

            if (ae.getSource() == login){


                try{
                    Connection conn = SimpleDataSourceV2.getConnection(username.getText(), new String(password.getPassword()));
                    System.out.println("Connection to the database: " + SimpleDataSourceV2.database + "!");
                }
                catch(SQLException exc){

                    JOptionPane.showMessageDialog(null, "Invalid username or password. Try again.", "Login Failure", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

    }
}