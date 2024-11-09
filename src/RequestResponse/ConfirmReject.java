package RequestResponse;

import java.io.Serializable;

public class ConfirmReject extends Request implements Serializable {
    private String guestId;
    private boolean confirmed;
    public ConfirmReject(String g,boolean t){
        super("registration-approval");
        guestId=g;
        confirmed=t;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    public boolean getConfirmed() {
        return confirmed;
    }

}