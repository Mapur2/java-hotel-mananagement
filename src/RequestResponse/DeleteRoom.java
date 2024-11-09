package RequestResponse;

import java.io.Serializable;

public class DeleteRoom extends Request implements Serializable {
    private String number;
    public DeleteRoom(String n){
        super("delete-room");
        number=n;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
