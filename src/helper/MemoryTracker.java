package helper;
/** 
 * 
 * This is used to keep track of the maximum memory used by the algorithm during its execution
 * @author Pritam Sil
 */
public class MemoryTracker
{
	private double max = 0;// To store keep track of the maximum memory usage
	
	/**
	 * To get the maximum memory usage till invocation
	 * @return the memory used in megabytes
	 */
	public double getMaxMemory()
	{
		return max;
	}

	/**
	 * Reset the maximum amount of memory recorded.
	 */
	public void reset()
	{
		max = 0;
	}
	
	/**
	 * Record the Maximum Memory Usage
	 */
	public void checkUsage()
	{
		double temp=(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/1024d/1024d;
		if (temp>max)
			max=temp;
	}
}
