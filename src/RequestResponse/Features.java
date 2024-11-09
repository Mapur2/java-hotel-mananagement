package RequestResponse;
import java.io.Serializable;

public class Features  implements Serializable{
    private String res[][];
    public Features(String res[][], String req){
        this.res=res;
    }

    public void setRes(String[][] res) {
        this.res = res;
    }

    public String[][] getRes() {
        return res;
    }
}
