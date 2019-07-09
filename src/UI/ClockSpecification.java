package UI;

import Shape.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ClockSpecification extends JDialog{
    InstantGraph instantGraph = null;
    public ClockSpecification(Rect domain, Clock clock, int index){
        setSize(600, 800);
        setLocation((int) getWidth() / 2, (int) getHeight() / 2);
        setTitle("ClockSpecification");
        try{
            instantGraph = new InstantGraph(domain, clock,index);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        repaint();
        this.setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        System.out.println("paint");
        super.paint(g);
        try{
            instantGraph.draw(g, true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
