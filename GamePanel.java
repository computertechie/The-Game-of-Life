import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel{
	boolean alive = false;

	public GamePanel(GameFrame frame){
		setBackground(Color.black);
		addMouseListener(frame);
		Dimension dim = new Dimension(10,10);
		setPreferredSize(dim);
	}

	public boolean isAlive(){
		return alive;
	}

	public void setAlive(){
		alive = true;
		setBackground(Color.blue);
	}

	public void setDead(){
		alive = false;
		setBackground(Color.black);
	}

	public void updateBackground(){
		if(alive){
			setBackground(Color.blue);
		}
		else
			setBackground(Color.black);
	}

	public void switchPanel(){
		if(alive)
			setDead();

		else
			setAlive();
	}
}