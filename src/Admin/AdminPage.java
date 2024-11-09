package Admin;

import RequestResponse.AddNewRoom;
import User.LoginPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AdminPage extends Frame {
    Button requests, users,bookings ,addRoom, allRooms, logout;
    ObjectInputStream in; ObjectOutputStream out;
    String userId;
    RequestForRegister requestForRegister;
    LoginPage login;
    public AdminPage(LoginPage login, String userID, ObjectInputStream in, ObjectOutputStream out) {
        this.out=out;
        this.in=in;
        this.login=login;
        requestForRegister = null;
        this.userId = userID;
        this.setLayout(new GridLayout(6, 1, 10, 10));
        this.addWindowListener(new Close());
        Listener listener = new Listener();
        requests = new Button("Request for Register");
        requests.addActionListener(listener);
        users = new Button("All Users");
        users.addActionListener(listener);
        addRoom = new Button("Add New Room");
        addRoom.addActionListener(listener);
        allRooms = new Button("All Rooms");
        allRooms.addActionListener(listener);
        bookings=new Button("All Current Bookings");
        bookings.addActionListener(listener);
        logout = new Button("Logout");
        logout.addActionListener(listener);
        logout.setBackground(Color.red);
        this.add(requests);
        this.add(users);
        this.add(addRoom);
        this.add(allRooms);
        add(bookings);
        this.add(logout);

        this.setVisible(true);
        this.setBounds(500,100,400, 300);
        this.setTitle("Admin-" + userId);
    }


    public void updateID(String userId) {
        this.userId = userId;
        this.setTitle("Admin-" + userId);
    }

    private void showLogin() {
        this.setVisible(false);
        login.setVisible(true);
    }

    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }

    public void showRequests() {
        requestForRegister = new RequestForRegister(this,in,out);
        this.setVisible(false);
        requestForRegister.setVisible(true);
    }
    public void showBookings(){

        new CurrentBookings(userId,in,out,this);
        this.setVisible(false);
    }
    public void showUsers(){
        new AllUsers(this,in,out);
        this.setVisible(false);
    }
    public void showAddRoom(){
        this.setVisible(false);
        new AddRoom(this,out,in);
    }
    public  void showAllRooms(){
        new AllRoomsPage(in,out,this);
        this.setVisible(false);
    }
    class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logout) {
                showLogin();
            } else if (e.getSource() == requests) {
                showRequests();
            }
            else if(e.getSource()==bookings){
                showBookings();
            } else if (e.getSource()==users) {
                showUsers();
            } else if (e.getSource()==addRoom) {
                showAddRoom();
            } else if (e.getSource()==allRooms) {
                showAllRooms();
            }
        }
    }
    class Close extends WindowAdapter {
        public void windowClosing(WindowEvent e){
            System.exit(0);
        }
    }
}
