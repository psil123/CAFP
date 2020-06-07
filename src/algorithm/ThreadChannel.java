package algorithm;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadChannel
{
	/** 
	 * This is responsible for maintaning a separate ThreadPools for each Cell in the CLA. It provides a certain degree of parallelism to our algorithm
	 * @author Pritam Sil
	 */
	Map<Long,ExecutorService> mapping=new HashMap<Long,ExecutorService>();
	/**
	 * Constructor
	 * @param mapping a set containing all the cell types of CLAMap.
	 * @see CAFPAlgorithm 
	 */
	public ThreadChannel(Set<Long> mapping)
	{
		for(Long i:mapping)
			this.mapping.put(i,Executors.newSingleThreadExecutor());
//			this.mapping.put(i,Executors.newWorkStealingPool(4));
//			this.mapping.put(i,Executors.newFixedThreadPool(1));
	}
	/**
	 * Method to add a Thread to its corresponding ThreadPool
	 * @param type the type of the ThreadPool to which the thread is to be added
	 * @param ob the Thread object
	 */
	public void add(long type,Thread ob)
	{
		mapping.get(type).execute(ob);
	}
	/**
	 * Method to shutdown all the ThreadPools
	 */
	public void shutdown()
	{
		for(ExecutorService i:mapping.values())
			i.shutdown();
		for(ExecutorService i:mapping.values())
			while (!i.isTerminated());
	}
}
