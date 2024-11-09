package RequestResponse;
import java.io.Serializable;
public class Request implements Serializable {
    protected String request;
    protected boolean success;
    protected String message;
    public Request(String r){
        request=r;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
