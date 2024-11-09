package Admin;

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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class RequestForRegister extends Frame {
    Button confirm[], reject[];
    String users[];
    ObjectInputStream in;
    ObjectOutputStream out;
    Statement st;
    ResultSet rs;
    AdminPage admin;
    ArrayList<Register> requests;
    Panel reqPanel;
    Listener listener;
    int countReq;

    RequestForRegister(AdminPage admin, ObjectInputStream in, ObjectOutputStream out) {
        listener = new Listener();
        this.setTitle("Request for Register");
        this.addWindowListener(new CloseComp());
        this.setLayout(new BorderLayout(10, 10));
        this.admin = admin;
        this.out = out;
        this.in = in;
        Panel headings = new Panel(new GridLayout(1, 7, 10, 10));
        headings.add(new Label("User Id"));
        headings.add(new Label("Name"));
        headings.add(new Label("Email"));
        headings.add(new Label("Phone"));
        headings.add(new Label("Address"));
        headings.add(new Label("Confirm"));
        headings.add(new Label("Reject"));
        showData();
        this.add(headings, BorderLayout.NORTH);
        this.setVisible(true);
        this.setBounds(500,100,600, 200 + 20 * countReq);
    }

    private void showData() {
        try {
            out.writeObject(new RegisterRequests());
            RegisterRequests r = (RegisterRequests) in.readObject();
            if (!r.isSuccess()) {
                reqPanel = new Panel(new GridLayout(1, 1));
                reqPanel.add(new Label(r.getMessage()));
            } else {

                countReq = r.getRequests().size();
                requests = r.getRequests();
                confirm = new Button[countReq];
                reject = new Button[countReq];
                users = new String[countReq];
                reqPanel = new Panel(new GridLayout(countReq, 6, 5, 5));
                int i = 0;
                for (Register ele : requests) {
                    users[i] = ele.getId();
                    reqPanel.add(new Label(ele.getId()));
                    reqPanel.add(new Label(ele.getName()));
                    reqPanel.add(new Label(ele.getEmail()));
                    reqPanel.add(new Label(ele.getPhone()));
                    reqPanel.add(new Label(ele.getAddress()));
                    confirm[i] = new Button("Confirm");
                    confirm[i].addActionListener(listener);
                    reject[i] = new Button("Reject");
                    reject[i].addActionListener(listener);
                    reqPanel.add(confirm[i]);
                    reqPanel.add(reject[i]);
                    i++;
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

    private void registerUser(int i, boolean confirmedRegister) {
        try {
            out.writeObject(new ConfirmReject(users[i], confirmedRegister));
            ConfirmReject res = (ConfirmReject) in.readObject();
            if (res.isSuccess()) {
                if (confirmedRegister)
                    confirm[i].setBackground(Color.green);
                else
                    reject[i].setBackground(Color.red);
                confirm[i].setEnabled(false);
                reject[i].setEnabled(false);
            }

        } catch (Exception e) {

        }
    }

    class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int i = 0;
            boolean confirmed = false;
            for (i = 0; i < countReq; i++) {
                if (e.getSource() == confirm[i]) {
                    confirmed = true;
                    break;
                }
                if (e.getSource() == reject[i])
                    break;
            }
            registerUser(i, confirmed);
        }
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
