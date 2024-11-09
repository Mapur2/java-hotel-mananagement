package Admin;

import RequestResponse.BookedRoom;
import RequestResponse.BookedRoomQuery;
import RequestResponse.CancelBookingQuery;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CurrentBookings extends Frame {
    ObjectInputStream in;
    ObjectOutputStream out;
    ArrayList<BookedRoom> bookedRoomList;
    AdminPage dashboard;
    Button back;
    int countReq;
    int numbers[];
    Button cancelBooking[];
    public CurrentBookings(String id, ObjectInputStream in, ObjectOutputStream out, AdminPage dashboard){
        this.dashboard=dashboard;
        this.out=out;
        this.in=in;
        this.setTitle("Current Bookings");
        addWindowListener(new Close());
        back=new Button("Back");
        back.addActionListener(new Listener());
        try{
            out.writeObject(new BookedRoomQuery("all-current-bookings"));
            BookedRoomQuery bRQ=(BookedRoomQuery) in.readObject();
            bookedRoomList=bRQ.getBookedRoomList();
            if(bRQ.isSuccess()){
                int n=bookedRoomList.size();
                countReq=n;

                if(n==0)
                {
                    this.setLayout(new GridLayout(n + 1, 1, 5, 5));
                    add(new Label("No bookings found"));
                }
                else {
                    cancelBooking=new Button[n];
                    numbers=new int[n];
                    this.setLayout(new GridLayout(n+1, 3, 5, 5));
                    Listener listener=new Listener();
                    for (int i = 0; i < n; i++) {
                        BookedRoom b = bookedRoomList.get(i);
                        this.add(new Label(b.getNumber() + " From " + b.getFrom() + " to " + b.getTo()));
                        this.add(new Label(" Price: " + b.getPrice()));
                        cancelBooking[i]=new Button("Cancel Booking");
                        cancelBooking[i].addActionListener(listener);
                        this.add(cancelBooking[i]);
                        numbers[i]=b.getId();
                    }
                }
            }
            this.add(new Label(" "));
            this.add(back);
            this.add(new Label(" "));
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
    private void cancelBookedRoom(int i){
        try {
            out.writeObject(new CancelBookingQuery(numbers[i]));
            CancelBookingQuery r=(CancelBookingQuery) in.readObject();
            if(r.isSuccess()){
                cancelBooking[i].setBackground(Color.red);
                cancelBooking[i].setEnabled(false);
            }
        }
        catch (Exception e){

        }
    }
    class Listener implements ActionListener {
        public void actionPerformed(ActionEvent e){

            if(e.getSource()==back) showDashboard();
            else{
                int i=0;
                for(Button ele:cancelBooking){
                    if(ele==e.getSource()){
                        break;
                    }
                    i++;
                }
                cancelBookedRoom(i);
            }
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
