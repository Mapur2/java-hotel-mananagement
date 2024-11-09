package RequestResponse;
import java.util.ArrayList;

public class RoomQuery extends Request{
    private String from,to;
    private int children,adult;

    public int getChildren() {
        return children;
    }

    public int getAdult() {
        return adult;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
    public ArrayList<Room> rooms;
    public RoomQuery(String r, String  from, String to, int children, int adult) {
        super(r);
        this.from = from;
        this.to = to;
        this.children = children;
        this.adult = adult;
    }
}
