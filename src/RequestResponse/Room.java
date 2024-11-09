package RequestResponse;
import java.io.Serializable;

public class Room implements Serializable {
    public int id;
    public String number;
    public String type;
    public int bed;
    public int price;
    public int floor_number;
    public String description;
    public String amenities;

    public Room(int id, String number, String type, int bed, int price, int floor_number, String description, String amenities) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.bed = bed;
        this.price = price;
        this.floor_number = floor_number;
        this.description = description;
        this.amenities = amenities;
    }
}
