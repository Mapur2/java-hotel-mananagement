import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

import RequestResponse.*;
public class UserController {
    Connection con;
    UserController(Connection c){
        con=c;
    }
    public RegisterRequests sendRegisterRequestsList(RegisterRequests obj){
        ArrayList<Register> req;
        try {
            Statement st=con.createStatement();
            req=new ArrayList<>();
            ResultSet rs=st.executeQuery("select * from register_request");
            while(rs.next())
            {
                req.add(new Register("",rs.getString(1),rs.getString(2),
                        rs.getString(3),rs.getString(4),rs.getString(5),
                        rs.getString(6)));
            }
            if(req.isEmpty())
            {
                obj.setMessage("No new request for register");
                obj.setSuccess(false);
                return obj;
            }
            obj.setRequests(req);
            obj.setMessage("Retrieved all requests");
            obj.setSuccess(true);
        }
        catch (Exception e){
            System.out.println(e);
            obj.setMessage("Something went wrong");
            obj.setSuccess(false);
        }
        return obj;
    }
    public Login loginUser(Login login,String type) throws IOException
    {
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select name,pwd,role from "+type+" where id='"+login.getId()+"';");
            if(rs.next())
            {

                //System.out.println(login.getPwd()+" "+rs.getString(2));
                if(isPasswordCorrect(login.getPwd(),rs.getString(2))) {
                    login.setSuccess(true);
                    login.setName(rs.getString(1));
                    login.setMessage("Logged in successfully");
                    login.setType(rs.getString(3));
                }
                else {
                    login.setSuccess(false);
                    login.setMessage("Wrong password");
                }
            }
            else {
                login.setSuccess(false);
                login.setMessage("Invalid id or password");
            }
        }
        catch (Exception e){
            System.out.println(e);
            login.setSuccess(false);
            login.setMessage("Something went wrong");
        }
        return login;
    }
    public Register registerUser(Register r, String requestType) throws IOException {
        try {
            Statement st = con.createStatement();
            ResultSet check=st.executeQuery("select name from guest where id='"+r.getId()+"';");
            if(check.next())
            {
                r.setMessage("Id is already present");
                r.setSuccess(false);
                return r;
            }
            check=st.executeQuery("select email from guest where email='"+r.getEmail()+"';");
            if(check.next())
            {
                r.setMessage("Email is already present");
                r.setSuccess(false);
                return r;
            }
            String p = encryptPassword(r.getPwd());
            st.executeUpdate("insert into "+requestType+"(id,pwd,name,email,phone,address) value('"+r.getId()+"','"+p+"','"+r.getName()+
                    "','"+r.getEmail()+"','"+r.getPhone()+"','"+ r.getAddress()+"')");
            r.setMessage("Applied for Registration");
            r.setSuccess(true);
        }
        catch (Exception e)
        {
            r.setMessage("Registration not success");
            r.setSuccess(false);
            System.out.println(e);
        }
        return r;
    }
    public ConfirmReject confirmRegister(ConfirmReject ob){
        try {
            Statement st=con.createStatement();
            if(ob.getConfirmed()) {
                st.executeUpdate("INSERT INTO guest(id,pwd,name,email,phone,address) SELECT * FROM register_request WHERE id='" + ob.getGuestId()+"'");
                ob.setMessage(ob.getGuestId()+" registration confirmed");
            }
            else
                ob.setMessage(ob.getGuestId()+" registration rejected");
            st.executeUpdate("DELETE FROM register_request WHERE id='"+ob.getGuestId()+"'");
            st.executeUpdate("commit");
            ob.setSuccess(true);
        }
        catch (Exception e){
            ob.setMessage("Registration not success");
            ob.setSuccess(false);
            System.out.println(e);
        }
        return ob;
    }
    public  AllUsersList allUsersList(AllUsersList al){
        try {
            Statement st=con.createStatement();
            ResultSet rs= st.executeQuery("select * from guest where role='user'");
            ArrayList<Register> users= new ArrayList<>();
            while(rs.next()){
                users.add(new Register("",rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)
                        ,rs.getString(5),rs.getString(6)));
            }
            al.setUsers(users);
            al.setSuccess(true);
            al.setMessage("All users retrieved");
        }
        catch (Exception e){
            System.out.println(e);
            al.setSuccess(false);
            al.setMessage("Could not Retrieved confirmed rooms");
        }
        return al;
    }

    private String encryptPassword(String s){
        int n=s.length();
        String w="";
        for (int i = 0; i < n; i++) {
            char ch=s.charAt(i);
            w=w+(char)(ch+32);
        }
        return w;
    }
    private boolean isPasswordCorrect(String userPass,String dbPass){
        int n=dbPass.length();
        String w="";
        for (int i = 0; i < n; i++) {
            char ch=dbPass.charAt(i);
            w=w+(char)(ch-32);
        }
        return w.equals(userPass);
    }
}
