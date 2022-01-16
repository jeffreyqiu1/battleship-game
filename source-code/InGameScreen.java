import java.awt.*;  
import java.awt.event.*;  
import javax.swing.*;  

public class InGameScreen extends JPanel implements ActionListener{
	
	//button declarations
	public JButton[][] defenseGrid;
	public JButton[][] attackGrid;
	public JButton back, vertical, horizontal;
	
	//panel declarations
	public JPanel p1, p2, btn3, centP;
	
	//frame/layout declarations
	static Container shipPanels;
	static CardLayout clayout;
	public JLabel currentShip;
	
	//constructors
	Player p;
	AI a;
	
	//variables and arrays
	int width;
	int length;
	public int [] shipSize;
	public String [] shipName;
	boolean verticalPlacement = false;
	boolean horizontalPlacement = false;
	
	public InGameScreen(){
		reset();
	}  //end initializer

	public void actionPerformed(ActionEvent e) {
		boolean shipAlreadyThere = false;
		boolean invalidPlacement = false;
		boolean[][] tempShipGrid = p.getShipGrid();		
		// back
		if (e.getSource() == back) {
			reset();
			GameFrame.clayout.previous(GameFrame.hold);
		}
		//if vertical placement
		if (e.getSource() == vertical){
			vertical.setEnabled (false);
			horizontal.setEnabled (true);
			verticalPlacement = true;
			horizontalPlacement = false;
		}
		
		//if horizontal placement
		else if (e.getSource () == horizontal){
			horizontal.setEnabled (false);
			vertical.setEnabled(true);
			verticalPlacement = false;
			horizontalPlacement = true;
		}
		
		// place ships
		try{
			
			//if ships are not done placing
			if (p.shipsPlaced < 5) {
				for (int x = 0; x < length; x++) {
					for (int y = 0; y < width; y++) {	
					
					//check for ship placement
						if (e.getSource() == defenseGrid[x][y]) {
							shipAlreadyThere = false;
							invalidPlacement = false;
							
							if (verticalPlacement){
								// ensure ship can be placed there
								
								
								// ensure a ship is not already there
								
								tempShipGrid = p.getShipGrid();
								for (int ii = 0; ii < shipSize[p.shipsPlaced]; ii++){
									if (tempShipGrid[x+ii][y]) {
										shipAlreadyThere = true;
										break;
									}
								}
								//if ships not there and can place, place
								if (!shipAlreadyThere && !invalidPlacement) {
									for (int i = 0; i < shipSize [p.shipsPlaced]; i++){
										p.setShips (x+i, y);
										defenseGrid [x+i][y].setBackground (Color.GRAY);
										defenseGrid[x+i][y].setEnabled(false);
										
									}
									p.shipsPlaced++;
								}
							}
							
							//for horizontal placement
							else if (horizontalPlacement){
								// ensure ship can be placed there 
							
								// ensure a ship is not already there
								
								tempShipGrid = p.getShipGrid();
								for (int ii = 0; ii < shipSize[p.shipsPlaced]; ii++){
									if (tempShipGrid[x][y+ii]) {
										shipAlreadyThere = true;
										break;
									}
								}
							
								if (!shipAlreadyThere && !invalidPlacement) {
									for (int i = 0; i < shipSize [p.shipsPlaced]; i++){
										p.setShips (x, y+i);
										defenseGrid [x][y+i].setBackground (Color.GRAY);
										defenseGrid[x][y+i].setEnabled(false);
										
									}
									p.shipsPlaced++;
								}
							}
							
							if (p.shipsPlaced < 5){
								//display what ship you are placing
								currentShip.setText ("You are now placing your " + shipName[p.shipsPlaced]);
							}
							//display finisheds
							else if (p.shipsPlaced == 5){
								currentShip.setText ("You have placed all your ships.");
								vertical.setEnabled (false);
								horizontal.setEnabled (false);
							}
						}
					} // end y
				} // end x	
			}//end if
		}
		//catch arrayoutofbounds
		catch (ArrayIndexOutOfBoundsException error){
			invalidPlacement = true;
		}
		// all ships have been placed, shooting can begin
			for (int a = 0; a < length; a++) { // makes entire defenseGrid unclickable
				for (int b = 0; b < width; b++) {
					defenseGrid[a][b].setEnabled(false); 					} 
			}
			
		//set defense
		updateDefense(p.getShipGrid());
			
		// shoot
		if (p.lives > 0 && a.lives > 0) {
			
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < width; y++) {
				if (p.shipsPlaced == 5) { // START GAME 
					if (e.getSource() == attackGrid[x][y]) { //hit placement detector
						attackGrid[x][y].setEnabled(false);
						boolean[][] storedGrid = a.getShipGrid();
						//check for hit
						if (storedGrid[x][y]) {
							attackGrid[x][y].setBackground(Color.RED);
							a.lives--;//lose life
							if (a.lives == 0) {
								JOptionPane.showMessageDialog(this,
								"You won! Congratulations! ",
								"The game has ended",
								JOptionPane.PLAIN_MESSAGE);
								reset();
								GameFrame.clayout.previous(GameFrame.hold);
							}
						} else { // PLAYER MISSED
							attackGrid[x][y].setBackground(Color.WHITE);
							// computer takes a turn
							updateDefense(p.getShipGrid());
							boolean computerHits = false;
							int c = (int)(Math.random() * 10);
							int d = (int)(Math.random() * 10);
							storedGrid = p.getShipGrid();
							while (a.guesses[c][d]) { // it's already shot there
								c = (int)(Math.random() * 10);
								d = (int)(Math.random() * 10);
							}
							a.guesses[c][d] = true;

							if (storedGrid[c][d]) { // computer first hit
								p.lives--;
								computerHits = true;
								defenseGrid[c][d].setBackground(Color.RED);
								updateDefense(p.getShipGrid());
								if (p.lives == 0) { // check if game is over
									JOptionPane.showMessageDialog(this,
										"You lost. Better luck next time! ",
										"The game has ended",
										JOptionPane.PLAIN_MESSAGE);
									reset();
									GameFrame.clayout.previous(GameFrame.hold);
								} else { // game continues
									while (computerHits) {
										checkPlayer();
										boolean smartAI = true;
										if (smartAI) {
											checkPlayer();
											if (c - 1 >= 0 && !a.guesses[c - 1][d]) { // north inbounds and not shot yet
												c = c - 1;
												a.guesses[c][d] = true;
												storedGrid = p.getShipGrid();
												if (storedGrid[c][d]) {
													defenseGrid[c][d].setBackground(Color.RED);
													p.lives--;
													computerHits = true;
													checkPlayer();
												} else {
													computerHits = false;
												}
												checkPlayer();
											} else if (c + 1 <= 9 && !a.guesses[c + 1][d]) { // south inbounds and not shot yet
												c = c + 1;
												a.guesses[c][d] = true;
												storedGrid = p.getShipGrid();
												if (storedGrid[c][d]) {
													defenseGrid[c][d].setBackground(Color.RED);
													p.lives--;
													computerHits = true;
													checkPlayer();
												} else {
													computerHits = false;
												}
												checkPlayer();
											} else if (d - 1 >= 0 && !a.guesses[c][d - 1]) { // west inbounds and not shot yet
												d = d - 1;
												a.guesses[c][d] = true;
												storedGrid = p.getShipGrid();
												if (storedGrid[c][d]) {
													defenseGrid[c][d].setBackground(Color.RED);
													p.lives--;
													computerHits = true;
													checkPlayer();
												} else {
													computerHits = false;
												}
												checkPlayer();
											} else if (d + 1 <= 9 && !a.guesses[c][d + 1]) { // east inbounds and not shot yet
												d = d + 1;
												a.guesses[c][d] = true;
												storedGrid = p.getShipGrid();
												if (storedGrid[c][d]) {
													defenseGrid[c][d].setBackground(Color.RED);
													p.lives--;
													computerHits = true;
													checkPlayer();
												} else {
													computerHits = false;
												}
												checkPlayer();
											} else {
												// choose randomly
												checkPlayer();
												while (a.guesses[c][d]) { // it's already shot there
													c = (int)(Math.random() * 10);
													d = (int)(Math.random() * 10);
												}
												a.guesses[c][d] = true;
												storedGrid = p.getShipGrid();
												if (storedGrid[c][d]) {
													p.lives--;
													computerHits = true;
													checkPlayer();
												} else {
													computerHits = false;
												}

											}
										} else { // NOT SMART AI
											// choose randomly
											checkPlayer();
											while (a.guesses[c][d]) { // it's already shot there
												c = (int)(Math.random() * 10);
												d = (int)(Math.random() * 10);
											}
											a.guesses[c][d] = true;
											storedGrid = p.getShipGrid();
											if (storedGrid[c][d]) {
												p.lives--;
											}
											checkPlayer();
										}
										checkPlayer();
									} // end computer hit streak
									defenseGrid[c][d].setBackground(Color.WHITE); // when computer eventually misses
									checkPlayer();

								} // end else game continues

							} else { // computer first missed
								defenseGrid[c][d].setBackground(Color.WHITE);
							}
						} // end player missed
					} // end click for game
				} // end game
			} // end y
		} // end x
		
		} // end if
	} // end actionPerformed
	
	//set defense method
	public void updateDefense(boolean[][] grid) {
			for (int x = 0; x < length; x++) {
				for (int y = 0; y < width; y++) {	
					if (grid[x][y]) { // ship spots
						defenseGrid [x][y].setEnabled (false);
						defenseGrid[x][y].setBackground(Color.GRAY);
						if (a.guesses[x][y]) { // AI hit a ship spot
							defenseGrid[x][y].setBackground(Color.RED);
						} else { // ship spot has not been hit yet
							defenseGrid[x][y].setBackground(Color.GRAY);
						}
					} else { // not ship spots
						defenseGrid[x][y].setEnabled (true);
						defenseGrid[x][y].setBackground (Color.BLUE);
						if (a.guesses[x][y]) { // AI missed
							defenseGrid[x][y].setBackground(Color.WHITE);
						} else { // spot has not been hit yet
							defenseGrid[x][y].setBackground(Color.BLUE);
						}
					}	
				}
			}
	}// end updateDefense
	
	//remove asssets and redeclare
	public void reset() {
		
		removeAll();
		
		//variable values
		width = 10;
		length = 10;
		
		//set size of ships
		shipSize = new int [5];
		shipSize [0] = 2;
		shipSize [1] = 3;
		shipSize [2] = 3;
		shipSize [3] = 4;
		shipSize [4] = 5;
		
		//set ship names
		shipName = new String [5];
		shipName [0] = "Destroyer (size 2)";
		shipName [1] = "Cruiser (size 3)";
		shipName [2] = "Submarine (size 3)";
		shipName [3] = "Battleship (size 4)";
		shipName [4] = "Carrier (size 5)";
		
		//display ship name label
		currentShip = new JLabel ();
		
		//declare constructors
		p = new Player(length, width);
		a = new AI(length, width);
		
		//layout
		this.setLayout(new BorderLayout());
		
		//declare panels
		centP = new JPanel();
		centP.setLayout(new GridLayout(1,2, 15, 15));
		p1 = new JPanel();
		p2 = new JPanel();
		btn3 = new JPanel();
		
		//declare buttons
		back = new JButton("Back");
		vertical = new JButton ("Vertical");
		horizontal = new JButton ("Horizontal");
		horizontal.setEnabled (false);
		vertical.setEnabled (true);
		verticalPlacement = false;
		horizontalPlacement = true;
		
		//board layout and declaration
		p1.setLayout(new GridLayout(width,length));
		defenseGrid = new JButton[width][length];
		
		//fill your board with buttons
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < width; y++) {
				defenseGrid[x][y] = new JButton(); //creates new button
				defenseGrid[x][y].addActionListener(this);
                p1.add(defenseGrid[x][y]); //adds button to grid
				defenseGrid[x][y].setBackground (Color.BLUE);
			}
		} 
		
		//fill attacking board with buttons
		p2.setLayout(new GridLayout(width,length));
		attackGrid = new JButton[width][length];
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < width; y++) {
				attackGrid[x][y] = new JButton(); //creates new button     
                attackGrid[x][y].addActionListener(this);
				p2.add(attackGrid[x][y]); //adds button to grid
				attackGrid[x][y].setBackground (Color.BLUE);
			}
		}
		
		//what ship you are placing
		currentShip.setText ("You are now placing your " + shipName [0]);
		
		//button actionlisteners
		back.addActionListener(this);
		vertical.addActionListener (this);
		horizontal.addActionListener (this);
		
		//add panels
		btn3.add (currentShip);
		centP.add(p1);
		centP.add(p2);
		
		//add to final panel
		btn3.add(back);
		btn3.add(vertical);
		btn3.add(horizontal);
		this.add(centP, BorderLayout.CENTER); 
		this.add(btn3, BorderLayout.SOUTH);
	}
	
	
	//check if player dies
	public void checkPlayer() {
		updateDefense(p.getShipGrid());
	if (p.lives == 0) { // check if game is over
		JOptionPane.showMessageDialog(this,
		"You lost. Better luck next time! ",
		"The game has ended",
		JOptionPane.PLAIN_MESSAGE);
		reset();
		GameFrame.clayout.previous(GameFrame.hold);
		} // end if
	}
	
} // end InGameScreen class

