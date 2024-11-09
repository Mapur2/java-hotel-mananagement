package Admin;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import RequestResponse.*;
public class AddRoom extends Frame {
    TextField number, bed, price, floor_number;
    TextArea description, amenities;
    Choice type;
    Button addBtn,back;
    Panel panel;
    ObjectOutputStream out;
    ObjectInputStream in;
    AdminPage admin;
    Label msg;
    public AddRoom(AdminPage ad, ObjectOutputStream out, ObjectInputStream in){
        this.admin=ad;
        this.out=out;
        this.in=in;
        this.setTitle("Add New Room");
        this.setLayout(new BorderLayout(10,10));
        panel=new Panel(new GridLayout(9,2,10,10));

        number=new TextField();

        type=new Choice();
        type.add("Single Bed");
        type.add("Double Bed");
        type.add("Triple Bed");
        type.add("Deluxe");
        type.select(0);

        bed=new TextField();
        price=new TextField();
        floor_number=new TextField();
        description=new TextArea(4,40);
        amenities=new TextArea(4,40);

        Listener listener=new Listener();
        addBtn=new Button("Add Room");
        addBtn.addActionListener(listener);
        back=new Button("Back");
        back.addActionListener(listener);
        addBtn.setBackground(Color.cyan);

        panel.add(new Label("Number"));
        panel.add(number);
        panel.add(new Label("Type of Room"));
        panel.add(type);
        panel.add(new Label("No. of Beds"));
        panel.add(bed);
        panel.add(new Label("Price"));
        panel.add(price);
        panel.add(new Label("Floor Number"));
        panel.add(floor_number);
        panel.add(new Label("Description"));
        panel.add(description);
        panel.add(new Label("Amenities"));
        panel.add(amenities);


        panel.add(addBtn);
        panel.add(back);

        msg=new Label();
        msg.setBackground(Color.lightGray);
        this.add(msg,BorderLayout.SOUTH);

        this.add(panel,BorderLayout.CENTER);

        setBounds(500,100,500,500);
        this.setVisible(true);
        this.addWindowListener(new Close());
    }

    public Insets getInsets(){
        return new Insets(50,20,20,20);
    }

    private void addNewRoom(){
        try {
            String newNumber,newDescription, newAmenities;
            int newBed,newPrice,newFloor;
            newNumber=number.getText().trim();
            newBed=Integer.parseInt(bed.getText().trim());
            newPrice=Integer.parseInt(price.getText().trim());
            newFloor=Integer.parseInt(floor_number.getText().trim());
            newDescription=description.getText().trim();
            newAmenities=amenities.getText().trim();
            if(newNumber.isEmpty() || newDescription.isEmpty() || newAmenities.isEmpty())
                throw new RegisterException("Empty Field(s)");

            AddNewRoom room =new AddNewRoom(newNumber,type.getSelectedItem(),newBed,newPrice,newFloor,newDescription,newAmenities);
            out.writeObject(room);
            AddNewRoom response=(AddNewRoom) in.readObject();

            if(!response.isSuccess())
                throw new RegisterException(response.getMessage());
            else {
                msg.setText(response.getMessage());
            }
        }
        catch (RegisterException e){
            msg.setText(e.toString());
        }
        catch (Exception e){
            msg.setText("Something went wrong");
            System.out.println(e);
        }
    }

    private class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==back){
                closeRegister();
            }
            else if (e.getSource()==addBtn) {
                addNewRoom();
            }
        }
    }
    public void closeRegister(){
        admin.setVisible(true);
        this.setVisible(false);
    }
    class RegisterException extends Exception{
        String msg;
        RegisterException(String m){
            msg=m;
        }
        public String toString(){
            return  msg;
        }
    }
    class Close extends WindowAdapter {
        public void windowClosing(WindowEvent e){
            closeRegister();
        }
    }
}
