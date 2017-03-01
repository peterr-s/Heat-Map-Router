package net.schoener.peter.hmr;

import java.util.Set;

/**
 * Represents a point on a map. Can be a target point or not. Has a score representing how easy it is to get from it to a target.
 * 
 * @author peterr
 */
public class Point
{
	// square root of 2 (for diagonal distances)
	private static final double DIAG_CONST = 1.414213562373095048801688724209698078569671875376948073176;
	
	// because it's used multiple times
	private Set<Point> outposts;
	
	private int x, y;
	private Point bestDirectionOfEntry;
	private double outpostProximity;
	
	/**
	 * makes a new point
	 * requires x and y because these should not be set later
	 * 
	 * @param nX - the x coordinate
	 * @param nY - the y coordinate
	 */
	public Point(int nX, int nY, Set<Point> nOutposts, Point defaultEntry)
	{
		// by default this is a target point or one that has not been routed to
		bestDirectionOfEntry = defaultEntry;
		
		// if no outposts are provided, this is an outpost
		outposts = nOutposts;
		outpostProximity = 0;
		if(outposts != null)
		{
			// add the sum of the reciprocals of the squares of the radii from each outpost
			for(Point outpost : outposts)
				outpostProximity += 1 / (Math.pow(x - outpost.getX(), 2) + Math.pow(y - outpost.getX(), 2));
		}
		
		x = nX;
		y = nY;
	}
	
	/**
	 * gets the score
	 * score represents the ease of getting to the closest target point
	 * 
	 * @return this point's score
	 */
	public double getScore()
	{
		double score = 0;
		
		// if this is not a target point
		if(bestDirectionOfEntry != null)
		{
			// then the score starts as the score until here
			score = bestDirectionOfEntry.getScore();
			
			score += incrementalScore(bestDirectionOfEntry);
		}
		
		return score;
	}
	
	/**
	 * incremental score moving from an adjacent point
	 * 
	 * @param from - the point from which to measure
	 * @return the amount by which the score increases between the two points
	 */
	private double incrementalScore(Point from)
	{
		return outpostProximity * (from.getX() == x || from.getY() == y ? 1 : DIAG_CONST);
	}
	
	/**
	 * whether or not the shortest route to this point passed through a given point
	 * 
	 * @param p - the point to check for
	 * @return
	 */
	public boolean passedThrough(Point p)
	{
		// if this is the point then yes, clearly
		if(equals(p))
			return true;
		
		// recursively go down the chain
		if(bestDirectionOfEntry != null)
			return bestDirectionOfEntry.passedThrough(p);
		
		// if it's not found then guess not
		return false;
	}
	
	/**
	 * sets a new approach if it's better
	 * 
	 * @param nEntry - the approach to try
	 * @return true if the score was indeed lower, else false
	 */
	public boolean tryBetterPath(Point nEntry)
	{
		// check if this would be a better or equally good way to get to this point
		// equally good is allowed because of the way redundancy checking is done in the main method; else the program would jam and die
		if(nEntry.getScore() + incrementalScore(nEntry) <= getScore())
		{
			// if so, change the old direction and indicate that it was a success
			bestDirectionOfEntry = nEntry;
			return true;
		}
		
		//else false
		return false;
	}
	
	/**
	 * this is the key point of this class
	 * the hash code is based on the coordinates, NOT the score
	 * 
	 * @return a hash code based on x and y
	 */
	public int hashCode()
	{
		return ("" + x + ", " + y).hashCode();
	}
	
	/**
	 * getter for x
	 * 
	 * @return x
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * getter for y
	 * 
	 * @return y
	 */
	public int getY()
	{
		return y;
	}
	
	/**
	 * returns a String representation of this point (just the coordinates)
	 * 
	 * @return a coordinate pair in parentheses (comma delimited)
	 */
	public String toString()
	{
		return "(" + x + ", " + y + ")" + (bestDirectionOfEntry == null ? "" : " -> " + bestDirectionOfEntry.toString());
	}
	
	/**
	 * returns true if the parameter is a point with the same coordinates
	 * 
	 * @return true if match, else false
	 */
	public boolean equals(Object o)
	{
		// points are same iff they have the same coordinates
		return (o instanceof Point) && ((Point)o).getX() == x && ((Point)o).getY() == y;
	}
	
	/*public Point getBestDirectionOfEntry()
	{
		return bestDirectionOfEntry;
	}*/
	
	public Point clone()
	{
		return new Point(x, y, outposts, bestDirectionOfEntry);
	}
}
