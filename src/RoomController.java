import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import RequestResponse.*;
public class RoomController {
    Connection con;
    RoomController(Connection con){
        this.con=con;
    }
    public RoomQuery findRoom(RoomQuery roomQuery){
        ArrayList<Room> rooms= new ArrayList<>();

        try {
            Statement st=con.createStatement();
//            ResultSet rs= st.executeQuery("select * from room where number not in (select room_number from booking where book_from<='"+roomQuery.getFrom()+"' and book_to>='"+roomQuery.getTo()+"')");
           //select * from room where number not in (select room_number from booking where (book_from<="2024-08-10" and book_to>="2024-08-11") or
            //(book_from>="2024-08-10" and book_to<="2024-08-10" ) or (book_from>="2024-08-11" and book_to<="2024-08-11" ));
            String range="(book_from>='"+roomQuery.getFrom()+"' and book_to<='"+roomQuery.getTo()+"')";
            String from="(book_from>='"+roomQuery.getFrom()+"' and book_to<='"+roomQuery.getFrom()+"')";
            String to="(book_from>='"+roomQuery.getTo()+"' and book_to<='"+roomQuery.getTo()+"')";
            ResultSet rs= st.executeQuery("select * from room where number not in (select room_number from booking where "+range+" or "+from+" or "+to+")");

            while(rs.next()){
                rooms.add(new Room(rs.getInt(1),
                        rs.getString(2),rs.getString(3),rs.getInt(4)
                        ,rs.getInt(5)
                        ,rs.getInt(6),rs.getString(7),rs.getString(8)));

            }
            roomQuery.rooms=rooms;
            roomQuery.setSuccess(true);
            roomQuery.setMessage("Rooms retrieved");
        }
        catch (Exception e){
            roomQuery.setSuccess(false);
            roomQuery.setMessage("Could not retrieve rooms");
        }
        return  roomQuery;
    }
    public UpdateRoom updateRoom(UpdateRoom updateRoom){
        try {
            Room room=updateRoom.getRoom();
            Statement st=con.createStatement();
            st.executeUpdate("update room set bed="+room.bed+", price="+room.price+", floor_number="+room.floor_number+
                    ", description='"+room.description+"', amenities='"+room.amenities+"', type='"+room.type+"' where number='"+room.number+"';");
            updateRoom.setMessage("Updated room successfully");
            updateRoom.setSuccess(true);
        }
        catch (Exception e){
            updateRoom.setSuccess(false);
            updateRoom.setMessage("Could not update room");
        }
        return  updateRoom;
    }
    public DeleteRoom deleteRoom(DeleteRoom room){
        try {
            Statement st=con.createStatement();
            st.execute("delete from room where number='"+room.getNumber()+"'");
            st.execute("commit");
            room.setSuccess(true);
            room.setMessage("Deleted room");
        }
        catch (Exception e){
            System.out.println(e);
            room.setSuccess(false);
            room.setMessage("Something went wrong");
        }
        return room;
    }
    public AllRooms getAllRooms(AllRooms r){
        try {
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select * from room");
            ArrayList<Room> rooms=new ArrayList<>();
            while(rs.next()){
                rooms.add(new Room(rs.getInt(1),
                        rs.getString(2),rs.getString(3),rs.getInt(4)
                        ,rs.getInt(5)
                        ,rs.getInt(6),rs.getString(7),rs.getString(8)));

            }
            if(rooms.isEmpty())
            {
                r.setSuccess(false);
                r.setMessage("No rooms found");
                return r;
            }
            r.setRooms(rooms);
            r.setMessage("All rooms retrieved");
            r.setSuccess(true);
        } catch (SQLException e) {
            r.setSuccess(false);
            r.setMessage("Could not retrieve rooms");
        }
        return r;
    }
    public RoomsBook bookRoomsForGuest(RoomsBook roomsBook){
        try{
            Statement st=con.createStatement();
            ArrayList<String> l=roomsBook.getRoomNumbers();
            int n=l.size();
            for(int i=0;i<n;i++)
            {
                st.execute("insert into booking (room_number,guest_id,book_from,book_to,total_price) value("+l.get(i)+",'"+roomsBook.getGuest_id()+"','"+roomsBook.getFrom()+
                        "','"+roomsBook.getTo()+"',"+"((book_to-book_from)+1)*(select price from room where number='"+l.get(i)+"'))");

            }
            ResultSet rs=st.executeQuery("select total_price from booking where guest_id='"+roomsBook.getGuest_id()+"' and book_from='"+roomsBook.getFrom()+"' and book_to='"+ roomsBook.getTo()+"';");
            int price=0;
            while(rs.next()){
                price+=rs.getInt(1);
            }
            roomsBook.setPrice(price);
            roomsBook.setSuccess(true);
            roomsBook.setMessage("Booked all rooms");
        }
        catch (Exception e){
            roomsBook.setSuccess(false);
            roomsBook.setMessage("Could not book rooms");
            System.out.println(e);
        }
        return roomsBook;
    }
    public BookedRoomQuery confirmedRoomsForGuest(BookedRoomQuery br,String type){
        try {
            Statement st=con.createStatement();
            String currDate=(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            ResultSet rs= st.executeQuery("select * from booking where guest_id='"+br.id+"' and book_from"+
                    (type.equals("confirmed")?" >= '":" < '")+currDate+"';");
            ArrayList<BookedRoom> rooms= new ArrayList<>();
            while(rs.next()){
                rooms.add(new BookedRoom(rs.getInt(1),rs.getString(2),rs.getInt(6),rs.getString(4)
                        ,rs.getString(5)));
            }
            br.setBookedRoomList(rooms);
            br.setSuccess(true);
            br.setMessage("Rooms retrieved");
        }
        catch (Exception e){
            System.out.println(e);
            br.setSuccess(false);
            br.setMessage("Could not Retrieved confirmed rooms");
        }
        return br;
    }
    public CancelBookingQuery cancelBooking(CancelBookingQuery cBQ){
        try {
            Statement st=con.createStatement();
            st.execute("delete from booking where id="+cBQ.getId()+";");
            st.execute("commit");
            cBQ.setSuccess(true);
            cBQ.setMessage("Cancelled Booking BN-HT-"+cBQ.getId());
        }
        catch (Exception e){
            System.out.println(e);
            cBQ.setSuccess(false);
            cBQ.setMessage("Could not Retrieved confirmed rooms");
        }
        return cBQ;
    }
    public AddNewRoom addNewRoom(AddNewRoom ad){
        try {
            Room room=ad.getRoom();
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select * from room where number='"+room.number+"'");
            if(rs.next())
            {
                ad.setSuccess(false);
                ad.setMessage("Room number already present");
                return ad;
            }
            st.executeUpdate("INSERT INTO room (number, type, bed, price, floor_number, description, amenities) " +
                    "VALUES('"+room.number+"','"+room.type+"',"+room.bed+","+room.price+","+room.floor_number+
                    ",'"+room.description+"','"+room.amenities+"')");
            ad.setSuccess(true);
            ad.setMessage("Added room (Room Number-"+ad.getRoom().number+")");
        }
        catch (Exception e){
            System.out.println(e);
            ad.setMessage("Something went wrong");
            ad.setSuccess(false);
        }
        return ad;
    }
    public  BookedRoomQuery allCurrentBookings(BookedRoomQuery br){
        try {
            Statement st=con.createStatement();
            ResultSet rs= st.executeQuery("select * from booking b where curdate()<=b.book_from");
            ArrayList<BookedRoom> rooms= new ArrayList<>();
            while(rs.next()){
                rooms.add(new BookedRoom(rs.getInt(1),rs.getString(2),rs.getInt(6),rs.getString(4)
                        ,rs.getString(5)));
            }
            br.setBookedRoomList(rooms);
            br.setSuccess(true);
            br.setMessage("Rooms retrieved");
        }
        catch (Exception e){
            System.out.println(e);
            br.setSuccess(false);
            br.setMessage("Could not Retrieved confirmed rooms");
        }
        return br;
    }
}
