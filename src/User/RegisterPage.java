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
import RequestResponse.*;
public class RegisterPage extends Frame {
    TextField name, id, email, address, password, cpassword,phone;
    Button registerBtn,homeBtn;
    Panel panel;
    ObjectOutputStream out;
    ObjectInputStream in;
    Home home;
    Label msg;
    DashboardPage dashboardPage;
    RegisterPage(Home home, ObjectOutputStream out, ObjectInputStream in){
        this.home=home;
        this.out=out;
        this.in=in;
        this.setTitle("Register");
        this.setLayout(new BorderLayout(10,10));
        panel=new Panel(new GridLayout(9,2,10,10));

        name=new TextField("");
        email=new TextField("");
        address=new TextField("");
        id=new TextField("");
        password=new TextField("");
        password.setEchoChar('*');
        cpassword=new TextField("");
        cpassword.setEchoChar('*');
        phone=new TextField();

        Listener listener=new Listener();
        registerBtn=new Button("Register");
        registerBtn.addActionListener(listener);
        homeBtn=new Button("Back");
        homeBtn.addActionListener(listener);
        registerBtn.setBackground(Color.cyan);

        panel.add(new Label("Name"));
        panel.add(name);
        panel.add(new Label("Email"));
        panel.add(email);
        panel.add(new Label("Phone Number"));
        panel.add(phone);
        panel.add(new Label("Address"));
        panel.add(address);
        panel.add(new Label("User ID"));
        panel.add(id);
        panel.add(new Label("Password"));
        panel.add(password);
        panel.add(new Label("Confirm Password"));
        panel.add(cpassword);


        panel.add(registerBtn);
        panel.add(homeBtn);

        msg=new Label();
        msg.setBackground(Color.lightGray);
        this.add(msg,BorderLayout.SOUTH);

        this.add(panel,BorderLayout.CENTER);
        this.setSize(400,400);

        setBounds(500,100,500,500);
        this.setVisible(true);
        this.addWindowListener(new Close());
    }

    public Insets getInsets(){
        return new Insets(50,20,20,20);
    }

    private void registerUser(){
        try {
            String userPhone,userName, userId, userEmail, userAddress, userPassword, userCPassword;
            userName=name.getText().trim();
            userId=id.getText().trim();
            userEmail=email.getText().trim();
            userAddress=address.getText().trim();
            userPassword=password.getText().trim();
            userCPassword=cpassword.getText().trim();
            userPhone=phone.getText().trim();
            if(userAddress.isEmpty() || userEmail.isEmpty() || userCPassword.isEmpty() ||
                    userPassword.isEmpty() || userId.isEmpty() || userName.isEmpty() || userPhone.isEmpty())
                throw new RegisterException("Empty Field(s)");
            if(!userCPassword.equals(userPassword))
                throw new RegisterException("Confirm Password does not match with Password");

            Register register =new Register("request-for-register",userId,userPassword,userName,userEmail,userPhone,userAddress);
            out.writeObject(register);
            Register response=(Register) in.readObject();

//            if(!response.isSuccess())
//                throw new RegisterException(response.getMessage());
//            else {
//                dashboardPage = new DashboardPage(home, userId, userName, in, out);
//                this.setVisible(false);
//            }
            name.setText("");
            id.setText("");
            email.setText("");
            address.setText("");
            password.setText("");
            cpassword.setText("");
            phone.setText("");
            msg.setText(response.getMessage());
        }
        catch (RegisterException e){
            msg.setText(e.toString());
        }
        catch (Exception e){
            msg.setText("Something went wrong");
            System.out.println(e);
        }
    }

    private class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==homeBtn){
                closeRegister();
            }
            else if (e.getSource()==registerBtn) {
                registerUser();
            }
        }
    }
    public void closeRegister(){
        home.setVisible(true);
        this.setVisible(false);
    }
    class RegisterException extends Exception{
        String msg;
        RegisterException(String m){
            msg=m;
        }
        public String toString(){
            return  msg;
        }
    }
    class Close extends WindowAdapter {
        public void windowClosing(WindowEvent e){
            closeRegister();
        }
    }
}
