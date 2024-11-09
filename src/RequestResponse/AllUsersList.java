package RequestResponse;


import java.io.Serializable;
import java.util.ArrayList;

public class AllUsersList extends Request implements Serializable {
    private ArrayList<Register> users;
    public AllUsersList(String request){
        super(request);
    }

    public ArrayList<Register> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Register> users) {
        this.users = users;
    }
}
