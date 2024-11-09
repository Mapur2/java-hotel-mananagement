package User;
import RequestResponse.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ConfirmedBookingsPage extends Frame {
    String guest_id;
    ObjectInputStream in;
    ObjectOutputStream out;
    ArrayList<BookedRoom> bookedRoomList;
    DashboardPage dashboard;
    Button back;
    public ConfirmedBookingsPage(String id, ObjectInputStream in, ObjectOutputStream out, DashboardPage dashboard){
        this.dashboard=dashboard;
        guest_id=id;
        this.out=out;
        this.in=in;
        this.setTitle("Confirmed Bookings");
        addWindowListener(new Close());
        back=new Button("Back");
        back.addActionListener(new Listener());
        try{
            out.writeObject(new BookedRoomQuery(id,"booked-rooms"));
            BookedRoomQuery bRQ=(BookedRoomQuery) in.readObject();
            bookedRoomList=bRQ.getBookedRoomList();
            if(bRQ.isSuccess()){
                int n=bookedRoomList.size();
                setLayout(new GridLayout(n + 1, 1, 5, 5));
                if(n==0)
                {
                    add(new Label("No bookings found"));
                }
                else {
                    setLayout(new GridLayout(n + 1, 1, 5, 5));
                    for (int i = 0; i < n; i++) {
                        BookedRoom b = bookedRoomList.get(i);
                        add(new Label(b.getNumber() + " From " + b.getFrom() + " to " + b.getTo() + " Price: " + b.getPrice()));
                    }
                }
            }
            add(back);
        }
        catch (Exception e){
            System.out.println(e);
        }
        //this.setSize(600,100*(bookedRoomList.size()+1));

        setBounds(500,100,600,80*(bookedRoomList.size()+2));
        setVisible(true);
    }
    private  void showDashboard(){
        setVisible(false);
        dashboard.setVisible(true);
    }
    class Listener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            showDashboard();
        }
    }
    class Close extends WindowAdapter {
        public void windowClosing(WindowEvent e){
            showDashboard();
        }
    }
    public Insets getInsets(){
        return new Insets(50,20,20,20);
    }

}
