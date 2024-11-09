package RequestResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class RegisterRequests extends Request implements Serializable {
    private ArrayList<Register> requests ;
    public RegisterRequests(){
        super("register-requests");

    }

    public ArrayList<Register> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Register> requests) {
        this.requests = requests;
    }
}
