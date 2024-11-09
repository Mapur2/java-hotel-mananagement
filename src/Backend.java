import java.io.*;
import java.net.*;
import java.sql.*;
import RequestResponse.*;
public class Backend implements Runnable {
    int clientport, serverport;
    ServerSocket serverSocket = null;
    Socket client = null, server = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    Connection con;
    RoomController roomController;
    UserController userController;
    Thread t;
    Backend() {
        clientport = 5173;
        serverport = 8000;
        t=new Thread(this);
        t.start();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "rupam123");
            System.out.println(con);

        } catch (Exception ex) {
            System.out.println("Error : " + ex);
        }
    }

    public static void main(String[] args) {
        new Backend();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverport);

            client = serverSocket.accept();
            System.out.println("Reference of "+client);
            server = new Socket(InetAddress.getByName("localhost"), clientport);
            in = new ObjectInputStream(server.getInputStream());
            out = new ObjectOutputStream(client.getOutputStream());
            sendFeaturesData();
            connect();
            //POJO
        } catch (Exception e) {
            System.out.println("Error:"+e.getMessage());
        }
    }
    /**
     * Main loop in which backend is running
     */
    public void connect() {

        userController = new UserController(con);
        roomController = new RoomController(con);
        while (true) {
            if (in == null || out==null)
                continue;
            Request request = null;
            try {
                request = (Request) in.readObject();
                if (request.getRequest().equals("request-for-register")) {//user requesting for register
                    out.writeObject(userController.registerUser((Register) request,"register_request"));
                }
                else if (request.getRequest().equals("registration-approval")) {
                    out.writeObject(userController.confirmRegister((ConfirmReject) request));
                }
                else if (request.getRequest().equals("login-admin")) {
                    out.writeObject(userController.loginUser((Login) request,"admin"));
                }
                else if (request.getRequest().equals("login")) {
                    out.writeObject(userController.loginUser((Login) request,"guest"));
                }
                else if(request.getRequest().equals("register-requests")){//admin requesting for register requests list
                    out.writeObject(userController.sendRegisterRequestsList((RegisterRequests) request));
                }
                else if (request.getRequest().equals("findroom")) {
                    out.writeObject(roomController.findRoom((RoomQuery) request));

                }
                else if (request.getRequest().equals("all-rooms")) {
                    out.writeObject(roomController.getAllRooms((AllRooms)request));

                }
                else if (request.getRequest().equals("roomsbook")) {
                    out.writeObject(roomController.bookRoomsForGuest((RoomsBook) request));
                }
                else if (request.getRequest().equals("booked-rooms")) {
                    out.writeObject(roomController.confirmedRoomsForGuest((BookedRoomQuery) request,"confirmed"));
                }
                else if (request.getRequest().equals("previous-booked-rooms")) {
                    out.writeObject(roomController.confirmedRoomsForGuest((BookedRoomQuery) request,"previous"));
                }
                else if (request.getRequest().equals("update-room")) {
                    out.writeObject(roomController.updateRoom((UpdateRoom) request));
                }
                else if (request.getRequest().equals("all-current-bookings")) {
                    out.writeObject(roomController.allCurrentBookings((BookedRoomQuery) request));
                }
                else if(request.getRequest().equals("delete-room"))
                {
                    out.writeObject(roomController.deleteRoom((DeleteRoom) request));
                }
                else if (request.getRequest().equals("cancelbooking")) {
                    out.writeObject(roomController.cancelBooking((CancelBookingQuery) request));
                }
                else if (request.getRequest().equals("add-room")) {
                    out.writeObject(roomController.addNewRoom((AddNewRoom) request));
                }
                else if(request.getRequest().equals("users-list")){
                    out.writeObject(userController.allUsersList((AllUsersList) request));
                }
                else {
                    System.out.println("Unrecognised request");
                    request.setMessage("Unrecognised request " + request.getRequest());
                    request.setSuccess(false);
                    out.writeObject(request);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
        }
    }

    void sendFeaturesData() {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from features");
            //System.out.println(rs);
            int n = count(rs);
            System.out.println(n);
            String[][] r = new String[n][2];
            st = con.createStatement();
            rs = st.executeQuery("select * from features");
            int i = 0;
            while (rs.next()) {
                r[i][0] = rs.getString(2);
                r[i][1] = rs.getString(3);
                i++;
            }
            Features f = new Features(r, "Facility");
            out.writeObject(f);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int count(ResultSet rs) {
        int n = 0;
        try {
            while (rs.next()) {
                n++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return n;
    }

    void endConnection() {

        try {

            out.close();
            in.close();
            client.close();
            server.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
