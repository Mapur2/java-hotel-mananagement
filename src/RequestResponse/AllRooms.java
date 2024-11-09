package RequestResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class AllRooms extends Request implements Serializable {
    private ArrayList<Room> rooms;
    public AllRooms(){
        super("all-rooms");

    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }
}
