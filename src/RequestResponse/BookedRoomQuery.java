package RequestResponse;
import java.util.ArrayList;

public class BookedRoomQuery extends Request{
    private ArrayList<BookedRoom> bookedRoomList;
    public String id;
    public BookedRoomQuery(String id,String request) {
        super(request);
        this.id=id;
    }
    public BookedRoomQuery(String request){
        super(request);
    }
    public void setBookedRoomList(ArrayList<BookedRoom> bookedRoomList) {
        this.bookedRoomList = bookedRoomList;
    }

    public ArrayList<BookedRoom> getBookedRoomList() {
        return bookedRoomList;
    }
}
