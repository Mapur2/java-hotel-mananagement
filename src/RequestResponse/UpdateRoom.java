package RequestResponse;

import java.io.Serializable;

public class UpdateRoom extends Request implements Serializable {
    private Room room;
    public UpdateRoom(Room r){
        super("update-room");
        room=r;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
