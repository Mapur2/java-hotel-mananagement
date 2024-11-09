package User;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import Admin.AdminPage;
import RequestResponse.*;
public class LoginPage extends Frame {
    TextField id, password;
    Button loginBtn,homeBtn, adminBtn;
    Panel panel;
    ObjectOutputStream out;
    ObjectInputStream in;
    Home home;
    Label msg;
    
    LoginPage(Home home, ObjectOutputStream out, ObjectInputStream in){
        this.home=home;
        this.out=out;
        this.in=in;
        this.setTitle("User Login");
        this.setLayout(new BorderLayout(10,10));
        panel=new Panel(new GridLayout(11,2,10,10));

        id=new TextField();
        password=new TextField();
        password.setEchoChar('*');


        Listener listener=new Listener();
        loginBtn=new Button("Login");
        loginBtn.addActionListener(listener);
        homeBtn=new Button("Back");
        homeBtn.addActionListener(listener);
        loginBtn.setBackground(Color.cyan);

        panel.add(new Label("User ID"));
        panel.add(id);
        panel.add(new Label("Password"));
        panel.add(password);
        panel.add(loginBtn);
        panel.add(homeBtn);

        msg=new Label();
        msg.setBackground(Color.lightGray);
        this.add(msg,BorderLayout.SOUTH);

        this.add(panel,BorderLayout.CENTER);

        setBounds(500,100,400,400);
        this.setSize(400,400);
        this.setVisible(true);
        this.addWindowListener(new Close());
    }

    public Insets getInsets(){
        return new Insets(50,20,20,20);
    }

    private void loginUser(){
        try {
            String  userId, userPassword;

            userId=id.getText().trim();
            userPassword=password.getText().trim();
            if(userPassword.isEmpty() || userId.isEmpty())
                throw new LoginException("Empty Field(s)");
            out.writeObject(new Login("login",userId,userPassword));
            Login response=(Login) in.readObject();

            if(!response.isSuccess()) {
                throw new LoginException(response.getMessage());
            }
            else {
                id.setText("");
                password.setText("");
                if(response.getType().equals("user"))
                    new DashboardPage(home, userId, response.getName(), in, out);
                else
                    new AdminPage(this, userId, in, out);
                this.setVisible(false);
                msg.setText("");
            }

        }
        catch (LoginException e){
            msg.setText(e.toString());
        }
        catch (Exception e){
            msg.setText("Something went wrong");
            System.out.println(e);
        }
    }
//    private void loginAdmin(){
//        try {
//            String  userId, userPassword;
//
//            userId=id.getText().trim();
//            userPassword=password.getText().trim();
//            if(userPassword.isEmpty() || userId.isEmpty())
//                throw new LoginException("Empty Field(s)");
//            out.writeObject(new Login("login-admin",userId,userPassword));
//            Login response=(Login) in.readObject();
//
//            if(!response.isSuccess()) {
//                throw new LoginException(response.getMessage());
//            }
//            else {
//                id.setText("");
//                password.setText("");
//                new AdminPage(this, userId, in, out);
//                this.setVisible(false);
//                msg.setText("");
//            }
//
//        }
//        catch (LoginException e){
//            msg.setText(e.toString());
//        }
//        catch (Exception e){
//            msg.setText("Something went wrong");
//            System.out.println(e);
//        }
//    }
    private class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==homeBtn){
                closeLogin();
            }
            else if (e.getSource()==loginBtn) {
                loginUser();
            }
        }
    }
    public void closeLogin(){
        home.setVisible(true);
        this.setVisible(false);
    }
    class LoginException extends Exception{
        String msg;
        LoginException(String m){
            msg=m;
        }
        public String toString(){
            return  msg;
        }
    }
    class Close extends WindowAdapter {
        public void windowClosing(WindowEvent e){
            closeLogin();
        }
    }
}
