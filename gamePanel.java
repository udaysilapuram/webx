// Student Name: Uday Silapuram
// Student ID: 9999-03351
// Course Name: Advanced Programming Concepts

// import required default packages
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.io.File;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is a gamePanel Java class.
 *
 * @author Uday Silapuram
 */
public class gamePanel extends JPanel implements MouseListener 
{
	 // Timer Variables
    private int FreshSeconds;
    private int seconds[]; // Integer variable that holds the amount of time passed
    private Timer timer; // Timer object
    private TimerTask timerTask; // TimerTask object
 //  Component Declarations
    private JPanel UPBar;
    private JPanel game;
    private JLabel flagLabel, timeLabel;
    private JLabel gameOverLabel;
    private JButton newGameButton;
    public static JButton returnButton;

    // declaring Game Variables
    private final int Size_of_tile = 30; // helps to hold the size of each tile in pixels
    private final int Blank_tile = 0; //  value of an empty tile
    private final int FLAG = 10; //  value of a flagged tile
    private final int MINE = 9; // value of a mine

    private int flagsLeft; // helps to hold the amount of flags the user has available
    private int mouseX, mouseY; // Holds the user's mouse X and Y coordinates whenever they click

    // declaring Arrays
    private int board[][]; // Contains the location of every mine and each tile's respective numeric value
    private boolean covered[][]; // Contains if a certain cell is covered or uncovered
    private boolean checked[][]; // Contains if a cell has been checked or not (used only for method: checkAdjacentCells)   

    // declaring Game Flags
    private boolean noMinesLeft = false; // Boolean flag that represents if no mines are remaining
    private boolean noCoveredTilesLeft = false; // Boolean flag that represents if there are no covered tiles left (excludes flagged mine tiles)
    public boolean gameLost = false; // Boolean flag that triggers whenever a user hits a mine
    private boolean firstClick = false; // Boolean flag that triggers when the user makes a first click (First Click -> Place Mines)
    public boolean timeExceeded = false;
    // declaring audio Variables
    private final int NUM_IMAGES = 14; // Holds the amount of images in the image folder
    private final ImageIcon[] images = initImages(); // An array that holds all the images in the image folder
    private static final String BOOM_ICON_FILE_NAME = "images/boom.gif";
	private final ImageIcon boomIcon = createImageIcon(BOOM_ICON_FILE_NAME);    
    private final int BAR_HEIGHT = 60; // The height of the GUI bar at the top of the screen (in pixels)
    boolean repaint = true;  // Flags if the paint component should repaint itself

    /**
     * JPanel zero argument Constructor
	 *@param no parameter passed
	 *@return no value is returned
     */
    gamePanel() {

        // Set the remaining amount of flags to the amount of mines
        flagsLeft = Main.no_of_Mines;

        // Set the size of the arrays
        covered = new boolean[Main.no_of_rows][Main.no_of_Columns];
        board = new int[Main.no_of_rows][Main.no_of_Columns];
        checked = new boolean[Main.no_of_rows][Main.no_of_Columns];
       // seconds = new int[Main.no_of_sec];
        // Initiate the arrays
        for (int i = 0; i < Main.no_of_Columns; i++) {
            for (int j = 0; j < Main.no_of_rows; j++) { // Traverse through each cell

                // Set every tile covered
                covered[j][i] = true;
                // Set every tile's value to 0 (for now)
                board[j][i] = 0;
                // Set every tile to unchecked
                checked[j][i] = false;

            }
        }

        // Initiate the components in the panel
        initialize();

        // Panel properties
        this.setPreferredSize(new Dimension(Main.no_of_rows * Size_of_tile, Main.no_of_Columns * Size_of_tile + BAR_HEIGHT)); // Sets the preferred size of the panel
        this.setLayout(new BorderLayout()); // Sets the layout of the panel

        // Add the components to the panel
        this.add(UPBar, BorderLayout.NORTH);
        this.add(game, BorderLayout.SOUTH);

        // Add a mouse listener to the game panel
        game.addMouseListener(this);

        // Start the timer
        timer.scheduleAtFixedRate(timerTask, 1000, 1000);

    }

