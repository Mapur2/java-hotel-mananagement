package User;
import RequestResponse.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CancelBookingPage extends Frame {
    String guest_id;
    ObjectInputStream in;
    ObjectOutputStream out;
    ArrayList<BookedRoom> bookedRoomList;
    DashboardPage dashboard;
    Button back,confirm;
    Panel confirmCancelPanel;
    TextField bookingNum;
    TextArea detail;
    String date;
    Panel bookings;
    public CancelBookingPage(String id, ObjectInputStream in, ObjectOutputStream out, DashboardPage dashboard){
        this.dashboard=dashboard;
        guest_id=id;
        this.out=out;
        this.in=in;
        this.setTitle("Cancel Bookings");
        addWindowListener(new Close());
        back=new Button("Back");
        back.addActionListener(new Listener());
        setLayout(new BorderLayout(10,10));

        try{
            out.writeObject(new BookedRoomQuery(id,"booked-rooms"));
            BookedRoomQuery bRQ=(BookedRoomQuery) in.readObject();
            bookedRoomList=bRQ.getBookedRoomList();
            if(bRQ.isSuccess()){
                int n=bookedRoomList.size();
                System.out.println("size :"+n);
                bookings=new Panel(new GridLayout(n+1,1,5,5));
                bookings.add(new Label("Your Bookings"));
                for (int i = 0; i < n; i++) {
                    BookedRoom b=bookedRoomList.get(i);
                    bookings.add(new Label("BN-HT-"+b.getId()+" Room No."+b.getNumber()+" From "+b.getFrom()+" to "+b.getTo()+ " Price: "+b.getPrice()));
                }
                add(bookings,BorderLayout.NORTH);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

        date=(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        bookingNum=new TextField();
        confirmCancelPanel=new Panel(new GridLayout(6,1,10,10));
        confirmCancelPanel.add(new Label("Current Date: "+date));
        confirmCancelPanel.add(new Label("Enter the last two digit of your booking number"));
        confirmCancelPanel.add(bookingNum);
        Button find=new Button("Find");
        find.addActionListener(new Listener());
        confirmCancelPanel.add(find);
        detail =new TextArea();
        confirmCancelPanel.add(detail);
        confirm=new Button("Confirm");
        confirm.addActionListener(new Listener());
        confirm.setEnabled(false);
        confirmCancelPanel.add(confirm);
        this.setSize(600,500);
        add(confirmCancelPanel,BorderLayout.CENTER);
        add(back,BorderLayout.SOUTH);
        setBounds(500,100,600,600);
        setVisible(true);
    }
    private  void showDashboard(){
        setVisible(false);
        dashboard.setVisible(true);
    }
    private void findRoom(){
        try {

            int bn = Integer.parseInt(bookingNum.getText());
            int d = getDateStamp(date);
            for (int i = 0; i < bookedRoomList.size(); i++) {
                if (bn == bookedRoomList.get(i).getId()) {
                    if (d <= getDateStamp(bookedRoomList.get(i).getFrom())) {
                        BookedRoom b = bookedRoomList.get(i);
                        detail.setText("Room No." + b.getNumber() + " From " + b.getFrom() + " to " + b.getTo());
                        confirm.setEnabled(true);
                    } else {
                        detail.setText("Booking of room cannot be cancelled");
                        confirm.setEnabled(false);
                    }
                    return;
                }

            }
            detail.setText("Booking number not found");
        }
        catch (NumberFormatException e){
            detail.setText("Invalid booking number");
        }
        catch (Exception e){
            System.out.println(e);
            detail.setText("Something went wrong");
        }
    }
    private void updateList(){
        this.remove(bookings);
        try{
            out.writeObject(new BookedRoomQuery(guest_id,"booked-rooms"));
            BookedRoomQuery bRQ=(BookedRoomQuery) in.readObject();
            bookedRoomList=bRQ.getBookedRoomList();
            if(bRQ.isSuccess()){
                int size=bookedRoomList.size();
                bookings=new Panel(new GridLayout(size+1,1,5,5));
                bookings.add(new Label("Your Bookings"));
                for (int i = 0; i < size; i++) {
                    BookedRoom b=bookedRoomList.get(i);
                    bookings.add(new Label("BN-HT-"+b.getId()+" Room No."+b.getNumber()+" From "+b.getFrom()+" to "+b.getTo()+ " Price: "+b.getPrice()));
                }
                add(bookings,BorderLayout.NORTH);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    private void cancelBooking(){
        try {
            out.writeObject(new CancelBookingQuery(Integer.parseInt(bookingNum.getText())));
            CancelBookingQuery cBQ=(CancelBookingQuery) in.readObject();
            if(cBQ.isSuccess()) {
                detail.setText("Cancelled booking number BN-HT-" + bookingNum.getText());
                updateList();
            }
            else{
                detail.setText("Something went wrong");
            }
        }
        catch (Exception e){
            detail.setText("Something went wrong");
        }
        finally {
            confirm.setEnabled(false);
        }
    }
    private int getDateStamp(String s) {
        return Integer.parseInt(s.substring(0, 4) + s.substring(5, 7) + s.substring(8, 10));
    }
    class Listener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getSource()==back)
                showDashboard();
            else if(e.getActionCommand().equals("Find")){
                if(!bookingNum.getText().isEmpty())
                    findRoom();
            }
            else if(confirm==e.getSource()){
                cancelBooking();
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
