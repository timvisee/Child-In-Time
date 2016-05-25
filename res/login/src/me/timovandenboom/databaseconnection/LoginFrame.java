package me.timovandenboom.databaseconnection;

import javax.swing.JFrame;

public class LoginFrame extends JFrame{

    private LoginPanel loginPanel;

    public LoginFrame(){

        this.loginPanel = new LoginPanel();
        setSize(300, 200);
        setTitle("Database Connector");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        add(this.loginPanel);
    }

}