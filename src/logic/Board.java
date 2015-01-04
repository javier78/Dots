package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Board 
{
	public enum Dot_Color{RED,YELLOW,BLUE,GREEN,PURPLE};
	private ArrayList<ArrayList<Dot>> board;
	public Board()
	{
		board=new ArrayList<ArrayList<Dot>>();
		for(int x=0;x<6;x++)
		{
			ArrayList<Dot> it=new ArrayList<Dot>(6);
			board.add(it);
			for(int y=0;y<6;y++)
			{
				Dot_Color c=randomColor(-1);
				System.out.println("Row "+x+": "+c);
				it.add(new Dot(c, x, y));
			}
		}
		System.out.println(this);
	}

	public Dot getDot(int x,int y)
	{
		return board.get(x).get(y);
	}

	public static Dot_Color randomColor(int reject)
	{
		
		Dot_Color[] values=Dot_Color.values();
		int pick=new Random().nextInt(values.length);
		while(pick==reject)
			pick=new Random().nextInt(values.length);
		return values[pick];
		//return Dot_Color.BLUE;
	}
	
	public void clearDot(int x, int y)
	{
		Dot d=this.getDot(x, y);
		ArrayList<Dot> change=board.get(d.getX());
		change.remove(d.getY());
		for(int z=y;z<5;z++)
			change.get(z).setLocation(5-x, z);
		
		change.add(new Dot(randomColor(-1),5-x,5));	//TODO: Only supports clearing of one dot in arr list!
	}
	
	/**
	 * Takes in a column to make the deletions in, and the dots in this column will already be marked as visited.
	 * @param x The column to work on.
	 */
	public void clearDotsInArrList(int x,int reject)
	{
		ArrayList<Dot> d=board.get(x);
		/*
		for(int z:y)
			d.get(z).setVisited(true);
		*/
		int dec=0;
		for(int z=0;z<6;z++)	//TODO: May not need this loop, locations may be useless.
		{
			if(d.get(z).getReferenceCount()>0)
				dec++;	//dec is still needed! Find a way to obtain dec without this loop.
			else
				d.get(z).setLocation(x, z-dec);
		}
		for(int z=5;z>=0;z--)
		{
			if(d.get(z).getReferenceCount()>0)
				d.remove(z);
		}
		
		for(int z=dec-1;z>=0;z--)
			d.add(new Dot(randomColor(reject),0,0));
		//d.add(new Dot(randomColor(reject),0,0));
			
	}
	
	public void clearDots()
	{
		for(int x = 0; x < 6; x++)
		{
			clearDotsInArrList(x, -1);
		}
	}
	
	public void closingClear(Dot_Color c)
	{
		int found=0;
		//for(int x=0;x<board.size();x++)
		int reject=-1;
		switch(c)
		{
		case RED:
			reject=0;
			break;
		case YELLOW:
			reject=1;
			break;
		case BLUE:
			reject=2;
			break;
		case GREEN:
			reject=3;
			break;
		case PURPLE:
			reject=4;
			break;
		}
		for(int x=0;x<6;x++)
		{
			//for(int y=0;y<board.get(x).size();y++)
			for(int y=0;y<6;y++)
			{
				if(board.get(x).get(y).getColor()==c)
				{
					board.get(x).get(y).incrementReferenceCount();
					found++;
				}
			}
			clearDotsInArrList(x,reject);
		}
		System.out.println("Found: "+found);
	}
	
	public int getReferences()
	{
		int count=0;
		for(int x=0;x<6;x++)
		{
			for(Dot d:board.get(x))
			{
				count+=d.getReferenceCount();
			}
			
		}
		return count;
	}
	
	public Dot[] getNeighbors(int x,int y)
	{
		Dot[] neighbors=new Dot[4];
		if(y+1<6)
			neighbors[0]=getDot(x, y+1);
		if(y-1>=0)
			neighbors[1]=getDot(x,y-1);
		if(x-1>=0)
			neighbors[2]=getDot(x-1, y);
		if(x+1<6)
			neighbors[3]=getDot(x+1, y);
		return neighbors;
	}

	public String toString()
	{
		String toReturn="";
		for(int x=5;x>=0;x--)
		{
			for(int y=0;y<6;y++)
			{
				Dot z=this.getDot(y, x);
				switch(z.getColor())
				{
				case RED:
					toReturn+="R ";
					break;
				case BLUE:
					toReturn+="B ";
					break;
				case PURPLE:
					toReturn+="P ";
					break;
				case YELLOW:
					toReturn+="Y ";
					break;
				case GREEN:
					toReturn+="G ";
					break;
				}
			}
			toReturn+='\n';
		}
		return toReturn;
	}
}
