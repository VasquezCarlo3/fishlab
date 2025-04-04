public class Clear extends Particle
{
    public Clear(Particle[][] grid)
    {
        super(grid);
    }
    @Override
    public void act(int row, int col)
    {
        for(int r = 0; r < grid.length; r++)
        {
            for(int c = 0; c < grid[0].length; c++)
            {
                grid[r][c] = null;
            }
        }
    }
}
