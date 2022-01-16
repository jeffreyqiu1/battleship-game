/*
Final Project - Battleship
ICS 3U7 - Ms. Strelkovska
June 10 2019
Harry Hu and Jeffrey Qiu
 */

import java.awt. * ;
import java.awt.event. * ;
import javax.swing. * ;

public class Main extends JFrame {
	public static void main(String[]args) {
		
		//constructor
		GameFrame a = new GameFrame();
		
		//frame size
		a.setSize(900, 600);
		a.setVisible(true);
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
