package RequestResponse;

import java.io.Serializable;

public class AddNewRoom extends Request implements Serializable {
    private Room room;
    public AddNewRoom( String number, String type, int bed, int price, int floor_number, String description, String amenities){
        super("add-room");
        room=new Room(-1,number,type,bed,price,floor_number,description,amenities);
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
