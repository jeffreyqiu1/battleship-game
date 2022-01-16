/*
BattleShip Main Menu 
Made by Jeffrey and Harry
6-13-19
*/
import java.awt. * ;
import java.awt.event. * ;
import javax.swing. * ;

public class GameFrame extends JFrame {
	
	//declare layout and container
	static CardLayout clayout;
	static Container hold;
	
	//declare panels
	MenuPanel menuPanel;
	InGameScreen gamePanel;
	NormalInstructions instructionsPanel1;
	AbilityInstructions instructionsPanel2;
	BeforeNormalPanel normalPanel1;
	BeforeAbilityPanel normalPanel2;
	AbilityGameScreen specialGamePanel;
	ChooseGameMode gamemode;

	public GameFrame() {
		
		//assign holder and layout
		hold = getContentPane();
		clayout = new CardLayout();
		hold.setLayout(clayout);
		
		//assign panels
		menuPanel = new MenuPanel();

		gamemode = new ChooseGameMode();

		normalPanel1 = new BeforeNormalPanel();

		instructionsPanel1 = new NormalInstructions();

		gamePanel = new InGameScreen();

		specialGamePanel = new AbilityGameScreen();

		instructionsPanel2 = new AbilityInstructions();
		
		normalPanel2 = new BeforeAbilityPanel ();
		
		//add to cardlayout
		hold.add("BattleShip Main Menu", menuPanel);
		hold.add("Game Mode", gamemode);
		hold.add("Menu", normalPanel1);
		hold.add("Normal Battleship", gamePanel);
		hold.add("Normal Battleship Instructions", instructionsPanel1);
		hold.add("Menu", normalPanel2);
		hold.add("Special Battleship", specialGamePanel);
		hold.add("Special Battleship Instructions", instructionsPanel2);
	}
}

class MenuPanel extends JPanel implements ActionListener {
	
	//button declarations
	private JButton playGame;
	private JButton instructions;
	private ImageIcon menuBackground;

	//constructor
	public MenuPanel() {
		
		//button addition
		playGame = new JButton("Play Game");
		playGame.addActionListener(this);
		this.add(playGame);
		this.setLayout(new FlowLayout());
	}

	public void actionPerformed(ActionEvent e) {
		//Action performed for button
		if (e.getSource() == playGame) {
			GameFrame.clayout.next(GameFrame.hold);
		} 
	}

	public void paintComponent(Graphics g) {
		//Paint background image 
		super.paintComponent(g);
		menuBackground = new ImageIcon ("battleship.jpg");
		Image menuBack = menuBackground.getImage ();
		g.drawImage (menuBack, 0, 0, getWidth(), getHeight(), this);
		
		//draw Info
		g.setFont(new Font("SansSerif", Font.BOLD, 30));
		g.drawString("Welcome to Battleship", getWidth () / 2 - 150, getHeight () / 2 - 50);
		g.drawString("Made by Jeffrey Qiu and Harry Hu", getWidth () / 2 - 180, getHeight () / 2 - 10); 
	}
}

class NormalInstructions extends JPanel implements ActionListener {
	
	//button declaration
	private JButton back;
	
	//constructor
	public NormalInstructions() {
		back = new JButton("Back");
		back.addActionListener(this);
		this.add(back);
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Draw instructions
		g.setFont(new Font("SansSerif", Font.BOLD, 18));
		g.drawString("Before playing the game, you must place your ships", 0, 120);
		g.drawString("Ships cannot overlap but they can be placed next to each other", 0, 150);
		g.drawString("If you hit a ship you get to go again!", 0, 180);
		g.drawString("The ships are:", 0, 210);
		g.drawString("Carrier (Size 5)", 0, 240);
		g.drawString("Battleship (Size 4)", 0, 270);
		g.drawString("Cruiser (Size 3)", 0, 300);
		g.drawString("Submarine (Size 3)", 0, 330);
		g.drawString("Destroyer (Size 2)", 0, 360);
		g.drawString("First to sink all of the other person's ships wins!", 0, 390);
	}
	public void actionPerformed(ActionEvent e) {
		
		//go back to BeforeNormalPanel
		if (e.getSource() == back)
			GameFrame.clayout.previous(GameFrame.hold);
			GameFrame.clayout.previous(GameFrame.hold);
	}
}

class ChooseGameMode extends JPanel implements ActionListener {
	
	//button declaration
	public JButton normal,
	abilities,
	back;
	public ImageIcon menuBackground;

	//constructor
	public ChooseGameMode() {
		//assign buttons
		normal = new JButton("Normal");
		abilities = new JButton("Abilities");
		back = new JButton("Back");
		
		//add actionlistener
		normal.addActionListener(this);
		abilities.addActionListener(this);
		back.addActionListener(this);
		
		//add to panel
		this.add(normal);
		this.add(abilities);
		this.add(back);
	}

