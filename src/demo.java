import javax.swing.*;
import java.awt.*;

public class demo extends JFrame{
    public demo(){

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(20,20));
        for(int i=0;i<20;i++)
            for(int j=0;j<20;j++){
                p.add(new Button("Button "+(i+1)+" "+(j+1)));
            }
        JScrollPane pane1=new JScrollPane(p);
        this.setLayout(new BorderLayout());
        this.add(pane1,BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        demo obj=new demo();
        obj.setSize(150,150);
        obj.setVisible(true);
    }
}
