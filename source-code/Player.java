import java.awt. * ;
import java.awt.event. * ;
import javax.swing. * ;

public class Player {
	//variable declarations
	protected boolean[][]shipGrid;
	int lives;
	int shipsPlaced;
	
	
	public Player(int length, int width) {
		lives = 17;
		shipGrid = new boolean[length][width];
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				shipGrid[x][y] = false;
			}
		}
	} // end initializer
	
	//set grid to true or false
	public void setShips(int x, int y) {
		shipGrid[x][y] = true;

	} // end setShips

	public boolean[][]getShipGrid() {
		return shipGrid;
	}

} // end class
