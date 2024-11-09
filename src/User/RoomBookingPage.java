package User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import RequestResponse.*;
public class RoomBookingPage extends Frame {
    ScrollPane pane;
    ArrayList<Room> rooms;
    ObjectInputStream in;
    ObjectOutputStream out;
    Panel roomsPanel;
    DashboardPage dashboardPage;
    Dialog dialog;
    Button confirm,okdialog;
    String bookDate,checkOut;
    ArrayList<String> roomNumbers;
    String guest_id;
    Panel confirmPanel;
    Label confirmed;
    Label confirmedDialog, confirmedPrice;
    RoomBookingPage( ObjectInputStream in, ObjectOutputStream out, ArrayList<Room> rm, DashboardPage d,String bookDate, String checkOut, String guest_id){
        this.guest_id=guest_id;
        this.bookDate=bookDate;
        this.checkOut=checkOut;
        dashboardPage=d;
        rooms=rm;
        this.in=in;
        this.out=out;
        this.setLayout(new BorderLayout(10,10));
        this.setTitle("Book your Room");
        dialog=new Dialog(this,"Booked",true);
        dialog.setSize(500,150);
        dialog.addWindowListener(new CloseDialog(dialog));
        confirm=new Button("Confirm");
        confirm.setEnabled(false);
        confirm.addActionListener(new Listener());

        confirmed =new Label();
        confirmedDialog=new Label();
        confirmedPrice=new Label();
        dialog.setLayout(new GridLayout(3,1,5,5));
        okdialog=new Button("Okay");
        okdialog.addActionListener(new OkayDialog(dialog));
        dialog.add(confirmedDialog);
        dialog.add(confirmedPrice);
        dialog.add(okdialog);
        dialog.setBounds(500,60,400,100);

        this.addWindowListener(new Close());
//        add(new Label("From = "+bookDate+"  To = "+checkOut), BorderLayout.NORTH);
        roomNumbers=new ArrayList<>();
        roomsPanel=new Panel(new GridLayout(rooms.size(), 1,10,10));
        roomsPanel.setSize(750,750);
        for (int i=0;i < rooms.size();i++)
        {
            Room room=rooms.get(i);
            roomsPanel.add(new RoomPanel(room.number,room.type,room.bed,room.price,room.floor_number,room.description,room.amenities));
        }
        confirmPanel=new Panel(new GridLayout(3,1));
        confirmPanel.add(new Label("From = "+bookDate+"  To = "+checkOut));
        confirmPanel.add(confirmed);
        confirmPanel.add(confirm);

        pane=new ScrollPane();
        pane.setWheelScrollingEnabled(true);
        pane.add(roomsPanel);
        this.add(pane,BorderLayout.CENTER);
        add(confirmPanel,"South");

        setBounds(500,100,800,500);
        this.setSize(800,500);
        this.setVisible(true);
    }

    public Insets getInsets(){
        return new Insets(50,20,20,20);
    }


    private void showHome(){
        this.setVisible(false);
        dashboardPage.setVisible(true);
    }
    private void bookRooms(){
        try{
            out.writeObject(new RoomsBook(roomNumbers,guest_id,bookDate,checkOut));
            RoomsBook r=(RoomsBook) in.readObject();
            confirmed.setText(r.getMessage());
            confirmedDialog.setText("Confirmed Room numbers: "+roomNumbers);
            confirmedPrice.setText(" From = "+bookDate+"  To = "+checkOut+" Total Price: "+r.getPrice());
            showHome();
            dialog.setVisible(true);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    class  Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            bookRooms();
        }
    }
    class Close extends WindowAdapter{
        public void windowClosing(WindowEvent e){
            showHome();
        }
    }
    class RoomPanel extends Panel {
        String number;
        String type;
        int bed;
        int price;
        int floor_number;
        String description;
        String amenities;
        Button book;
        boolean booked;

        public RoomPanel(String number, String type, int bed, int price, int floor_number, String description, String amenities) {
            booked=false;
            this.setBackground(Color.lightGray);
            this.setSize(100,100);
            this.number = number;
            this.type = type;
            this.bed = bed;
            this.price = price;
            this.floor_number = floor_number;
            this.description = description;
            this.amenities = amenities;
            this.setLayout(new BorderLayout(10,10));
            Panel top = new Panel(new GridLayout(5,1,5,5));
            top.add(new Label("Type:"+this.type));
            top.add(new Label("No. of bed="+this.bed));
            top.add(new Label("Price="+this.price));
            top.add(new Label(description));
            top.add(new Label(amenities));
            this.add(new Label("Room Number="+this.number),BorderLayout.WEST);
            this.add(top,BorderLayout.CENTER);
            book=new Button("Book");
            book.addActionListener(new BookListener());
            this.add(book,BorderLayout.EAST);
        }
        class BookListener implements ActionListener{
            public void actionPerformed(ActionEvent e){
                if(!booked) {
                    roomNumbers.add(number);
                    booked=true;
                    book.setLabel("Remove");
                    book.setBackground(Color.pink);
                }
                else{
                    roomNumbers.remove(number);
                    book.setLabel("Book");
                    book.setBackground(Color.white);
                    booked=false;
                }
                if(!roomNumbers.isEmpty()){
                    confirmed.setText("Room numbers to confirm: "+roomNumbers);
                    confirm.setEnabled(true);
                }
                else   {
                    confirmed.setText("Room numbers to confirm: "+roomNumbers);
                    confirm.setEnabled(false);
                }
            }
        }
    }
}
class OkayDialog implements ActionListener{
    Dialog d;
    OkayDialog(Dialog a){
        d=a;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        d.setVisible(false);
    }
}
class CloseDialog extends WindowAdapter{
    Dialog d;
    CloseDialog(Dialog a){
        d=a;
    }
    public void windowClosing(WindowEvent e){
        d.setVisible(false);
    }

}