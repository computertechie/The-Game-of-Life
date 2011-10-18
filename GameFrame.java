import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;

public class GameFrame extends JFrame implements MouseListener, Runnable, ActionListener{
	GamePanel[][] panels;
	GamePanel[][] nextPanels;
	int num = 0, counter =0,size;
	Thread t;
	JButton start = new JButton("Start"), pause = new JButton("Pause/Stop"), clear = new JButton("Clear");
	JPanel buttons = new JPanel(), grid = new JPanel();
	volatile boolean go = true, mouseDown, paused = false;
	public GameFrame(){
		size = Integer.parseInt(JOptionPane.showInputDialog("Input the size of the grid",null));
		panels = new GamePanel[size][size];
		nextPanels = new GamePanel[size][size];
		grid.setLayout(new GridLayout(size,size,2,2));
		buttons.add(start);
		buttons.add(pause);
		buttons.add(clear);
		start.addActionListener(this);
		pause.addActionListener(this);
		clear.addActionListener(this);
		getContentPane().add(grid,BorderLayout.CENTER);
		getContentPane().add(buttons, BorderLayout.NORTH);
		for(int i = 0; i<panels.length; i++){
			for(int j = 0; j<panels[0].length;j++){
				panels[i][j] = new GamePanel(this);
				nextPanels[i][j] = new GamePanel(this);
				grid.add(panels[i][j]);

				switch((int)(Math.random()*2+1)){
				case 1:
					panels[i][j].setAlive();
					break;

				case 2:
					panels[i][j].setDead();
					break;
				}
			}
		}
		t=new Thread(this);
		t.start();
		invalidate();
		validate();
	}

	public void mousePressed(MouseEvent e) {
		mouseDown = true;
		for(int i = 0; i<panels.length; i++){
			for(int j = 0; j<panels[0].length; j++){
				if(e.getSource() == panels[i][j])
					panels[i][j].switchPanel();

				else if(e.getSource() == nextPanels[i][j])
					nextPanels[i][j].switchPanel();
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
		if(paused)
			go = false;
		else
			go =true;
	}

	public void mouseEntered(MouseEvent e) {
		if(mouseDown){
			go = false;
			for(int i = 0; i<panels.length; i++){
				for(int j = 0; j<panels[0].length; j++){
					if(e.getSource() == panels[i][j])
						panels[i][j].switchPanel();

					else if(e.getSource() == nextPanels[i][j])
						nextPanels[i][j].switchPanel();
				}
			}
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		for(int i = 0; i<panels.length; i++){
			for(int j= 0; j<panels[0].length; j++){
				if(e.getSource() == panels[i][j]){
					panels[i][j].switchPanel();
				}
				else if(e.getSource() == nextPanels[i][j]){
					nextPanels[i][j].switchPanel();
				}
			}
		}
	}



	public void run(){
		ActionListener action =new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(go){
					checkSurround(0,0);
					counter++;
				}
			}
		};
		javax.swing.Timer time = new javax.swing.Timer(1000, action);
		time.setInitialDelay(1000);
		time.start();
	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource() == start){
			go = true;
			paused = false;
		}
		if(e.getSource() == pause){
			paused = true;
			go = false;
		}
		if(e.getSource() == clear){
			clearBoard();
		}
	}

	public void clearBoard(){
		for(int i = 0; i<panels.length; i++){
			for(int j = 0; j<panels[0].length; j++){
				nextPanels[i][j].setDead();
				panels[i][j].setDead();
			}
		}
	}

	public static void main(String[] args){
		GameFrame f = new GameFrame();
		f.setVisible(true);
		f.setBounds(0,0,100,100);
		f.setTitle("The Game of Life");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setExtendedState(MAXIMIZED_BOTH);
	}

