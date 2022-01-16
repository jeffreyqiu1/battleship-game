/* Extension of Player class
Made by Jeffrey and Harry
6-13-19
*/


public class AI extends Player {
	//variable declarations
	int[]shipSize;
	boolean[][]guesses;

	public AI(int length, int width) {
		
		//variable assignment
		super(length, width);
		shipSize = new int[5];
		shipSize[0] = 2;
		shipSize[1] = 3;
		shipSize[2] = 3;
		shipSize[3] = 4;
		shipSize[4] = 5;
		guesses = new boolean[length][width];

		// set AI guesses all to false
		for (int m = 0; m < length; m++) {
			for (int n = 0; n < length; n++) {
				guesses[m][n] = false;
			}
		}

		// place the AI ships
		while (shipsPlaced < 5) {
			int currentShipSize = shipSize[shipsPlaced];

			// choose random spot
			int horizontal = (int)(Math.random() * 2);
			int x = (int)(Math.random() * 10);
			int y = (int)(Math.random() * 10);

			// ensure ship can be placed there or a ship is not already there
			boolean invalidPlacement = false;
			for (int b = 0; b < currentShipSize; b++) {
				if (horizontal == 0) {
					if (x + b > 9 || shipGrid[x + b][y])
						invalidPlacement = true;
				} else {
					if (y + b > 9 || shipGrid[x][y + b])
						invalidPlacement = true;
				}
			} // end for loop

			if (!invalidPlacement) {
				shipsPlaced++;
				// place each spot of current ship
				for (int b = 0; b < currentShipSize; b++) {
					if (horizontal == 0) {
						shipGrid[x + b][y] = true;
					} else {
						shipGrid[x][y + b] = true;
					}
				}
			} // end placing current ship

		} // end placing of all ships

	} //the whole constructor

} // end AI class
