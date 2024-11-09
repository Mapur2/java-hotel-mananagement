package RequestResponse;
import java.io.Serializable;

public class Login
        extends Request
        implements Serializable {
    private String id;
    private String pwd;
    private String name;
    private String type;

    public Login(String request, String i, String ps) {
        super(request);
        id=i;
        pwd=ps;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getId() {
        return id;
    }
    public String getPwd() {
        return pwd;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
