package RequestResponse;
import java.io.Serializable;

public class Register extends Request implements Serializable {
    private String id,pwd,name,email,phone,address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Register(String request, String i, String ps, String n, String e, String ph, String ad) {
          super(request);
        id=i;
        pwd=ps;
        name=n;
        email=e;
        phone = ph;
        address = ad;
    }


    public String getPwd() {
        return pwd;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
