package net.schoener.peter.hmr;

/**
 * Represents a point on a map. Can be a target point or not. Has a score representing how easy it is to get from it to a target.
 * 
 * @author peterr
 */
public class Point
{
	private double score;
	private int x, y;
	
	/**
	 * makes a new point
	 * requires x and y because these should not be set later
	 * 
	 * @param nX - the x coordinate
	 * @param nY - the y coordinate
	 */
	public Point(int nX, int nY)
	{
		score = Double.MAX_VALUE; // use the maximum value so that the first attempt to overwrite works
		
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
		return score;
	}
	
	/**
	 * sets the score if the new one is lower
	 * 
	 * @param nScore
	 */
	public void tryLowerScore(double nScore)
	{
		if(nScore < score)
			score = nScore;
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
}
