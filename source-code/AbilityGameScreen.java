import java.awt. * ;
import java.awt.event. * ;
import javax.swing. * ;

public class AbilityGameScreen extends JPanel implements ActionListener {
	//button declarations
	public JButton[][]defenseGrid;
	public JButton[][]attackGrid;
	public JButton back,
	vertical,
	horizontal,
	nuke,
	column,
	row,
	regular;

	//panel declarations
	public JPanel p1,
	p2,
	btn3,
	centP;

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
	public int[]shipSize;
	public String[]shipName;
	public boolean[][]shipHitBefore;
	boolean verticalPlacement = false;
	boolean horizontalPlacement = false;
	boolean useNuke;
	boolean useColumn;
	boolean useRow;
	boolean useRegular;
	boolean nukeUsed;
	boolean columnUsed;
	boolean rowUsed;

	//constructor
	public AbilityGameScreen() {
		reset();

	} //end initializer

	public void actionPerformed(ActionEvent e) {
		boolean shipAlreadyThere = false;
		boolean invalidPlacement = false;
		boolean alreadyShot = false;
		boolean[][]tempShipGrid = p.getShipGrid();
		// back
		if (e.getSource() == back) {
			reset();
			GameFrame.clayout.previous(GameFrame.hold);
		}
		//rotated vertically
		if (e.getSource() == vertical) {
			vertical.setEnabled(false);
			horizontal.setEnabled(true);
			verticalPlacement = true;
			horizontalPlacement = false;
		}
		//rotated horizontally
		else if (e.getSource() == horizontal) {
			horizontal.setEnabled(false);
			vertical.setEnabled(true);
			verticalPlacement = false;
			horizontalPlacement = true;
		}

		//use nuke
		else if (e.getSource() == nuke) {
			useNuke = true;
			useRow = false;
			useColumn = false;
			useRegular = false;
			nuke.setEnabled(false);
			column.setEnabled(true);
			row.setEnabled(true);
			regular.setEnabled(true);
		}

		//use row
		else if (e.getSource() == row) {
			useNuke = false;
			useRow = true;
			useColumn = false;
			useRegular = false;
			nuke.setEnabled(true);
			column.setEnabled(true);
			row.setEnabled(false);
			regular.setEnabled(true);
		}

		//use column
		else if (e.getSource() == column) {
			useNuke = false;
			useRow = false;
			useColumn = true;
			useRegular = false;
			nuke.setEnabled(true);
			column.setEnabled(false);
			row.setEnabled(true);
			regular.setEnabled(true);

		}

		//go back to regular
		else if (e.getSource() == regular) {
			useNuke = false;
			useRow = false;
			useColumn = false;
			useRegular = true;
			nuke.setEnabled(true);
			column.setEnabled(true);
			row.setEnabled(true);
			regular.setEnabled(false);
		}

		// place ships
		try {
			if (p.shipsPlaced < 5) {
				for (int x = 0; x < length; x++) {
					for (int y = 0; y < width; y++) {
						if (e.getSource() == defenseGrid[x][y]) {
							shipAlreadyThere = false;
							invalidPlacement = false;

							if (verticalPlacement) {
								// ensure ship can be placed there


								// ensure a ship is not already there

								tempShipGrid = p.getShipGrid();
								for (int ii = 0; ii < shipSize[p.shipsPlaced]; ii++) {
									if (tempShipGrid[x + ii][y]) {
										shipAlreadyThere = true;
										break;
									}
								}

								if (!shipAlreadyThere && !invalidPlacement) {
									for (int i = 0; i < shipSize[p.shipsPlaced]; i++) {
										p.setShips(x + i, y);
										defenseGrid[x + i][y].setBackground(Color.GRAY);
										defenseGrid[x + i][y].setEnabled(false);

									}
									p.shipsPlaced++;
								}
							} else if (horizontalPlacement) {
								// ensure ship can be placed there

								// ensure a ship is not already there

								tempShipGrid = p.getShipGrid();
								for (int ii = 0; ii < shipSize[p.shipsPlaced]; ii++) {
									if (tempShipGrid[x][y + ii]) {
										shipAlreadyThere = true;
										break;
									}
								}

								if (!shipAlreadyThere && !invalidPlacement) {
									for (int i = 0; i < shipSize[p.shipsPlaced]; i++) {
										p.setShips(x, y + i);
										defenseGrid[x][y + i].setBackground(Color.GRAY);
										defenseGrid[x][y + i].setEnabled(false);

									}
									p.shipsPlaced++;
								}
							}

							if (p.shipsPlaced < 5)
								//display what ship you are placing
								currentShip.setText("You are now placing your " + shipName[p.shipsPlaced]);
							else if (p.shipsPlaced == 5) {
								currentShip.setText("You have placed all your ships.");
								vertical.setEnabled(false);
								horizontal.setEnabled(false);
							}
						}
					} // end y
				} // end x
			}
		} catch (ArrayIndexOutOfBoundsException error) {
			invalidPlacement = true;
		}
		// all ships have been placed, shooting can begin
		for (int a = 0; a < length; a++) { // makes entire defenseGrid unclickable
			for (int b = 0; b < width; b++) {
				defenseGrid[a][b].setEnabled(false);
			}
		}

		updateDefense(p.getShipGrid());

		// shoot
		if (p.lives >= 0 && a.lives >= 0) {
			try {
				if (useRegular) {
					for (int x = 0; x < length; x++) {
						for (int y = 0; y < width; y++) {
							if (p.shipsPlaced == 5) { // PLAYER HIT
								if (e.getSource() == attackGrid[x][y]) {
									attackGrid[x][y].setEnabled(false);
									boolean[][]storedGrid = a.getShipGrid();
									if (storedGrid[x][y]) {
										attackGrid[x][y].setBackground(Color.RED);
										a.lives--;
										shipHitBefore[x][y] = true;
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
										boolean computerHits = true;
										while (computerHits) {
											try {
												Thread.sleep(100);
												int c = (int)(Math.random() * 10);
												int d = (int)(Math.random() * 10);
												storedGrid = p.getShipGrid();
												while (a.guesses[c][d]) { // it's already shot there
													c = (int)(Math.random() * 10);
													d = (int)(Math.random() * 10);
												}
												a.guesses[c][d] = true;

												if (storedGrid[c][d]) {
													defenseGrid[c][d].setBackground(Color.RED);
													computerHits = true;
													p.lives--;
													if (p.lives == 0) {
														JOptionPane.showMessageDialog(this,
															"You lost. Better luck next time! ",
															"The game has ended",
															JOptionPane.PLAIN_MESSAGE);
														reset();
														GameFrame.clayout.previous(GameFrame.hold);
													}
												} else {
													defenseGrid[c][d].setBackground(Color.WHITE);
													computerHits = false;
												}
											} catch (InterruptedException ex) {
												Thread.currentThread().interrupt();
											}

										}
									}
								}
							} // end game
						} // end y
					} // end x
				} //end regular
				else if (useRow) {

					for (int x = 0; x < length; x++) {
						for (int y = 0; y < width; y++) {
							if (p.shipsPlaced == 5) { // PLAYER HIT
								if (e.getSource() == attackGrid[x][y]) {
									if (!rowUsed) {
										//loop to hit complete row
										for (int rowShot = 0; rowShot < 10; rowShot++) {
											attackGrid[x][rowShot].setEnabled(false);
											boolean[][]storedGrid = a.getShipGrid();
											if (storedGrid[x][rowShot] && !shipHitBefore[x][rowShot]) {

												attackGrid[x][rowShot].setBackground(Color.RED);

												a.lives--;
												shipHitBefore[x][rowShot] = true;

												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
											} else // PLAYER MISSED
													attackGrid[x][rowShot].setBackground(Color.WHITE);
												//only let computer shoot if row is done
												if (rowShot == 9) {

													// computer takes a turn
													attackGrid[x][rowShot].setBackground(Color.WHITE);
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
												}
										}
									}
									rowUsed = true;

								}
							} // end game
						} // end y
					} // end x
				} //end regular//end regular
				else if (useColumn) {
					for (int x = 0; x < length; x++) {
						for (int y = 0; y < width; y++) {
							if (p.shipsPlaced == 5) { // PLAYER HIT
								if (e.getSource() == attackGrid[x][y]) {
									if (!columnUsed) {
										//loop to hit complete column
										for (int columnShot = 0; columnShot < 10; columnShot++) {
											attackGrid[columnShot][y].setEnabled(false);
											boolean[][]storedGrid = a.getShipGrid();
											if (storedGrid[columnShot][y] && !shipHitBefore[columnShot][y]) {

												attackGrid[columnShot][y].setBackground(Color.RED);
												a.lives--;
												shipHitBefore[columnShot][y] = true;
												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
											} else // PLAYER MISSED
												attackGrid[columnShot][y].setBackground(Color.WHITE);
											if (storedGrid[columnShot][y]) {
												attackGrid[columnShot][y].setBackground(Color.RED);
											}
											//only let computer shoot if column done
											if (columnShot == 9) {

												// computer takes a turn
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
											}
										}
									}
									columnUsed = true;
								}
							} // end game
						} // end y
					} // end x
				} //end regular//end regular
				else if (useNuke) {

					boolean nukeDone = false;
					for (int x = 0; x < length; x++) {
						for (int y = 0; y < width; y++) {
							if (p.shipsPlaced == 5) { // PLAYER HIT
								if (e.getSource() == attackGrid[x][y]) {
									if (!nukeUsed) {
										try {
											attackGrid[x][y].setEnabled(false);
											boolean[][]storedGrid = a.getShipGrid();
											
											//check first nuke position
											if (storedGrid[x][y] && !shipHitBefore[x][y]) {
												attackGrid[x][y].setBackground(Color.RED);
												a.lives--;
												shipHitBefore[x][y] = true;
												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
											} else // PLAYER MISSED
												attackGrid[x][y].setBackground(Color.WHITE);
												
												
											//check second nuke position
											attackGrid[x - 1][y].setEnabled(false);
											storedGrid = a.getShipGrid();
											if (storedGrid[x - 1][y] && !shipHitBefore[x - 1][y]) {
												attackGrid[x - 1][y].setBackground(Color.RED);
												a.lives--;
												shipHitBefore[x - 1][y] = true;
												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
											} 
											else if (storedGrid [x-1][y] && shipHitBefore [x-1][y]){
											}
											else // PLAYER MISSED
												attackGrid[x - 1][y].setBackground(Color.WHITE);
												
												
											//check third nuke position
											attackGrid[x - 1][y - 1].setEnabled(false);
											storedGrid = a.getShipGrid();
											if (storedGrid[x - 1][y - 1] && !shipHitBefore[x - 1][y - 1]) {
												attackGrid[x - 1][y - 1].setBackground(Color.RED);
												a.lives--;
												shipHitBefore[x - 1][y - 1] = true;
												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
											} 
											else if (storedGrid [x-1][y-1] && shipHitBefore [x-1][y-1]){
											}
											else // PLAYER MISSED
												attackGrid[x - 1][y - 1].setBackground(Color.WHITE);
											
											//check fourth nuke position
											attackGrid[x][y - 1].setEnabled(false);
											storedGrid = a.getShipGrid();
											if (storedGrid[x][y - 1] && !shipHitBefore[x][y - 1]) {
												attackGrid[x][y - 1].setBackground(Color.RED);
												a.lives--;
												shipHitBefore[x][y - 1] = true;
												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
											}
											else if (storedGrid [x][y-1] && shipHitBefore [x][y-1]){
											}											
											else // PLAYER MISSED
												attackGrid[x][y - 1].setBackground(Color.WHITE);


											//check fifth nuke position
											attackGrid[x + 1][y - 1].setEnabled(false);
											storedGrid = a.getShipGrid();
											if (storedGrid[x + 1][y - 1] && !shipHitBefore[x + 1][y - 1]) {
												attackGrid[x + 1][y - 1].setBackground(Color.RED);
												a.lives--;
												shipHitBefore[x + 1][y - 1] = true;
												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
											} 
											else if (storedGrid [x+1][y-1] && shipHitBefore [x+1][y-1]){
											}
											else // PLAYER MISSED
												attackGrid[x + 1][y - 1].setBackground(Color.WHITE);


											//check sixth nuke position
											attackGrid[x + 1][y].setEnabled(false);
											storedGrid = a.getShipGrid();
											if (storedGrid[x + 1][y] && !shipHitBefore[x + 1][y]) {
												attackGrid[x + 1][y].setBackground(Color.RED);
												a.lives--;
												shipHitBefore[x + 1][y] = true;
												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
											} 
											else if (storedGrid [x+1][y] && shipHitBefore [x+1][y]){
											}
											else // PLAYER MISSED
												attackGrid[x + 1][y].setBackground(Color.WHITE);


											//check seventh nuke position
											attackGrid[x + 1][y + 1].setEnabled(false);
											storedGrid = a.getShipGrid();
											if (storedGrid[x + 1][y + 1] && !shipHitBefore[x + 1][y + 1]) {
												attackGrid[x + 1][y + 1].setBackground(Color.RED);
												a.lives--;
												shipHitBefore[x + 1][y + 1] = true;
												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
											}
											else if (storedGrid [x+1][y+1] && shipHitBefore [x+1][y+1]){
											}
											else // PLAYER MISSED
												attackGrid[x + 1][y + 1].setBackground(Color.WHITE);


											//check eighth nuke position
											attackGrid[x][y + 1].setEnabled(false);
											storedGrid = a.getShipGrid();
											if (storedGrid[x][y + 1] && !shipHitBefore[x][y + 1]) {
												attackGrid[x][y + 1].setBackground(Color.RED);
												a.lives--;
												shipHitBefore[x][y + 1] = true;
												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
											} 
											else if (storedGrid [x][y+1] && shipHitBefore [x][y=1]){
											}
											else // PLAYER MISSED
												attackGrid[x][y + 1].setBackground(Color.WHITE);


											//check last nuke position
											attackGrid[x - 1][y + 1].setEnabled(false);
											storedGrid = a.getShipGrid();
											if (storedGrid[x - 1][y + 1] && !shipHitBefore[x - 1][y + 1]) {
												attackGrid[x - 1][y + 1].setBackground(Color.RED);
												a.lives--;
												shipHitBefore[x - 1][y + 1] = true;
												if (a.lives == 0) {
													JOptionPane.showMessageDialog(this,
														"You won! Congratulations! ",
														"The game has ended",
														JOptionPane.PLAIN_MESSAGE);
													reset();
													GameFrame.clayout.previous(GameFrame.hold);
												}
												nukeDone = true;
											} 
											else if (storedGrid [x-1][y+1] && shipHitBefore [x-1][y+1]){
											}
											else // PLAYER MISSED
												attackGrid[x - 1][y + 1].setBackground(Color.WHITE);
												nukeDone = true; //finished checking
												
											if (nukeDone) {
												// computer takes a turn
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
											}

										} catch (ArrayIndexOutOfBoundsException error) {}
										nukeUsed = true;
									}
								}
							} // end game
						} // end y
					} // end x
				} //end nuke

				//Disable ability buttons if clicked
				if (rowUsed)
					row.setEnabled(false);
				if (columnUsed)
					column.setEnabled(false);
				if (nukeUsed)
					nuke.setEnabled(false);

			} catch (ArrayIndexOutOfBoundsException error) {}
		} // end if
	} // end actionPerformed

