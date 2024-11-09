package Admin;
import java.awt.*;
import java.awt.event.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import RequestResponse.*;
public class UpdateRoomPage extends Frame {
    TextField bed, price, floor_number;
    TextArea description, amenities;
    Choice type;
    Button addBtn,back;
    Panel panel;
    ObjectOutputStream out;
    ObjectInputStream in;
    Label msg;
    Room room;
    AllRoomsPage allRoomsPage;
    public UpdateRoomPage(ObjectOutputStream out, ObjectInputStream in, Room room, AllRoomsPage allRoomsPage){
        this.allRoomsPage=allRoomsPage;
        this.room=room;
        this.out=out;
        this.in=in;
        this.setTitle("Update Room");
        this.setLayout(new BorderLayout(10,10));
        panel=new Panel(new GridLayout(9,2,10,10));
        type=new Choice();
        type.add("Single Bed");
        type.add("Double Bed");
        type.add("Triple Bed");
        type.add("Deluxe");
        type.select(room.type);

        bed=new TextField(""+room.bed);
        price=new TextField(room.price+"");
        floor_number=new TextField(room.floor_number+"");
        description=new TextArea(4,40);
        description.setText(room.description);
        amenities=new TextArea(4,40);
        amenities.setText(room.amenities);
        Listener listener=new Listener();

        addBtn=new Button("Update Room");
        addBtn.addActionListener(listener);
        back=new Button("Back");
        back.addActionListener(listener);
        addBtn.setBackground(Color.cyan);

        panel.add(new Label("Number"));
        panel.add(new Label(room.number));
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
    private void updateRoom(){
        try {
            String newNumber,newDescription, newAmenities;
            int newBed,newPrice,newFloor;
            newBed=Integer.parseInt(bed.getText().trim());
            newPrice=Integer.parseInt(price.getText().trim());
            newFloor=Integer.parseInt(floor_number.getText().trim());
            newDescription=description.getText().trim();
            newAmenities=amenities.getText().trim();
            if(newDescription.isEmpty() || newAmenities.isEmpty())
                throw new RegisterException("Empty Field(s)");
            if (room.bed==newBed && room.type.equals(type.getSelectedItem()) && room.price==newPrice && room.amenities.equals(newAmenities)
                    && room.description.equals(newDescription) && room.floor_number==newFloor){
                throw  new RegisterException("Nothing is changed. Please update something");
            }
            Room updateRoom = new Room(-1,room.number,type.getSelectedItem(),newBed,newPrice,newFloor,newDescription,newAmenities);
            out.writeObject(new UpdateRoom(updateRoom));
            UpdateRoom response=(UpdateRoom) in.readObject();
            if(!response.isSuccess())
                throw new RegisterException(response.getMessage());
            else {
                room=response.getRoom();
                updateFields();

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
    public void updateFields(){
        bed.setText(room.bed+"");
        type.select(room.type);
        floor_number.setText(room.floor_number+"");
        price.setText(room.price+"");
        description.setText(room.description);
        amenities.setText(room.amenities);
    }
    private class Listener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==back){
                closeRegister();
            }
            else if (e.getSource()==addBtn) {
                updateRoom();
            }
        }
    }
    public void closeRegister(){
        allRoomsPage.updateRoomsPanel();
        allRoomsPage.setVisible(true);
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
