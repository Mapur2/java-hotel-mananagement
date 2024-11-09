package RequestResponse;
import java.io.Serializable;
import java.util.ArrayList;

public class RoomsBook extends Request implements Serializable {
    private ArrayList<String> roomNumbers;
    private String guest_id,from,to;
    private int price;
    public RoomsBook(ArrayList<String> roomNumbers, String guest_id, String from, String to) {
        super("roomsbook");
        this.roomNumbers = roomNumbers;
        this.guest_id = guest_id;
        this.from = from;
        this.to=to;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ArrayList<String> getRoomNumbers() {
        return roomNumbers;
    }

    public String getGuest_id() {
        return guest_id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}