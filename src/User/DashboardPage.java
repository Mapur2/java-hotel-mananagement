package User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import RequestResponse.*;

public class DashboardPage extends Frame {
    Label intro, msg;
    Label welcome;
    String name, id;
    ObjectOutputStream out;
    ObjectInputStream in;
    Button logout;
    Home home;
    TextField bookDate, checkOut;
    TextField adults, children;
    Button findRooms;
    Panel roomsPanel;
    RoomBookingPage roomBookingPage;
    Button confirmedBookingsBtn, cancelBooking, previousBook;
    String currDate;

    DashboardPage(Home home, String id, String name, ObjectInputStream in, ObjectOutputStream out) {

        this.home = home;
        this.id = id;
        this.name = name;
        this.out = out;
        this.in = in;
        currDate = (new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        roomBookingPage = null;
        this.setTitle("Welcome, " + name);
        this.setLayout(new BorderLayout(10, 10));
        logout = new Button("Logout");
        logout.addActionListener(new Listener());
        intro = new Label("Booking at Seaside Serenity Hotel offers an exceptional experience where luxury " +
                "meets tranquility.");

        this.add(intro, BorderLayout.NORTH);
        this.add(logout, BorderLayout.SOUTH);
        Panel booking = new Panel(new GridLayout(8, 2, 10, 10));

        bookDate = new TextField("", 30);
        booking.add(new Label("Booking Date (YYYY-MM-DD)"));
        booking.add(bookDate);

        checkOut = new TextField("", 30);
        booking.add(new Label("Check out Date  (YYYY-MM-DD)"));
        booking.add(checkOut);

        booking.add(new Label("No. of family members"));
        booking.add(new Label());
        booking.add(new Label("Adults"));
        adults = new TextField("");
        booking.add(adults);
        booking.add(new Label("Children (Less than 14 years old)"));
        children = new TextField("0");
        booking.add(children);
        findRooms = new Button("Find Room");
        findRooms.addActionListener(new Listener());
        booking.add(findRooms);
        msg = new Label();
        booking.add(msg);
        confirmedBookingsBtn = new Button("Confirmed Bookings");
        confirmedBookingsBtn.addActionListener(new Listener());
        booking.add(confirmedBookingsBtn);
        cancelBooking = new Button("Cancel Booking");
        cancelBooking.addActionListener(new Listener());
        booking.add(cancelBooking);
        previousBook=new Button("Previous Bookings");
        previousBook.addActionListener(new Listener());
        booking.add(previousBook);
        this.add(booking, BorderLayout.CENTER);
        this.setVisible(true);

        setBounds(500, 100, 600, 400);
        this.setSize(600, 400);
    }

    public void logoutUser() {
        this.setVisible(false);
        home.setVisible(true);
    }

    public void sendFindRoom() {
        String from, to, a, ch;
        from = bookDate.getText().trim();
        to = checkOut.getText().trim();
        a = adults.getText().trim();
        ch = children.getText().trim();
        if (from.isEmpty() || to.isEmpty() || a.isEmpty() || ch.isEmpty()) {
            msg.setText("Empty Field(s)");
            return;
        }
        if (!checkDateFormat(from) || !checkDateFormat(to) || !checkDateDifference(from, to) || !checkDateDifference(currDate, from))
            return;
        try {
            msg.setText("");
            out.writeObject(new RoomQuery("findroom", from, to, Integer.parseInt(ch), Integer.parseInt(a)));
            RoomQuery response = (RoomQuery) in.readObject();
            if (response.isSuccess()) {
                /**
                 * roomsInThisRange
                 */
                showRooms(response.rooms);
            } else {
                this.add(new Label(response.getMessage()), BorderLayout.SOUTH);
            }
        } catch (Exception e) {

            System.out.println(e);
        }
    }

    public void showRooms(ArrayList<Room> roomsInThisRange) {
        roomBookingPage = new RoomBookingPage(in, out, roomsInThisRange, this, bookDate.getText().trim(), checkOut.getText().trim(), id);
        this.setVisible(false);
    }

    public boolean checkDateDifference(String from, String to) {
        int sf = getDateStamp(from), st = getDateStamp(to);
        if (sf - st > 0) {
            msg.setText("Invalid Date difference.");
            return false;
        }
        return true;
    }

    int getDateStamp(String s) {
        return Integer.parseInt(s.substring(0, 4) + s.substring(5, 7) + s.substring(8, 10));
    }

    public boolean checkDateFormat(String date) {
        int n = date.length();
        if (n != 10) {
            msg.setText("Invalid length of date");
            return false;
        }
        int m = Integer.parseInt(date.substring(5, 7));
        if (m < 1 || m > 12) {
            msg.setText("Invalid month");
            return false;
        }

        int d = Integer.parseInt(date.substring(8, 10));
        int days[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (m == 2 && (d < 1 || d > 28)) {
            msg.setText("Invalid day. February has 28 days");
            return false;
        }
        if (d < 1 || d > days[m - 1]) {
            msg.setText("Invalid day. ");
            return false;
        }
        return true;
    }

    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }

    private void showConfirmed() {
        this.setVisible(false);
        new ConfirmedBookingsPage(id, in, out, this);
    }

    public void showPreviousBook() {
        this.setVisible(false);
        new PreviousBookingsPage(id, in, out, this);
    }

    private void showCancelBooking() {
        new CancelBookingPage(id, in, out, this);
        this.setVisible(false);
    }

    class Listener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == logout) {
                logoutUser();
            } else if (e.getSource() == findRooms) {
                sendFindRoom();
            } else if (e.getSource() == confirmedBookingsBtn) {
                showConfirmed();
            } else if (e.getSource() == cancelBooking) {
                showCancelBooking();
            } else if (e.getSource() == previousBook) {
                showPreviousBook();
            }
        }
    }

}
