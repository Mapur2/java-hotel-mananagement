package Admin;

import RequestResponse.AllUsersList;
import RequestResponse.ConfirmReject;
import RequestResponse.Register;
import RequestResponse.RegisterRequests;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AllUsers extends Frame {
    ObjectInputStream in;
    ObjectOutputStream out;
    Statement st;
    ResultSet rs;
    AdminPage admin;
    ArrayList<Register> requests;
    Panel reqPanel;
    int countReq;

    AllUsers(AdminPage admin, ObjectInputStream in, ObjectOutputStream out) {
        this.addWindowListener(new CloseComp());
        this.setTitle("All users");
        this.setLayout(new BorderLayout(10, 10));
        this.admin = admin;
        this.out = out;
        this.in = in;
        Panel headings = new Panel(new GridLayout(1, 5, 10, 10));
        headings.add(new Label("User Id"));
        headings.add(new Label("Name"));
        headings.add(new Label("Email"));
        headings.add(new Label("Phone"));
        headings.add(new Label("Address"));
        showData();
        this.add(headings, BorderLayout.NORTH);
        this.setVisible(true);
        this.setBounds(500,100,700, 200 + 20 * countReq);
    }

    private void showData() {
        try {
            out.writeObject(new AllUsersList("users-list"));
            AllUsersList r = (AllUsersList) in.readObject();
            if (!r.isSuccess()) {
                reqPanel = new Panel(new GridLayout(1, 1));
                reqPanel.add(new Label(r.getMessage()));
            } else {

                countReq = r.getUsers().size();
                requests = r.getUsers();
                reqPanel = new Panel(new GridLayout(countReq, 5, 5, 5));
                for (Register ele : requests) {
                    reqPanel.add(new Label(ele.getId()));
                    reqPanel.add(new Label(ele.getName()));
                    reqPanel.add(new Label(ele.getEmail()));
                    reqPanel.add(new Label(ele.getPhone()));
                    reqPanel.add(new Label(ele.getAddress()));
                }

            }
            this.add(reqPanel, BorderLayout.CENTER);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Insets getInsets() {
        return new Insets(50, 20, 20, 20);
    }

    void closeRegisterRequest() {
        this.setVisible(false);
        admin.setVisible(true);
    }

    class CloseComp extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            closeRegisterRequest();
        }

    }
}
