package RequestResponse;
import java.io.Serializable;

public class BookedRoom implements Serializable {
    private int id;
    private String number;
    private int price;
    private String from,to;

    public BookedRoom(int id,String number, int price,String from, String to) {
        this.id=id;
        this.number = number;
        this.price = price;
        this.from = from;
        this.to = to;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public int getPrice() {
        return price;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
