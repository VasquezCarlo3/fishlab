import java.awt.Color;
public class Particle 
{
	protected Color color;
	protected Particle[][] grid;
	public Particle(Particle[][] grid)
	{
		this.grid = grid;
	}
	public Color getColor()
	{
		return color;
	}
	public void act(int row, int col) {}	
	public void swap(int fromRow, int fromCol, int toRow, int toCol)
	{
//toCol < grid[toRow].length && toCol - 1 < 0
		if(grid[toRow][toCol] == null || grid[toRow][toCol] instanceof Water) //include water when we get that far
		{
		Particle part = grid[fromRow][fromCol];
		grid[fromRow][fromCol] = grid[toRow][toCol];
		grid[toRow][toCol] = part;
		}
	}
}