	public void checkSurround(int row, int col){
		ArrayList<Boolean> neighbors = new ArrayList<Boolean>();

		if(counter % 2 == 0){
//interior
			if((col>0 && col<size-1)&&(row>0 && row<size-1)){
				for(int i = col-1;i<=col+1;i++){
					if(panels[row+1][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row-1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//first row interior
			else if((col>0 && col<panels.length-1) && row==0){
				for(int i = col-1; i<=col+1;i++){
					if(panels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row+1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//left column interior
			else if(col==0 && (row>0 && row<panels.length-1)){
				for(int i = col; i<=col+1; i++){
					if(panels[row+1][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row-1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//top left corner
			else if(col==0 && row==0){
				for(int i = col; i<=col+1; i++){
					if(panels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row+1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}

//bottom left corner
			else if(col == 0 && row == panels.length-1){
				for(int i = col; i<col+2; i++){
					if(panels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row-1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//top right corner
			else if(col == panels.length-1 && row == 0){
				for(int i = col-1; i<col+1;i++){
					if(panels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row+1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//bottom right corner
			else if(col == panels.length-1 && row == panels.length-1){
				for(int i = col-1; i<col+1; i++){
					if(panels[row-1][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//right column interior
			else if(col == panels.length-1 && (row>0 && row<=panels.length-1)){
				for(int i = col-1; i<col+1; i++){
					if(panels[row+1][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row-1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//bottom row interior
			else if((col>0 && col<panels.length-1) && row == panels.length-1){
				for(int i = col-1; i<col+2; i++){
					if(panels[row-1][i].isAlive()){
						neighbors.add(true);
					}
					if(panels[row][i].isAlive()){
						neighbors.add(true);
					}
				}
			}

			if(((neighbors.size() == 4) || (neighbors.size()==3)) && panels[row][col].isAlive()){
				nextPanels[row][col].setAlive();
			}
			else if((neighbors.size() == 4) && !panels[row][col].isAlive()){
				nextPanels[row][col].setDead();
			}
			else if((neighbors.size() ==5) && panels[row][col].isAlive()){
				nextPanels[row][col].setDead();
			}
			else if((neighbors.size() == 3) && !panels[row][col].isAlive()){
				nextPanels[row][col].setAlive();
			}

			col++;
			if(col==panels.length+1){
				col=0;
				row++;

				if(row<panels.length+1)
					checkSurround(row,col);

				else{
					row=0;
					for(int i = 0; i<panels.length; i++){
						for(int j = 0; j<panels[0].length; j++){
							grid.remove(panels[i][j]);
							grid.add(nextPanels[i][j]);
							panels[i][j] = new GamePanel(this);
						}
					}
					invalidate();
					validate();
				}
			}
			else
				checkSurround(row,col);
		}
		else{
//interior
			if((col>0 && col<size-1)&&(row>0 && row<size-1)){
				for(int i = col-1;i<=col+1;i++){
					if(nextPanels[row+1][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row-1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//first row interior
			else if((col>0 && col<nextPanels.length-1) && row==0){
				for(int i = col-1; i<=col+1;i++){
					if(nextPanels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row+1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//first column interior
			else if(col==0 && (row>0 && row<nextPanels.length-1)){
				for(int i = col; i<=col+1; i++){
					if(nextPanels[row+1][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row-1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//top left corner
			else if(col==0 && row==0){
				for(int i = col; i<=col+1; i++){
					if(nextPanels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row+1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}

//bottom left corner
			else if(col == 0 && row == nextPanels.length-1){
				for(int i = col; i<col+2; i++){
					if(nextPanels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row-1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//top right corner
			else if(col == nextPanels.length-1 && row == 0){
				for(int i = col-1; i<col+1;i++){
					if(nextPanels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row+1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//bottom right corner
			else if(col == nextPanels.length-1 && row == nextPanels.length-1){
				for(int i = col-1; i<col+1; i++){
					if(nextPanels[row-1][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//right column interior
			else if(col == nextPanels.length-1 && (row>0 && row<nextPanels.length-1)){
				for(int i = col-1; i<col+1; i++){
					if(nextPanels[row+1][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row-1][i].isAlive()){
						neighbors.add(true);
					}
				}
			}
//bottom row interior
			else if((col>0 && col<nextPanels.length-1) && row == nextPanels.length-1){
				for(int i = col-1; i<col+2; i++){
					if(nextPanels[row-1][i].isAlive()){
						neighbors.add(true);
					}
					if(nextPanels[row][i].isAlive()){
						neighbors.add(true);
					}
				}
			}

			if(((neighbors.size() == 4) || (neighbors.size()==3)) && nextPanels[row][col].isAlive()){
				panels[row][col].setAlive();
			}
			else if((neighbors.size() ==4)&& !nextPanels[row][col].isAlive()){
				panels[row][col].setDead();
			}
			else if((neighbors.size() ==5) && nextPanels[row][col].isAlive()){
				panels[row][col].setDead();
			}
			else if((neighbors.size() == 3) && !nextPanels[row][col].isAlive()){
				panels[row][col].setAlive();
			}

			col++;
			if(col==nextPanels.length+1){
				col=0;
				row++;

				if(row<nextPanels.length+1)
					checkSurround(row,col);

				else{
					row=0;
					for(int i = 0; i<panels.length; i++){
						for(int j = 0; j<panels[0].length; j++){
							grid.remove(nextPanels[i][j]);
							grid.add(panels[i][j]);
							nextPanels[i][j] = new GamePanel(this);
						}
					}
					invalidate();
					validate();
				}
			}
			else
				checkSurround(row,col);
		}
	}
}
