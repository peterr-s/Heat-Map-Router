package net.schoener.peter.hmr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * does everything in a CLI because my JRE is screwy
 * 
 * @author peterr
 */
public class Main
{
	// error codes
	private static final int ARG_ERR = 1,
			FILE_ERR = 2;
	
	public static void main(String[] args)
	{
		Queue<Point> toSearch = new LinkedList<>();
		Set<Point> outposts = new HashSet<>(),
				targets = new HashSet<>(),
				checkedPoints = new HashSet<>();
		Point bestEntryPoint = null;
		
		// get arguments
		if(args.length < 3)
		{
			System.err.println("Usage: java -jar HeatMapRouter.jar [targets] [outposts] [distance]");
			System.exit(ARG_ERR);
		}
		int distance = 0; // distance from targets to search
		try
		{
			distance = Integer.parseInt(args[2]);
		}
		catch(NumberFormatException e)
		{
			System.err.println(args[2] + " is not a valid distance");
			System.exit(ARG_ERR);
		}
		
		// read files
		try
		{
			// read the outpost locations first so that they're available for the targets
			BufferedReader outpostReader = new BufferedReader(new FileReader(new File(args[1])));
			String nLine;
			while((nLine = outpostReader.readLine()) != null)
			{
				String[] fields = nLine.split("\\s+");
				outposts.add(new Point(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), null, null));
			}
			outpostReader.close();
			
			// read the target points
			BufferedReader targetReader = new BufferedReader(new FileReader(new File(args[0])));
			while((nLine = targetReader.readLine()) != null)
			{
				String[] fields = nLine.split("\\s+");
				Point nTarget = new Point(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), outposts, null);
				targets.add(nTarget);
				toSearch.add(nTarget);
			}
			targetReader.close();
			
			
		}
		catch (Exception e)
		{
			System.err.println("Error reading file: " + e.getMessage());
			System.exit(FILE_ERR);
		}
		
		//System.out.println(toSearch.toString());
		
		while(!toSearch.isEmpty())
		{
			Point temp = toSearch.poll();
			
			// DEBUG: show route and length
			//System.out.println(toSearch.size() + "\n\t[" + temp.toString() + "]: " + temp.getScore());
			
			// for each combination of x and y offsets
			for(int i = -1; i <= 1; i++)
			{
				for(int j = -1; j <= 1; j++)
				{
					// don't move from the point to itself
					if(i == 0 && j == 0)
						continue;
					
					// don't backtrack
					Point nPoint = new Point(temp.getX() + i, temp.getY() + j, outposts, temp);
					if(temp.passedThrough(nPoint))
						continue;
					
					// don't go through another target
					if(targets.contains(nPoint))
						continue;
										
					//System.out.println(i + ", " + j);

					// if this point has been processed, compare the new route to it, else add it to the list of processed points
					checkedPoints.add(nPoint);
					for(Point p : checkedPoints)
						if(p.equals(nPoint))
						{
							//System.out.println("\t[" + nPoint.toString() + "] was in there");
							nPoint = p;
						}
					if(!nPoint.tryBetterPath(temp)) // if the new route was not better, no need to check it again because nothing changed
						continue;
					
					// if this point is not on the edge add it to the queue
					if(closeTo(nPoint, targets, distance))
					{
						if(!toSearch.contains(nPoint))
						{
							toSearch.add(nPoint);
							//System.out.println("adding\n\t[" + nPoint.toString() + "]: " + nPoint.getScore() + ", derived from\n\t[" + temp.toString() + "]");
						}
					}
					// else if it's the new best entry point save it as such
					else if(bestEntryPoint == null || bestEntryPoint.getScore() > nPoint.getScore())
					{
						bestEntryPoint = nPoint; // if its score changes it'll only be for the better; no need to make a copy
						System.out.println("NEW BEST\n\t[" + bestEntryPoint.toString() + "]: " + bestEntryPoint.getScore());
						try
						{
							Thread.sleep(200);
						}
						catch (InterruptedException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					//System.out.println("---");
				}
			}
		}
		
		// show the best route
		System.out.println();
		System.out.println("best route: [" + bestEntryPoint.toString() + "] with a coverage of " + bestEntryPoint.getScore());
	}
	
	private static boolean closeTo(Point p, Collection<Point> targets, int distance)
	{
		for(Point t : targets)
		{
			if(Math.sqrt(Math.pow(p.getX() - t.getX(), 2) + Math.pow(p.getY() - t.getY(), 2)) <= distance)
				return true;
		}
		
		return false;
	}
}
