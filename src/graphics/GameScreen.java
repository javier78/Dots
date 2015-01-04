package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JFrame;

import logic.Board;
import logic.Dot;

public class GameScreen extends JFrame{
	JFrame frame=new JFrame();
	Board b=new Board();
	GamePanel game=new GamePanel();
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		GameScreen screen=new GameScreen();
		screen.go();
	}

	public void go(){
		frame.getContentPane().setBackground(Color.BLACK);
		frame.getContentPane().add(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(380, 350);
		frame.setVisible(true);
	}
	private class GamePanel extends JComponent implements MouseListener,MouseMotionListener
	{
		private static final long serialVersionUID = -2236646551274002890L;
		boolean isDragging=false;
		int closedFigures=0;
		Ellipse2D.Double[][] dots;
		Point currentDot;
		Stack<Point> path;
		Stack<Line2D.Double> lines;
		Line2D.Double currentLine;
		double startx,starty;
		Board.Dot_Color pathColor;
		private boolean dotClicked;
		public GamePanel()
		{
			super();
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
			dots=new Ellipse2D.Double[6][6];
			lines=new Stack<Line2D.Double>();
			currentLine=new Line2D.Double();
			currentDot=new Point();
			path=new Stack<Point>();
		}

		public void paintComponent(Graphics g)
		{
			Graphics2D g2d=(Graphics2D) g;
			int currx=80;
			int curry=240;
			int inc=40;
			
			for(int x=0;x<6;x++)
			{
				for(int y=0;y<6;y++)
				{
					Dot z=b.getDot(x,y);
					Board.Dot_Color s=z.getColor();
					switch(z.getColor())
					{
					case YELLOW:
						g2d.setColor(Color.YELLOW);
						break;
					case RED:
						g2d.setColor(Color.RED);
						break;
					case BLUE:
						g2d.setColor(Color.BLUE);
						break;
					case PURPLE:
						g2d.setColor(new Color(170,0,204));
						break;
					case GREEN:
						g2d.setColor(Color.GREEN);
						break;
					}	
					dots[x][y]=new Ellipse2D.Double(currx,curry,20,20);
					dots[x][y].height=20;
					dots[x][y].width=20;
					dots[x][y].x=currx;
					dots[x][y].y=curry;
					g2d.fill(dots[x][y]);
					curry-=inc;
				}
				currx+=inc;
				curry=240;
			}
			g2d.setColor(Color.WHITE);
			for(Line2D.Double l:lines)
				g2d.draw(l);
			g2d.draw(currentLine);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			for(int x=0;x<6;x++)
			{
				for(int y=0;y<6;y++)
					if(dots[x][y].contains(e.getX(), e.getY()))
					{
						System.out.println(b.getDot(x, y).getColor()+" Dot clicked!");
					}
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			for(int x=0;x<6;x++)
			{
				for(int y=0;y<6;y++)
					if(dots[x][y].contains(e.getX(), e.getY()))
					{
						System.out.println(b.getDot(x, y).getColor()+" Dot clicked!");
						startx=dots[x][y].getCenterX();
						starty=dots[x][y].getCenterY();
						//System.out.println(dots[x][y].getCenterX());
						//System.out.println(dots[x][y].getCenterY());
						currentDot.x=x;
						currentDot.y=y;
						pathColor=b.getDot(x, y).getColor();
						b.getDot(x, y).incrementReferenceCount();
						dotClicked=true;
						System.out.println("Current color: "+pathColor);
						repaint();
						return;
					}
			}
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			if(closedFigures>0)
				b.closingClear(pathColor);
			else if(path.size()>0)
				b.clearDots();
			if(dotClicked && path.size()==0)
			{
				b.getDot(currentDot.x, currentDot.y).decrementReferenceCount();
			}
			dotClicked=false;
			closedFigures=0;
			isDragging=false;
			currentLine.setLine(0, 0, 0, 0);
			path.clear();
			lines.clear();
			currentDot.x=0;
			currentDot.y=0;
			System.out.println("References "+b.getReferences());
			repaint();
		}
		
		
		
		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if(isDragging)
			{
				currentLine.setLine(startx, starty, arg0.getX(), arg0.getY());
				ArrayList<Point> neighbors=getNeighbors(currentDot);
				//System.out.println("Size of neighbors: "+neighbors.size());
				for(Point p:neighbors)
				{
					if(closedFigures>0 && b.getDot(p.x, p.y).getReferenceCount()>0 && !path.peek().equals(p))
					{
						System.out.println("All true!");
						continue;
					}
					if(dots[p.x][p.y].contains(arg0.getX(),arg0.getY()))
					{
						if(path.size()>0 && path.peek().equals(p))
						{
							
							currentLine=lines.pop();
							startx=currentLine.x1;
							starty=currentLine.y1;
							b.getDot(currentDot.x,currentDot.y).decrementReferenceCount();
							if(b.getDot(currentDot.x,currentDot.y).getReferenceCount()>0)
								closedFigures--;
							currentDot=path.pop();
							return;
						}
						if(b.getDot(p.x, p.y).getReferenceCount()>0)
						{
							System.out.println("Closed figure created!");
							closedFigures++;
						}
						currentLine.setLine(startx, starty,dots[p.x][p.y].getCenterX(),dots[p.x][p.y].getCenterY());
						path.push(currentDot);
						b.getDot(p.x, p.y).setVisited(true);
						b.getDot(p.x, p.y).incrementReferenceCount();
						lines.push(currentLine);
						Ellipse2D.Double curr=dots[p.x][p.y];
						startx=curr.getCenterX();
						starty=curr.getCenterY();
						currentLine=new Line2D.Double(startx,starty,curr.getCenterX(),curr.getCenterY());
						currentDot=p;
						repaint();
						return;
					}
				}
			}
			if(dots[currentDot.x][currentDot.y].contains(arg0.getX(),arg0.getY()))
			{
				isDragging=true;
			}
			repaint();
		}
		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public ArrayList<Point> getNeighbors(Point p)
		{
			ArrayList<Point> neighbors=new ArrayList<Point>(4);
			if(p.y+1<6 && pathColor==b.getDot(p.x, p.y+1).getColor())
			{
				//System.out.println("Path color: "+pathColor);
				//System.out.println("East: Neighbor Color: "+b.getDot(p.x, p.y+1).getColor());
				neighbors.add(new Point(p.x,p.y+1));
			}
			if(p.y-1>=0 && pathColor==b.getDot(p.x, p.y-1).getColor())
			{
				//System.out.println("Path color: "+pathColor);
				//System.out.println("West: Neighbor Color: "+b.getDot(p.x, p.y-1).getColor());
				neighbors.add(new Point(p.x,p.y-1));
			}
			if(p.x-1>=0 && pathColor==b.getDot(p.x-1, p.y).getColor())
			{
				//System.out.println("Path color: "+pathColor);
				//System.out.println("North: Neighbor Color: "+b.getDot(p.x-1, p.y).getColor());
				neighbors.add(new Point(p.x-1, p.y));
			}
			if(p.x+1<6 && pathColor==b.getDot(p.x+1, p.y).getColor())
			{
				//System.out.println("Path color: "+pathColor);
				//System.out.println("South: Neighbor Color: "+b.getDot(p.x+1, p.y).getColor());
				neighbors.add(new Point(p.x+1, p.y));
			}
			return neighbors;
		}
	}
}
