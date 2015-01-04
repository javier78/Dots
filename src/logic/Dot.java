package logic;
import logic.Board.Dot_Color;
public class Dot 
{
	private Dot_Color color;
	private int xcoordinate, ycoordinate;
	private boolean visited;
	private int referenceCount;
	
	public static final int NORTH=0;
	public static final int SOUTH=1;
	public static final int EAST=2;
	public static final int WEST=3;
	
	public Dot(Dot_Color c, int x, int y)
	{
		color=c;
		visited=false;
		xcoordinate=x;
		ycoordinate=y;
		referenceCount=0;
	}
	
	public Dot(Dot_Color c)
	{
		color=c;
		visited=false;
		referenceCount=0;
	}

	public boolean isVisited() {
		return visited;
	}
	
	public int getReferenceCount()
	{
		return referenceCount;
	}
	
	public void incrementReferenceCount()
	{
		referenceCount++;
	}
	
	public void decrementReferenceCount()
	{
		referenceCount--;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public Dot_Color getColor() {
		return color;
	}
	
	public int getX()
	{
		return xcoordinate;
	}
	
	public int getY()
	{
		return ycoordinate;
	}
	
	public void setLocation(int x, int y)
	{
		xcoordinate=x;
		ycoordinate=y;
	}
	/*
	public boolean equals(Object o)
	{
		if(o==null || !(o instanceof Dot))
			return false;
		
		Dot c=(Dot)o;
		return c.getX()==this.getX() && c.getY()==this.getY();
	}
	*/
}
