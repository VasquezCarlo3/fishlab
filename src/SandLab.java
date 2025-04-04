import java.awt.Color;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Project and code modified from
 * http://nifty.stanford.edu/2017/feinberg-falling-sand/assignmentwithoutarrays.html
 *
 */

public class SandLab
{
  
  //add constants for particle types here
  public static final int EMPTY = 0;
  public static final int METAL = 1;
  public static final int SAND = 2;
  public static final int WATER = 3;
  public static final int SNOW = 4;
  public static final int WOOD = 5;
  public static final int FROG = 6;
  public static final int CLEAR = 7;

  //The save file
  private static final String FILE = "save.txt";
  
  private String[] names;
  private Particle[][] grid;
  
  public SandLab(int numRows, int numCols)
  {
    //Change this whenever you add a new particle.
    //Increase the size of the array, add a new static int (above) and initialize the array element below.
	  names = new String[8];
	  names[EMPTY] = "Empty";
	  names[METAL] = "Metal";
    names[SAND] = "Sand";
    names[WATER] = "Water";
    names[SNOW] = "Snow";
    names[WOOD] = "Wood";
    names[FROG] = "Infestation";
    names[CLEAR] = "Clear";

    grid = new Particle[numRows][numCols];

  }
  
  //called when the user clicks on a location, tool is the particle type
  //Every time you add a new particle add a new case statment.
  //Make sure each case statement ends with break.
  public void locationClicked(int row, int col, int tool)
  {
	  Particle particle = null;
	   switch(tool)
	   {
	   	case METAL:
		    particle = new Metal(grid);
		    break;
        case SAND:
        particle = new Sand(grid);
        break;
        case WATER:
        particle = new Water(grid);
        break;
        case SNOW:
        particle = new Snow(grid);
        break;
        case EMPTY:
        for(int r = row-3; r <= row + 3; r++) // wood has a hole in the middle that needs fix
            {
              for(int c = col-3; c <= col+3; c++)
              {
                if(row +1 < grid.length && col + 1 < grid[0].length && col-1 >= 0 && row > 0)
                {
                    grid[r][c] = null;
                }
              }
            }
        particle = null;
        break;
        case FROG:
        for(int r = row-3; r <= row + 3; r++) // wood has a hole in the middle that needs fix
            {
              for(int c = col-3; c <= col+3; c++)
              {
                if(row +1 < grid.length && col + 1 < grid[0].length && col-1 >= 0 && row > 0)
                {
                    grid[r][c] = new Infestation(grid);
                }
              }
            }
        particle = new Infestation(grid);
        break;
        case WOOD:
            for(int r = row-1; r <= row + 1; r++) // wood has a hole in the middle that needs fix
            {
              for(int c = col-1; c <= col+1; c++)
              {
                if(row +1 < grid.length && col + 1 < grid[0].length && col-1 >= 0 && row > 0)
                {
                    grid[r][c] = new Wood(grid);
                }
              }
            }
            particle = new Wood(grid);
        break;
        case CLEAR:
        particle = new Clear(grid);
        break;
	   }
	   grid[row][col] = particle;
     /*if(grid[row][col] instanceof Wood && row +1 < grid.length && col +1 < grid[0].length)
     {
        grid[row+1][col] = particle;
        grid[row][col+1] = particle;
        grid[row+1][col+1] = particle;
        if(row+2 < grid.length && col+2 < grid[0].length)
        {
          grid[row+2][col+1] = particle;
          grid[row+1][col+2] = particle;
          grid[row+2][col+2] = particle;
          grid[row+2][col] = particle;
          grid[row][col+2] = particle;
        }
     }*/
  }

  //tells display what color to show for each value in the display
  public void updateDisplay(SandPanel display)
  {
      display.update(grid);
  }

  //called repeatedly.
  //causes one random particle to maybe do something.
  public void step()
  {
    int row = (int)(Math.random() * grid.length);
    int col = (int)(Math.random() * grid[0].length);
    if(grid[row][col] != null)
    {
      grid[row][col].act(row, col);
    }
  }
  //write the current state of the canvas to a text file
  public void save() 
  {
    File file = new File(FILE);
    try{
      FileWriter writer = new FileWriter(file);
      for(Particle[] row : grid)
      {
        for(Particle particle : row)
        {
          if(particle == null)
          {
            writer.write(" ");
          }
          else if(particle instanceof Metal)
          {
            writer.write("m");
          }
          else if(particle instanceof Sand)
          {
            writer.write("s");
          }
          else if(particle instanceof Snow)
          {
            writer.write("n");
          }
          else if(particle instanceof Water)
          {
            writer.write("w");
          }
          else if(particle instanceof Wood)
          {
            writer.write("t");
          }
          else if(particle instanceof Infestation)
          {
            writer.write("i");
          }
        }
        writer.write("\n");
      }
      writer.close();
    }
      catch(IOException e){
        e.printStackTrace();
      }
  } 
  public void load()
  {
    File file = new File(FILE);
    try
    {
      Scanner read = new Scanner(file);
      while(read.hasNextLine())
      {
        String line = "";
        for(int i = 0; i < grid.length; i++)
        {
          line = read.nextLine();
          for(int j = 0; j + 1 < grid[i].length; j++)
          {
            String type = line.substring(j, j+1);
            if(type.equals(" "))
            {
              grid[i][j] = null;
            }
            else if(type.equals("m"))
            {
              grid[i][j] = new Metal(grid);
            }
            else if(type.equals("s"))
            {
              grid[i][j] = new Sand(grid);
            }
            else if(type.equals("n"))
            {
              grid[i][j] = new Snow(grid);
            }
            else if(type.equals("w"))
            {
              grid[i][j] = new Water(grid);
            }
            else if(type.equals("t"))
            {
              grid[i][j] = new Wood(grid);
            }
            else if(type.equals("i"))
            {
              grid[i][j] = new Infestation(grid);
            }
          }
        }
        System.out.println(line);
      }
      read.close();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
  }
  public String[] getNames()
  {
    return names;
  }
}
