// Student Name: Uday Silapuram
// Student ID: 9999-03351
// Course Name: Advanced Programming Concepts

// import required default packages
import javax.sound.sampled.*;
import javax.swing.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * This is a Main Java class.
 *
 * @author Uday Silapuram
 */
public class Main extends JFrame implements ActionListener {
  // declaring Game Variables
    public static int no_of_rows, no_of_Columns, no_of_Mines,no_of_sec;
 // declaring Time Variables  
    public Timer timer; // Timer object
    public TimerTask timerTask; // timetask object
    /**
     * zero argument constructor 
	 *@param no parameters 
	 *@return nothing is returned
    */
    Main() {
    	gameSound();
        // Frame properties
        // Sets the title of the JFrame
        this.setTitle("Minesweeper");  
        this.setResizable(false);  // Makes sure the user can't resize the JFrame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Ensures that the threads close the program when the user hits the exit button
		//this.setIconImage(new ImageIcon(getClass().getResource("images/smiley.png")).getImage());  // Selects a Image for the JFrame icon
        this.add(new menu());  // Adds the panel containing the main menu to the JFrame
        this.pack();  // Sets every component to their preferred size
        this.setLocationRelativeTo(null);  // Put's the JFrame at the center of the user's screen 

        // Adds Action Listeners to button components
        menu.beginner.addActionListener(this);
        menu.advanced.addActionListener(this);
        menu.expert.addActionListener(this);
       // menu.customButton.addActionListener(this);

        this.setVisible(true);  // Makes the JFrame visible to the use
    }


    /**
     * gameSound function
	 *@param no parameter is passed
	 *@return nothing is returned
    */
	 public static void gameSound() 
	  {
	    try 
				{
					Clip clip = AudioSystem.getClip();
					clip.start();
				} 
				catch(Exception ex) 
				{	
				}
		}


	/* 
     * Main Method
    */
    public static void main(String[] args) 
	{
        // Create a new JFrame
        new Main(); 
    }

    /**
     * Runs whenever an action is performed
     * Return type: returns nothing (void)
     * @param e: the event that happened
    */
    @Override
    public void actionPerformed(ActionEvent e) {
        // User clicked return button
        if (e.getSource() == gamePanel.returnButton) {
            // Switch panels
            switchPanels(new menu());
            // Adds Action Listeners to button components
            menu.beginner.addActionListener(this);
            menu.advanced.addActionListener(this);
            menu.expert.addActionListener(this);
           // menu.customButton.addActionListener(this);  
        }
        // User selected easy difficulty
        else if (e.getSource() == menu.beginner) {
            // Disable the button
            menu.beginner.setEnabled(false);
			menu.beginner.setText("Beginner: board 7x9 and 10 mines");
            // Set the Game variables
            no_of_rows = 6;
            no_of_Columns = 9;
            no_of_Mines = 11;
            no_of_sec = 60;
            // Switch the main menu panel to the game panel
            switchPanels(new gamePanel());
            gamePanel.returnButton.addActionListener(this);
        } 
        // User selected normal difficulty
        else if (e.getSource() == menu.advanced) {
            menu.advanced.setEnabled(false);
			menu.advanced.setText("Advanced: board 13x18 and 35 mines");
            no_of_rows = 12;
            no_of_Columns = 18;
            no_of_Mines = 36;
            no_of_sec = 180;
            switchPanels(new gamePanel());
            gamePanel.returnButton.addActionListener(this);
        } 
        // User selected hard difficulty
        else if (e.getSource() == menu.expert)
			{
            menu.expert.setEnabled(false);
			menu.expert.setText("Expert: board 22x25 and 91 mines");         
            no_of_rows = 21;
            no_of_Columns = 26;
            no_of_Mines = 92;
            no_of_sec = 660;
            switchPanels(new gamePanel());
            gamePanel.returnButton.addActionListener(this);
			} 
    }

    /**
     * Switches panels between main menu and the game
     * Return type: returns nothing (void)
     * @param addPanel: The panel to add to the screen
    */
    public void switchPanels(JPanel addPanel) {
        // Remove all components
        this.getContentPane().removeAll();
        // Add the new panel
        this.add(addPanel);
        // Pack the contents and repaint the GUI
        this.pack();
        this.repaint();
    }
}