    /*
     * Paint method that constantly runs to update the panel Return type: returns
     * nothing (void)
     * 
     * @param g: the graphics object
     */
    @Override
    public void paint(Graphics g) {

        // Prevents the graphics object from overwriting all the components in the panel
        super.paint(g);

        // Set the game flags to true
        noMinesLeft = true;
        noCoveredTilesLeft = true;

        // Traverse board array and update covered cells and flagged cells
        for (int i = 0; i < Main.no_of_Columns; i++) {
            for (int j = 0; j < Main.no_of_rows; j++) {

                // The current tile has a value greater than a flag's value and is covered
                if (board[j][i] >= FLAG && covered[j][i] == true) {

                    // Draw a flag
                    g.drawImage(images[11].getImage(), j * Size_of_tile, i * Size_of_tile + BAR_HEIGHT, null);

                }

                // The current tile has a less than a flag's value and is covered
                if (board[j][i] < FLAG && covered[j][i] == true) {

                    // Draw an empty tile
                    g.drawImage(images[10].getImage(), j * Size_of_tile, i * Size_of_tile + BAR_HEIGHT, null);

                }
                // The current tile is a mine and the noMinesLeft flag is true
                if (board[j][i] == MINE && noMinesLeft == true) {

                    // Set the noMinesLeft flag to false
                    noMinesLeft = false;

                }

            }
        }

        // Traverse covered array for covered/uncovered cells
        for (int i = 0; i < Main.no_of_Columns; i++) {
            for (int j = 0; j < Main.no_of_rows; j++) {

                // The current tile is uncovered
                if (covered[j][i] == false) {

                    // Draw a tile respective to the tile's value
                    g.drawImage(images[board[j][i]].getImage(), j * Size_of_tile, i * Size_of_tile + BAR_HEIGHT, null);

                }

                // The current tile is covered and isn't a flagged mine
                if (covered[j][i] == true && board[j][i] != MINE + FLAG && noCoveredTilesLeft == true) {

                    // Set the noCoveredTilesLeft flag to false
                    noCoveredTilesLeft = false;

                }

            }
        }

        // Runs if the user hit a mine and the game is over
        if (gameLost) {

        	boomsound();
            gameOverLabel.setVisible(true);
            newGameButton = new JButton();
            // Stop the timer
            timer.cancel();

            // Set other mines and false flags visible
            for (int i = 0; i < Main.no_of_Columns; i++) {
                for (int j = 0; j < Main.no_of_rows; j++) {

                    // Current tile is a mine
                    if (board[j][i] == MINE) {
                        // Draw the mine
                        g.drawImage(images[9].getImage(), j * Size_of_tile, i * Size_of_tile + BAR_HEIGHT, null);
                    }

                    // Current tile is a false flagged tile
                    else if (board[j][i] >= FLAG && board[j][i] != MINE + FLAG) {
                        // Draw a false flag
                        g.drawImage(images[12].getImage(), j * Size_of_tile, i * Size_of_tile + BAR_HEIGHT, null);
                    }

                }
            }

            // Set return button visible and enabled
            repaint = false;
            newGameButton.setVisible(true);
            newGameButton.setEnabled(true);
            //newGameButton.setIcon(sadIcon);
            returnButton.setVisible(true);
            returnButton.setEnabled(true);

        }

        if (timeExceeded) {
        	timeover();
            // Set the label visible
        	gameOverLabel.setText("Time UP!");
        	gameOverLabel.setFont(new Font(gameOverLabel.getFont().getName(), Font.BOLD, 20));
        	gameOverLabel.setForeground(Color.WHITE);
        	gameOverLabel.setVisible(true);
        	//timeLabel.setIcon(boomIcon);
        	timeLabel.setVisible(true);
        	timer.cancel();
            // Set other mines and false flags visible
            for (int i = 0; i < Main.no_of_Columns; i++) {
                for (int j = 0; j < Main.no_of_rows; j++) {

                    // Current tile is a mine
                    if (board[j][i] == MINE) {
                        // Draw the mine
                        g.drawImage(images[9].getImage(), j * Size_of_tile, i * Size_of_tile + BAR_HEIGHT, null);
                    }

                    // Current tile is a false flagged tile
                    else if (board[j][i] >= FLAG && board[j][i] != MINE + FLAG) {
                        // Draw a false flag
                        g.drawImage(images[12].getImage(), j * Size_of_tile, i * Size_of_tile + BAR_HEIGHT, null);
                    }

                }
            }
            
            JOptionPane.showMessageDialog(null, "TIME UP!" + String.valueOf(FreshSeconds-2) +"  "+"seconds");
            // Set return button visible and enabled
            repaint = false;
            returnButton.setVisible(true);
            returnButton.setEnabled(true);

        }
        // User won the game!
        else if (noMinesLeft == true && noCoveredTilesLeft == true) {
        	newGameButton = new JButton();
            // Cancel the timer
            timer.cancel();
            // Display a message to the user
            gameOverLabel.setText("You found all mines!");
            gameOverLabel.setForeground(Color.GREEN);
            gameOverLabel.setVisible(true);
            
            newGameButton.setVisible(true);
            newGameButton.setEnabled(true);
            // Set return button visible and enabled
            repaint = false;
            returnButton.setVisible(true);
            returnButton.setEnabled(true);

        }

        if (repaint) {
            // Repaint the GUI
            this.repaint();
        }

        // Update flagsLeft label
        flagLabel.setText(String.valueOf(flagsLeft));

    }
	/**
     * boomsound
	 *@param no parameter is passed
	 *@return no value is returned
     */

