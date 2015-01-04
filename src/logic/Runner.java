package logic;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) throws InterruptedException 
	{
		// TODO Auto-generated method stub
		Board b=new Board();
		System.out.println(b);
		//Scanner kb=new Scanner(System.in);
		//System.out.println("Clear which top level dot?");
		System.out.println("Observe the board...");
		Thread.sleep(1000);
		System.out.println("Clearing all red!");;
		b.closingClear(Board.Dot_Color.RED);
		System.out.println(b);
		System.out.println("Clearing purples now!");
		Thread.sleep(1000);
		b.closingClear(Board.Dot_Color.PURPLE);
		//b.clearDotsInArrList(0, arr);
		System.out.println(b);
		/*
		while(true)
		{
			try{
				int x=kb.nextInt();
				int y=kb.nextInt();
				System.out.println("Clearing "+b.getDot(x, y).getColor()+" dot.");
				System.out.println("Clearing at loc: "+b.getDot(x, y).getX()+" "+b.getDot(x, y).getY());
				b.clearDot(x, y);
				System.out.println(b);
			}
			catch(InputMismatchException ime)
			{
				System.out.println("quitting");
				return;
			}
		}
		*/
		
	}

}
