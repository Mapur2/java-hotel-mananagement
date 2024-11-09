package Admin;


import RequestResponse.AllRooms;
import RequestResponse.DeleteRoom;
import RequestResponse.Room;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AllRoomsPage extends Frame {

    ScrollPane pane;
    ArrayList<Room> rooms;

    ObjectInputStream in;
    ObjectOutputStream out;
    Panel roomsPanel;
    AdminPage dashboardPage;

    AllRoomsPage(ObjectInputStream in, ObjectOutputStream out, AdminPage d) {
        dashboardPage = d;
        this.in = in;
        this.out = out;
        this.setLayout(new BorderLayout(10, 10));
        this.setTitle("All Rooms");

        this.addWindowListener(new Close());
        try {

            out.writeObject(new AllRooms());
            AllRooms allRooms = (AllRooms) in.readObject();
            if (!allRooms.isSuccess())
                throw new Exception(allRooms.getMessage());
            rooms = allRooms.getRooms();
            roomsPanel = new Panel(new GridLayout(rooms.size(), 1, 10, 10));
            roomsPanel.setSize(750, 750);
            for (Room room : rooms) {
                roomsPanel.add(new RoomPanel(room));
            }
            pane = new ScrollPane();
            pane.setWheelScrollingEnabled(true);
            pane.add(roomsPanel);
            this.add(pane, BorderLayout.CENTER);
        } catch (Exception e) {
            add(new Label(e.getMessage()));
        }


        setBounds(500, 100, 800, 500);
        this.setSize(800, 500);
        this.setVisible(true);
    }

    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }

    public void updateRoomsPanel() {
        try {

            pane.remove(roomsPanel);
            this.remove(pane);
            out.writeObject(new AllRooms());
            AllRooms allRooms = (AllRooms) in.readObject();
            if (!allRooms.isSuccess())
                throw new Exception(allRooms.getMessage());
            rooms = allRooms.getRooms();
            roomsPanel = new Panel(new GridLayout(rooms.size(), 1, 10, 10));
            roomsPanel.setSize(750, 750);
            for (Room room : rooms) {
                roomsPanel.add(new RoomPanel(room));
            }
            pane.add(roomsPanel);
            this.add(pane, BorderLayout.CENTER);
        } catch (Exception e) {
            add(new Label(e.getMessage()));
        }
    }

    private void showHome() {
        this.setVisible(false);
        dashboardPage.setVisible(true);
    }


    class Close extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            showHome();
        }

    }

    private void closeFrame(Room room) {
        new UpdateRoomPage(out, in, room,this);
        this.setVisible(false);
    }

    class RoomPanel extends Panel {
        Button book, delete;
        Room room;

        public RoomPanel(Room r) {
            this.setBackground(Color.lightGray);
            this.setSize(100, 100);
            room = r;
            this.setLayout(new BorderLayout(10, 10));
            Panel top = new Panel(new GridLayout(5, 1, 5, 5));
            top.add(new Label("Type:" + r.type));
            top.add(new Label("No. of bed=" + r.bed));
            top.add(new Label("Price=" + r.price));
            top.add(new Label(r.description));
            top.add(new Label(r.amenities));
            this.add(new Label("Room Number=" + r.number), BorderLayout.WEST);
            this.add(top, BorderLayout.CENTER);
            book = new Button("Update");
            delete = new Button("Delete");
            delete.setBackground(Color.red);
            Panel btn = new Panel(new GridLayout(2, 1, 10, 10));
            btn.add(book);
            btn.add(delete);
            delete.addActionListener(new Listener());
            book.addActionListener(new Listener());
            this.add(btn, BorderLayout.EAST);
        }

        private void showUpdateRoom() {
            closeFrame(room);
        }

        private void deleteRoom() {
            try {
                out.writeObject(new DeleteRoom(room.number));
                DeleteRoom response = (DeleteRoom) in.readObject();
                if (response.isSuccess()) {
                    book.setEnabled(false);
                    delete.setEnabled(false);
                    delete.setLabel("Deleted");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        private class Listener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == book)
                    showUpdateRoom();
                else {
                    deleteRoom();
                }
            }
        }
    }
}

