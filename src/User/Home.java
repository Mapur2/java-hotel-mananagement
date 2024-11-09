package User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.lang.reflect.Method;
import java.net.*;
import RequestResponse.*;
public class Home extends Frame implements Runnable{
    int clientport, serverport;
    ServerSocket clientServer = null;
    Socket client = null, server = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    Panel hero;
    Panel info;
    Label load;
    Button login,register;
    RegisterPage registerPage;
    LoginPage loginPage;
    String data[][];
    Thread t;
    public Home(){
        setBounds(500,100,500,500);
//        Image icon = Toolkit.getDefaultToolkit().getImage("./icon.jpg");
//        this.setIconImage(icon);
        setVisible(true);
        registerPage=null;
        loginPage=null;
        clientport = 5173;
        serverport = 8000;
        this.addWindowListener(new Close());
        this.setLayout(new BorderLayout(10,10));
        hero = new Panel(new BorderLayout(10,10));
        info=new Panel(new BorderLayout(10,10));
        Label welcome =new Label("Welcome to Hotel Raj");
        welcome.setAlignment(Label.CENTER);

        login=new Button("Login");
        login.addActionListener(new Listener());
        register=new Button("Register");
        register.addActionListener(new Listener());
        Panel loginPanel=new Panel(new GridLayout(2,2,5,5));
        loginPanel.add(new Label("Interested in knowing more then register or login"));
        loginPanel.add(new Label());
        loginPanel.add(login);
        loginPanel.add(register);
        this.add(loginPanel,BorderLayout.SOUTH);

        this.add(welcome,BorderLayout.NORTH);
        this.add(hero,BorderLayout.CENTER);
        t=new Thread(this);
        t.start();
        this.setSize(500,500);

    }
    private void showHeroData(){
        hero.add(info,BorderLayout.CENTER);
        int n=data.length;
        Panel features=new Panel(new GridLayout(n,2,2,2));
        features.add(new Label("Have a look on our key features"));
        features.add(new Label());
        info.add(new Label(),BorderLayout.EAST);
        info.add(new Label(),BorderLayout.WEST);
        info.add(new Label(data[0][1]),BorderLayout.NORTH);
        for (int i = 1; i < n; i++) {
            features.add(new Label(data[i][0]));
            features.add(new Label(data[i][1]));
        }
        info.add(features,BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new Home();
    }

    @Override
    public Insets getInsets() {
        return new Insets(50,20,20,20);
    }

    public void run() {
        try {
            client = new Socket(InetAddress.getByName("localhost"), serverport);
            clientServer = new ServerSocket(clientport);
            server = clientServer.accept();
            System.out.println("Reference of "+server);
            out = new ObjectOutputStream(server.getOutputStream());
            in = new ObjectInputStream(client.getInputStream());
            Features f= (Features) in.readObject();
            data = f.getRes();
            showHeroData();
            this.setVisible(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public void registerUser()
    {
        new RegisterPage(this,out,in);
        this.setVisible(false);
    }
    public void sendLogin()
    {
        if(loginPage==null)
            loginPage=new LoginPage(this,out,in);
        else
            loginPage.setVisible(true);
        this.setVisible(false);
    }
    class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(register==e.getSource())
            {
                registerUser();
            }
            else if(login == e.getSource())
            {
                sendLogin();
            }
        }
    }
    class Close extends WindowAdapter{
        public void windowClosing(WindowEvent e){
            System.exit(0);
        }
    }
}