	public void actionPerformed(ActionEvent e) {
		
		
		if (e.getSource() == normal) {
			//go to gamePanel
			GameFrame.clayout.next(GameFrame.hold);
		} else if (e.getSource() == abilities) {
			
			//go to specialGamePanel
			GameFrame.clayout.next(GameFrame.hold);
			GameFrame.clayout.next(GameFrame.hold);
			GameFrame.clayout.next(GameFrame.hold);
			GameFrame.clayout.next(GameFrame.hold);
		} else if (e.getSource() == back) {
			//go to Main Menu
			GameFrame.clayout.first(GameFrame.hold);
		}
	}
	public void paintComponent(Graphics g) {
		//draw image background
		super.paintComponent(g);
		menuBackground = new ImageIcon ("battleship.jpg");
		Image menuBack = menuBackground.getImage ();
		g.drawImage (menuBack, 0, 0, getWidth(), getHeight(), this);
		
		//draw info
		g.setFont(new Font("SansSerif", Font.BOLD, 40));
		g.setColor (Color.BLACK);
		g.drawString("CHOOSE YOUR GAMEMODE", getWidth () / 2 - 250, getHeight () / 2 - 10);
		
	}
}

class BeforeNormalPanel extends JPanel implements ActionListener {
	
	//button declaration
	public JButton play,
	instructs,
	back;
	public ImageIcon normalBackground;

	//constructor
	public BeforeNormalPanel() {
		
		//assign button
		play = new JButton("Play game");
		instructs = new JButton("Instructions");
		back = new JButton("Back");
		
		//add actionlistener
		play.addActionListener(this);
		instructs.addActionListener(this);
		back.addActionListener(this);
		
		//add to panel
		this.add(play);
		this.add(instructs);
		this.add(back);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == back) {
			//go back
			GameFrame.clayout.previous(GameFrame.hold);
		} else if (e.getSource() == play) {
			//go play
			GameFrame.clayout.next(GameFrame.hold);
		} else if (e.getSource() == instructs) {
			//go instructions
			GameFrame.clayout.next(GameFrame.hold);
			GameFrame.clayout.next(GameFrame.hold);
		}
	}
	public void paintComponent(Graphics g) {
		
		//draw image background
		
		super.paintComponent(g);
		normalBackground = new ImageIcon ("normalbattleship.jpg");
		Image normalBack = normalBackground.getImage();
		g.drawImage (normalBack, 0, 0, getWidth(), getHeight(), this);
		
		//draw info
		g.setFont(new Font("SansSerif", Font.BOLD, 40));
		g.setColor (Color.WHITE);
		g.drawString("NORMAL BATTLESHIP", getWidth () / 2 - 150, getHeight () / 2 + 30);
	}
}

class BeforeAbilityPanel extends JPanel implements ActionListener {
	
	//button declaration
	public JButton play,
	instructs,
	back;
	public ImageIcon abilityBackground;

	public BeforeAbilityPanel() {
		//assign button
		play = new JButton("Play");
		instructs = new JButton("Instructions");
		back = new JButton("Back");
		
		//add actionlistener
		play.addActionListener(this);
		instructs.addActionListener(this);
		back.addActionListener(this);
		
		//add to panel
		this.add(play);
		this.add(instructs);
		this.add(back);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == back) {
			//go back
			GameFrame.clayout.previous (GameFrame.hold);
			GameFrame.clayout.previous (GameFrame.hold);
			GameFrame.clayout.previous (GameFrame.hold);
			GameFrame.clayout.previous (GameFrame.hold);
		} else if (e.getSource() == play) {
			//go play
			GameFrame.clayout.next(GameFrame.hold);

		} else if (e.getSource() == instructs) {
			//go to instructions
			GameFrame.clayout.next(GameFrame.hold);
			GameFrame.clayout.next(GameFrame.hold);

		}
	}
	public void paintComponent(Graphics g) {
		//draw image background
		super.paintComponent(g);
		abilityBackground = new ImageIcon ("specialbattleship.jpg");
		Image abilityBack = abilityBackground.getImage();
		g.drawImage (abilityBack, 0, 0, getWidth(), getHeight(), this);
		
		//draw info
		g.setColor (Color.WHITE);
		g.setFont(new Font("SansSerif", Font.BOLD, 40));
		g.drawString("SPECIAL BATTLESHIP", getWidth () / 2 - 150, getHeight () / 2 + 30);
	}
}

class AbilityInstructions extends JPanel implements ActionListener {
	//button declaration
	private JButton back;
	
	//constructor
	public AbilityInstructions() {
		//add button
		back = new JButton("Back");
		back.addActionListener(this);
		this.add(back);
	}
	public void paintComponent(Graphics g) {
		//draw info
		super.paintComponent(g);
		g.setFont(new Font("SansSerif", Font.BOLD, 18));
		g.drawString("Before playing the game, you must place your ships", 0, 80);
		g.drawString("Ships cannot overlap but they can be placed next to each other", 0, 110);
		g.drawString("If you hit a ship you get to go again!", 0, 140);
		g.drawString("Abilities can only be used once, and hitting ships with them do not grant extra turns.", 0, 170);
		g.drawString("Abilities are:", 0, 200);
		g.drawString("Nuke: Destroy a 3x3 area", 0, 230);
		g.drawString("Column: Destroy the entire column", 0, 260);
		g.drawString("Row: Destroy the entire row", 0, 290);
		g.drawString("The ships are:", 0, 320);
		g.drawString("Carrier (Size 5)", 0, 350);
		g.drawString("Battleship (Size 4)", 0, 380);
		g.drawString("Cruiser (Size 3)", 0, 410);
		g.drawString("Submarine (Size 3)", 0, 440);
		g.drawString("Destroyer (Size 2)", 0, 470);
		g.drawString("First to sink all of the other person's ships wins!", 0, 500);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == back)
			//go back
			GameFrame.clayout.previous(GameFrame.hold);
			GameFrame.clayout.previous(GameFrame.hold);
	}
}