    public static void boomsound() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("blast.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {}
	}
	/**
     * timeover
	 *@param no parameter is passed
	 *@return no value is returned
     */
	  public static void timeover() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("blast.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {}
	}
    
  

	/**
     * Runs whenever a mouse is clicked Return type: returns nothing (void)
     * Return type: Returns nothing (void)
     * @param e: the mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {

        // Get the current mouse coordinates respective to each tile
        mouseX = (int) (e.getX() / Size_of_tile);
        mouseY = (int) (e.getY() / Size_of_tile);

        // User right clicked
        if (e.getButton() == MouseEvent.BUTTON3 && !gameLost && covered[mouseX][mouseY] == true && firstClick == true) {

            // Remove exiting flag
            if (board[mouseX][mouseY] >= FLAG) {
                board[mouseX][mouseY] -= FLAG;
                flagsLeft++;
            }
            // Add a flag
            else if (board[mouseX][mouseY] < FLAG && flagsLeft > 0) {
                board[mouseX][mouseY] += FLAG;
                flagsLeft--;
            }

        }

        // User left clicked
        if (e.getButton() == MouseEvent.BUTTON1 && !gameLost) {

            // Checks that the user isn't clicking on a flagged tile or an uncovered tile
            if (covered[mouseX][mouseY] == true && board[mouseX][mouseY] < FLAG) {

                // If the user hit a mine, set the gameLost flag to true
                if (board[mouseX][mouseY] == MINE) {
                    gameLost = true;
                }

                // The user hit an empty tile
                else if (board[mouseX][mouseY] == Blank_tile) {

                    // Runs if this is the user's first click
                    if (!firstClick) {

                        // Set the current tile to uncovered
                        covered[mouseX][mouseY] = false;
                        // Set the flag firstClick to true
                        firstClick = true;
                        // Initiate the board
                        board = initBoard(Main.no_of_rows, Main.no_of_Columns, Main.no_of_Mines);

                    }

                    // search adjacent cells and uncover them
                    revealAdjacentCells(mouseX, mouseY);

                }

                // Runs if the user hit a numeric tile
                else {

                    // Uncover the tile
                    covered[mouseX][mouseY] = false;

                }

            }

        }

    }

    // Unused mouseEvent methods
    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    /**
     * Initializes the board array Return type: Returns a 2D integer array (int[][])
     * @param rows: the amount of rows in the game
     * @param columns: the amount of columns in the game
     * @param mines: the amount of mines in the game
	 *@return intiger array n by n that contains rows and columns
     */
    public int[][] initBoard(int rows, int columns, int mines) {

        // Create a new 2D integer array
        int[][] fld = new int[rows][columns];
        // Create a 2D boolean array that contains spots where a mine won't be able to
        // generate
        boolean[][] noMine = new boolean[rows][columns];

        int minesLeft = mines;
        int randomX, randomY; // Contains the random coordinates for choosing mines

        // Random Object
        Random randObj = new Random();

        // Flag adjacent spots where no mines can generate
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {

                // Runs if the current tile is uncovered
                if (covered[j][i] == false) {

                    // no mine can generate on the uncovered tile
                    noMine[j][i] = true;

                    if (j > 0) { // Left tile
                        noMine[j - 1][i] = true;
                    }
                    if (j < rows - 1) { // Right tile
                        noMine[j + 1][i] = true;
                    }
                    if (i > 0) { // Top tile
                        noMine[j][i - 1] = true;
                    }
                    if (i < columns - 1) { // Bottom tile
                        noMine[j][i + 1] = true;
                    }
                    if (i > 0 && j > 0) { // Top-left tile
                        noMine[j - 1][i - 1] = true;
                    }
                    if (i > 0 && j < rows - 1) { // Top-right tile
                        noMine[j + 1][i - 1] = true;
                    }
                    if (i < columns - 1 && j > 0) { // Bottom-left tile
                        noMine[j - 1][i + 1] = true;
                    }
                    if (i < columns - 1 && j < rows - 1) { // Bottom-right tile
                        noMine[j + 1][i + 1] = true;
                    }

                }

            }
        }

