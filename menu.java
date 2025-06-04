// Student Name: uday Silapuram 
// Student ID: 9999-03351
// Course Name: Advanced Programming Concepts


// import required default packages
import javax.swing.*;
import java.awt.*;

/**
 * This is a menu Java class.
 *
 * @author Uday Silapuram
 */
public class menu extends JPanel 
{
    public JPanel levelPanel;
    public JLabel heading;
	public JPanel headingPanel;
    public static JButton beginner, advanced, expert;
    //  declaring Variables for panel
    public final int wdt = 1000;
    public final int Height = 600;

    // declaring  Variables for button
    public final int BUTTON_WIDTH = 150;
    public final int BUTTON_HEIGHT = 40;  
    /**
     * initialize all the components in the panel
	 *@param no parameter is passed
	 *@return nothing is returned
    */
    public void initialize() {
      

        // Panels
        headingPanel = new JPanel();
        headingPanel.setPreferredSize(new Dimension(wdt, Height / 5));
        headingPanel.setBackground(Color.GREEN);
        headingPanel.setLayout(new GridBagLayout()); // Layout that helps center the text

        levelPanel = new JPanel();
        levelPanel.setPreferredSize(new Dimension(550, Height));
        levelPanel.setBackground(Color.lightGray);
        levelPanel.setLayout(null);

      

        // Buttons
        beginner = new JButton();
        beginner.setText("BEGINNER");
       
        beginner.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        beginner.setBounds(new Rectangle(120, 80, BUTTON_WIDTH, BUTTON_HEIGHT));
        beginner.setForeground(Color.GREEN);
        beginner.setFocusable(false); //  button should not be focused 
        beginner.setFont(new Font(beginner.getFont().getName(), Font.BOLD, 20));

        advanced = new JButton();
        advanced.setText("ADVANCED");
        
        advanced.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        advanced.setBounds(new Rectangle(120, 140, BUTTON_WIDTH, BUTTON_HEIGHT));
        advanced.setForeground(Color.PINK);
        advanced.setFocusable(false); //  button should not be focused 
        advanced.setFont(new Font(advanced.getFont().getName(), Font.BOLD, 20));

        expert = new JButton();
        expert.setText("EXPERT");
     
        expert.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        expert.setBounds(new Rectangle(120, 200, BUTTON_WIDTH, BUTTON_HEIGHT));
        expert.setForeground(Color.BLUE);
        expert.setFocusable(false); // button should not be focused 
        expert.setFont(new Font(expert.getFont().getName(), Font.BOLD, 20));

        // Labels
        heading = new JLabel();
        heading.setText("Welcome to Minesweeper game");
        heading.setFont(new Font(heading.getFont().getName(), Font.CENTER_BASELINE, 30));
        heading.setForeground(Color.BLUE);


        // Add Components
        headingPanel.add(heading);
   
        levelPanel.add(beginner);
        levelPanel.add(advanced);
        levelPanel.add(expert);
       

    }	

    /** 
     * 
     * zero argument constructor 
	 *@param no parameters 
	 *@return nothing is returned
    */
    
    menu() {

        // initialize all the components in the panel
        initialize();
        
        // Panel Properties
        this.setPreferredSize(new Dimension(wdt, Height));  // Sets the preferred size of the JPanel
        this.setBackground(Color.white);  // Sets the background color to gray
        this.setLayout(new BorderLayout());  // Sets the border layout for the panel
        
        // Add Components to Panel
        this.add(headingPanel, BorderLayout.NORTH);
        this.add(levelPanel, BorderLayout.CENTER);
        

    }


    
    
   

}