	public void updateDefense(boolean[][]grid) {
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < width; y++) {
				if (grid[x][y]) { // ship spots
					defenseGrid[x][y].setEnabled(false);
					defenseGrid[x][y].setBackground(Color.GRAY);
					if (a.guesses[x][y]) { // AI hit a ship spot
						defenseGrid[x][y].setBackground(Color.RED);
					} else { // ship spot has not been hit yet
						defenseGrid[x][y].setBackground(Color.GRAY);
					}
				} else { // not ship spots
					defenseGrid[x][y].setEnabled(true);
					defenseGrid[x][y].setBackground(Color.BLUE);
					if (a.guesses[x][y]) { // AI missed
						defenseGrid[x][y].setBackground(Color.WHITE);
					} else { // spot has not been hit yet
						defenseGrid[x][y].setBackground(Color.BLUE);
					}
				}
			}
		}
	} // end updateDefense

	public void reset() {
		removeAll();

		//variable values
		width = 10;
		length = 10;
		useNuke = false;
		useColumn = false;
		useRow = false;
		useRegular = true;
		nukeUsed = false;
		columnUsed = false;
		rowUsed = false;
		shipHitBefore = new boolean[10][10];

		//set shipHitBefore

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				shipHitBefore[i][j] = false;
			}
		}

		//set size of ships
		shipSize = new int[5];
		shipSize[0] = 2;
		shipSize[1] = 3;
		shipSize[2] = 3;
		shipSize[3] = 4;
		shipSize[4] = 5;

		//set ship names
		shipName = new String[5];
		shipName[0] = "Destroyer (size 2)";
		shipName[1] = "Cruiser (size 3)";
		shipName[2] = "Submarine (size 3)";
		shipName[3] = "Battleship (size 4)";
		shipName[4] = "Carrier (size 5)";

		//display ship name label
		currentShip = new JLabel();

		//declare constructors
		p = new Player(length, width);
		a = new AI(length, width);

		//layout
		this.setLayout(new BorderLayout());

		//declare panels
		centP = new JPanel();
		centP.setLayout(new GridLayout(1, 2, 15, 15));
		p1 = new JPanel();
		p2 = new JPanel();
		btn3 = new JPanel();

		//declare buttons
		back = new JButton("Back");
		vertical = new JButton("Vertical");
		horizontal = new JButton("Horizontal");
		nuke = new JButton("Nuke");
		row = new JButton("Row");
		column = new JButton("Column");
		regular = new JButton("Regular");
		nuke.setEnabled(true);
		row.setEnabled(true);
		column.setEnabled(true);
		regular.setEnabled(false);
		horizontal.setEnabled(false);
		vertical.setEnabled(true);
		verticalPlacement = false;
		horizontalPlacement = true;
		useNuke = false;
		useRow = false;
		useColumn = false;
		useRegular = true;

		//board layout and declaration
		p1.setLayout(new GridLayout(width, length));
		defenseGrid = new JButton[width][length];

		//fill your board with buttons
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < width; y++) {
				defenseGrid[x][y] = new JButton(); //creates new button
				defenseGrid[x][y].addActionListener(this);
				p1.add(defenseGrid[x][y]); //adds button to grid
				defenseGrid[x][y].setBackground(Color.BLUE);
			}
		}

		//fill attacking board with buttons
		p2.setLayout(new GridLayout(width, length));
		attackGrid = new JButton[width][length];
		for (int x = 0; x < length; x++) {
			for (int y = 0; y < width; y++) {
				attackGrid[x][y] = new JButton(); //creates new button
				attackGrid[x][y].addActionListener(this);
				p2.add(attackGrid[x][y]); //adds button to grid
				attackGrid[x][y].setBackground(Color.BLUE);
			}
		}

		//what ship you are placing
		currentShip.setText("You are now placing your " + shipName[0]);

		//button actionlisteners
		back.addActionListener(this);
		vertical.addActionListener(this);
		horizontal.addActionListener(this);
		nuke.addActionListener(this);
		row.addActionListener(this);
		column.addActionListener(this);
		regular.addActionListener(this);

		//add panels
		btn3.add(currentShip);
		centP.add(p1);
		centP.add(p2);
		btn3.add(back);
		btn3.add(vertical);
		btn3.add(horizontal);
		btn3.add(nuke);
		btn3.add(row);
		btn3.add(column);
		btn3.add(regular);

		//add to final panel
		this.add(centP, BorderLayout.CENTER);
		this.add(btn3, BorderLayout.SOUTH);
	}
	//check player lives
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
}