        // Fill random spots with mines until there are no mines left to be placed
        while (minesLeft > 0) {

            // Generating a new random coordinate
            randomX = randObj.nextInt(rows);
            randomY = randObj.nextInt(columns);

            // Runs if the current tile is not a mine and can be placed down
            if (fld[randomX][randomY] != MINE && noMine[randomX][randomY] == false) {

                fld[randomX][randomY] = MINE;
                minesLeft--;

            }

        }

        // Assign numbers to tiles based on the mines around them
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {

                // Mine found
                if (fld[j][i] == MINE) {

                    // Check adjacent cells and increase their value by one if they're not a mine
                    if (j > 0 && fld[j - 1][i] != MINE) // Left 
					{
                        fld[j - 1][i]++;
                    }
                    if (j < rows - 1 && fld[j + 1][i] != MINE) // Right
					{ 
                        fld[j + 1][i]++;
                    }
                    if (i > 0 && fld[j][i - 1] != MINE)  // Top
					{
                        fld[j][i - 1]++;
                    }
                    if (i < columns - 1 && fld[j][i + 1] != MINE)// Bottom 
					{ 
                        fld[j][i + 1]++;
                    }
                    if (i > 0 && j > 0 && fld[j - 1][i - 1] != MINE)// Top-left 
					{ 
                        fld[j - 1][i - 1]++;
                    }
                    if (i > 0 && j < rows - 1 && fld[j + 1][i - 1] != MINE) // Top-right
					{ 
                        fld[j + 1][i - 1]++;
                    }
                    if (i < columns - 1 && j > 0 && fld[j - 1][i + 1] != MINE) // Bottom-left
					{ 
                        fld[j - 1][i + 1]++;
                    }
                    if (i < columns - 1 && j < rows - 1 && fld[j + 1][i + 1] != MINE) // Bottom-right
					{ 
                        fld[j + 1][i + 1]++;
                    }

                }

            }
        }

        // Return the array containing all mines and number tiles
        return fld;

    }

    /**
     * Initates all components in the game panel 
	 *@param no parameter is passed
	 *@return no value is returned
     */
    public void initialize() {

        // Panels
        UPBar = new JPanel();
        UPBar.setBackground(Color.WHITE);
        UPBar.setPreferredSize(new Dimension(Size_of_tile * Main.no_of_rows, BAR_HEIGHT));
        UPBar.setLayout(new BorderLayout());

        game = new JPanel();
        game.setPreferredSize(new Dimension(Size_of_tile * Main.no_of_rows, Size_of_tile * Main.no_of_Columns));
        game.setLayout(new GridBagLayout());

        // Buttons
        returnButton = new JButton();
        returnButton.setText("Reset");
        returnButton.setForeground(Color.BLACK);
        returnButton.setFont(new Font(returnButton.getFont().getName(), Font.PLAIN, 20));
        returnButton.setPreferredSize(new Dimension(150, 40));
        returnButton.setFocusable(false);
        returnButton.setEnabled(false);
        returnButton.setVisible(false);
        
        newGameButton = new JButton();
        newGameButton.setForeground(Color.BLACK);
        newGameButton.setPreferredSize(new Dimension(100, 40));
        newGameButton.setFocusable(false);
        newGameButton.setVisible(false);
        newGameButton.setEnabled(false);
        // Labels
        flagLabel = new JLabel();
        flagLabel.setIcon(images[9]);
        flagLabel.setFont(new Font(flagLabel.getFont().getName(), Font.PLAIN, 20));
        flagLabel.setText(String.valueOf(flagsLeft));
        flagLabel.setForeground(Color.BLACK);

        timeLabel = new JLabel();
        timeLabel.setIcon(images[13]);
        timeLabel.setFont(new Font(timeLabel.getFont().getName(), Font.PLAIN, 20));
        timeLabel.setHorizontalTextPosition(JLabel.LEFT);
        timeLabel.setForeground(Color.BLACK);
        timeLabel.setOpaque(false);
        
        gameOverLabel = new JLabel();
        gameOverLabel.setText("Game Over!");
        gameOverLabel.setVisible(false);
        gameOverLabel.setFont(new Font(gameOverLabel.getFont().getName(), Font.PLAIN, 20));
        gameOverLabel.setForeground(Color.RED);
        
        // Add components
        UPBar.add(flagLabel, BorderLayout.WEST);
        UPBar.add(timeLabel, BorderLayout.EAST);
        UPBar.add(gameOverLabel, BorderLayout.CENTER);
       
        game.add(returnButton);
       
        // Timer
        FreshSeconds = 0;
        timer = new Timer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
            	Main.gameSound();
            	if(FreshSeconds<=Main.no_of_sec) {
                timeLabel.setText(String.valueOf(FreshSeconds));
            	}
            	else
            	{
            		
            		timeExceeded = true;
      
            	}
            	FreshSeconds++;
            }

        };

    }

    /** 
     * Returns an ImageIcon array containing all the images in the image folder
	 *@param no parameter is passed
     * @return  Returns an ImageIcon array (ImageIcon[])
     */
    public ImageIcon[] initImages() {

        // Original images
        ImageIcon[] images = new ImageIcon[NUM_IMAGES];
        // Scaled images
        Image[] scaledImages = new Image[NUM_IMAGES];
        // Resized images
        ImageIcon[] resizedImages = new ImageIcon[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {

            images[i] = new ImageIcon(getClass().getResource("images/" + i + ".png"));
            scaledImages[i] = images[i].getImage().getScaledInstance(Size_of_tile, Size_of_tile, java.awt.Image.SCALE_SMOOTH);
            resizedImages[i] = new ImageIcon(scaledImages[i]);

        }

        // Returns the resized images based on Size_of_tile
        return resizedImages;

    }
	
	 /** 
      Create an ImageIcon from the given file, or returns null if the path was invalid. 
         (adapted from code in Oracle Java Tutorials
         https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html)
    * @param path    relative path to the file that has the image
    * @return   ImageIcon for this image or null if path was invalid
    */
   private ImageIcon createImageIcon(String path) 
   {
      java.net.URL imgURL = getClass().getResource(path);
      if (imgURL != null) {
         return new ImageIcon(imgURL, path);
      } else {
         System.err.println("not able to find file: " + path);
         return null;
      }
   }


    /**
     * A basic recursive method that reveals nearby tiles Return type: Returns
     * nothing (void)
     * @param px: the x-coordinate to reveal
     * @param pY: the y-coordinate to reveal
	 *@return no value is returned
     */
    public void revealAdjacentCells(int px, int pY) {

        // Current tile hasn't been checked yet
        if (!checked[px][pY]) {

            // Uncover the current tile and mark it as checked
            covered[px][pY] = false;
            checked[px][pY] = true;

            // Runs if the current tile is an empty tile (Check adjacent cells)
            if (board[px][pY] == 0) {

                // Checks adjacent cells and the method calls itself again
                if (px > 0 && !checked[px - 1][pY] && board[px - 1][pY] < FLAG) { // Left
                    revealAdjacentCells(px - 1, pY);
                }
                if (px < Main.no_of_rows - 1 && !checked[px + 1][pY] && board[px + 1][pY] < FLAG) { // Right
                    revealAdjacentCells(px + 1, pY);
                }
                if (pY > 0 && !checked[px][pY - 1] && board[px][pY - 1] < FLAG) { // Top
                    revealAdjacentCells(px, pY - 1);
                }
                if (pY < Main.no_of_Columns - 1 && !checked[px][pY + 1] && board[px][pY + 1] < FLAG) { // Bottom
                    revealAdjacentCells(px, pY + 1);
                }
                if (pY > 0 && px > 0 && !checked[px - 1][pY - 1] && board[px - 1][pY - 1] < FLAG) { // Top-left
                    revealAdjacentCells(px - 1, pY - 1);
                }
                if (pY > 0 && px < Main.no_of_rows - 1 && !checked[px + 1][pY - 1]
                        && board[px + 1][pY - 1] < FLAG) { // Top-right
                    revealAdjacentCells(px + 1, pY - 1);
                }
                if (pY < Main.no_of_Columns - 1 && px > 0 && !checked[px - 1][pY + 1]
                        && board[px - 1][pY + 1] < FLAG) { // Bottom-left
                    revealAdjacentCells(px - 1, pY + 1);
                }
                if (pY < Main.no_of_Columns - 1 && px < Main.no_of_rows - 1 && !checked[px + 1][pY + 1]
                        && board[px + 1][pY + 1] < FLAG) { // Bottom-right
                    revealAdjacentCells(px + 1, pY + 1);
                }
            }
        }
    }
}
