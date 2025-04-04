import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SandPanel implements ChangeListener, MouseListener, MouseMotionListener, ActionListener 
{
    private JFrame frame;
    private JPanel canvas;
    private JPanel particleButtonPanel;
    private JPanel fileButtonPanel;    
    private JButton save;
    private JButton load;
    private JSlider slider;
    private JButton[] buttons;

    private int speed;
    private int tool;
    private int[] mouseLoc;
    private final int pixelSize = 3;
    private int rows;
    private int cols;

    private Particle[][] grid;

    private static SandLab sandLab;

    public SandPanel(String title, int rows, int cols)
    {
        grid = new Particle[rows][cols];
        System.out.println("New Sand Panel");
        this.rows = rows;
        this.cols = cols;
        tool = 0;
        //initialize the mouse x, y array
        mouseLoc = new int[2];
        mouseLoc[0] = -1;
        //set up the buttons
        String[] names = sandLab.getNames();
        buttons = new JButton[names.length];
        for(int i = 0; i < names.length; i++)
        {
            buttons[i] = new JButton(names[i]);
            buttons[i].setActionCommand("" + i);
            buttons[i].addActionListener(this);
        }
        //the window frame
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1300, 700);

        //the canvas where the particles are drawn
        canvas = new JPanel() {
            @Override
            public void paintComponent(Graphics g)
            {
                for(int row = 0; row < grid.length; row++)
                {
                    for(int col = 0; col < grid[0].length; col++)
                    {
                        Particle p = grid[row][col];
                        if(p == null)
                        {
                            g.setColor(Color.black);
                        }
                        else
                        {
                            g.setColor(p.getColor());
                        }
                        g.fillRect(col * pixelSize, row * pixelSize, pixelSize, pixelSize);
                    }
                } 
            }
        };
        canvas.setSize(new Dimension(pixelSize*cols, pixelSize*rows));
        canvas.setBackground(Color.black);
        //add the mouse listeners to the canvas
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);

        //panel to hold the two button panels, 
        //one for the particle buttons and one for save/load buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1));
        
        //panel to hold the particle buttons
        particleButtonPanel = new JPanel();
        particleButtonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        for(JButton b : buttons)
            particleButtonPanel.add(b, gbc);
        
        //panel to hold the save and load buttons
        fileButtonPanel = new JPanel();
        fileButtonPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        save = new JButton("Save");
        save.setActionCommand("Save");
        save.addActionListener(this);
        fileButtonPanel.add(save, gbc);
        load = new JButton("Load");
        load.setActionCommand("Load");
        load.addActionListener(this);
        fileButtonPanel.add(load, gbc);

        //add the two button panels to the button panel
        buttonPanel.add(particleButtonPanel);
        buttonPanel.add(fileButtonPanel);

        //create the slider for speed adjustment
        //range from 0 to 100 initially set to 50
        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        slider.addChangeListener(this);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true); 

        //add all of the stuff to the frame
        frame.add(canvas, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.EAST);
        frame.add(slider, BorderLayout.SOUTH);
        frame.setVisible(true);
    
        //initialize the speed based on the slider's default value
        speed = calculateSpeed();
    }
    public void update(Particle[][] grid)
    {
        this.grid = grid;
        canvas.repaint();
    }
    //get methods
    public int getSpeed()
    {
        return speed;
    }
    public int getTool()
    {
        return tool;
    }
    public int[] getMouseLocation()
    {
        if(mouseLoc[0] >= rows || mouseLoc[0] < 0 || mouseLoc[1] >= cols || mouseLoc[1] < 0)
            return null;
        return mouseLoc;
    }
    //this is the callback from the button clicks
    public void actionPerformed(ActionEvent e)
    {
        if(e.getActionCommand().equals("Save"))
        {
            sandLab.save();
            return;
        }
        else if(e.getActionCommand().equals("Load"))
        {
            sandLab.load();
            return;
        }
        tool = Integer.parseInt(e.getActionCommand());
        for (JButton button : buttons)
            button.setSelected(false);
        ((JButton)e.getSource()).setSelected(true);
    }
    //pause for a li'l bit
    private void pause(int milliseconds)
    {
      try
      {
         Thread.sleep(milliseconds);
      }
      catch(InterruptedException e)
      {
         throw new RuntimeException(e);
      }
    }
    //helper method that has the speed calculation function from the slider
    private int calculateSpeed()
    {
        return (int)(Math.pow(10, 0.03 * slider.getValue() + 3));
    }
    //callback for changing the slider
    @Override
    public void stateChanged(ChangeEvent e) 
    {
       speed = calculateSpeed();
    }
    //methods from the MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        mouseLoc[1] = e.getX() / pixelSize;
        mouseLoc[0] = e.getY() / pixelSize;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        mouseLoc[0] = -1;
        mouseLoc[1] = -1;
    }
    //methods from the MouseMotionListener
    @Override 
    public void mouseDragged(MouseEvent e) {
        mouseLoc[1] = e.getX() / pixelSize;
        mouseLoc[0] = e.getY() / pixelSize;
    }
    @Override
    public void mouseMoved(MouseEvent e) {}
    //the loop
    public void run()
    {
        while (true)
        {
            for (int i = 0; i < getSpeed(); i++)
                sandLab.step();
            sandLab.updateDisplay(this);
            pause(1);  //wait for redrawing and for mouse
            int[] mouseLoc = getMouseLocation();
            if (mouseLoc != null)  //test if mouse clicked
                sandLab.locationClicked(mouseLoc[0], mouseLoc[1], tool);
        }
    }
    //where the magic begins
    public static void main(String[] args)
    {
        int rows = 180;
        int cols = 360;
        sandLab = new SandLab(rows, cols);
        SandPanel panel = new SandPanel("Sand Lab", rows, cols);
        panel.run();
    }
}