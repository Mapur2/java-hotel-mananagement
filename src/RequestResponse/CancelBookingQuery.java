package RequestResponse;
public class CancelBookingQuery extends Request{
    private int id;
    public CancelBookingQuery(int id) {
        super("cancelbooking");
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
