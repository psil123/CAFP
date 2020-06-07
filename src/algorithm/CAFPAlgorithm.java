package algorithm;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import helper.MemoryTracker;
import helper.TDBReader;;
/** 
 * This is an implementation of the CAFP algorithm.
 * CAFP is described here:
 * <br/><br/>
 * Paper Name
 * <br/><br/>
 * 
 * This is an optimized version that saves the result to a file
 * or keep it into memory if no output path is provided
 * by the user to the runAlgorithm method().
 *
 * @see TNode
 * @see TreeCell
 * @see ThreadChannel
 * @see TDBReader
 * @author Pritam Sil
 */
public class CAFPAlgorithm
{
	public MemoryTracker m;//To keep track of the maximum memory used
	List<List<Long>> TDB; // To store input Transactional Database
	long minsup;//The minsup values
	Map<Long,Long> FImap;//To store the count of each frequent item
	Map<Long,TreeCell> CLAMap;//To store the CLA . Each Cell is mapped to its cell type in it.
	
	//A comparator to to order two items based on their frequency
	private Comparator<Long> FIorderComparator=new Comparator<Long>(){
		public int compare(Long item1, Long item2){
			long compare = FImap.get(item2) - FImap.get(item1);
			if(compare == 0){ 
				return (int)(item1 - item2);
			}
			return (int)compare;
		}
	};
	
	//A comparator to compare between two sets
	private Comparator<Set<Long>> SetLongComparator=new Comparator<Set<Long>>()
	{

		@Override
		public int compare(Set<Long> o1, Set<Long> o2)
		{
			if(o1.size()>o2.size())
				return 1;
			else if(o1.size()<o2.size())
				return -1;
			if(Sets.symmetricDifference(o1,o2).isEmpty())
				return 0;
			return 1;
		}
		
	};
	
	//A comparator to compare between two lists
	private Comparator<List<Long>> ListLongComparator=new Comparator<List<Long>>()
	{

		@Override
		public int compare(List<Long> o1, List<Long> o2)
		{
			if(o1.size()>o2.size())
				return 1;
			else if(o1.size()<o2.size())
				return -1;
			if(new HashSet<>(o1).equals(new HashSet<>(o2)))
					return 0;
			return 1;
		}
		
	};
	/**
	 * Constructor
	 */
	public CAFPAlgorithm()
	{
		this.CLAMap=new TreeMap<Long,TreeCell>();
		this.m=new MemoryTracker();
	}
	/**
	 * Method to create the FImap which maps each frequent item to its frquency.
	 */
	void find_freq_item()
	{
		Map<Long,Long> FImap=new HashMap<Long,Long>();
		this.FImap=new HashMap<Long,Long>();
		for(List<Long> s:TDB)
			for(Long i:s)
			{
				if(FImap.containsKey(i))
					FImap.put(i,FImap.get(i)+1);
				else
					FImap.put(i,(long)1);
			}
		this.FImap=FImap.entrySet().stream().filter(x->x.getValue()>=this.minsup).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));	
	}
	
	/**
	 * Method to convert the Transactional Database (TDB) into Dense Dataset (DDS)
	 * @return the Dense Dataset
	 */
	Map<List<Long>, Long> get_CDS()
	{
		Map<List<Long>,Long> ob=new TreeMap<List<Long>,Long>(this.ListLongComparator);
		for(List<Long> i:TDB)
		{
			
			List<Long> temp=i.stream().filter(x->FImap.containsKey(x)).sorted(this.FIorderComparator).collect(Collectors.toList());
			if(temp.size()==0)
				continue;
//			Collections.sort(temp,FIorderComparator);
			if(!ob.containsKey(temp))
			{
				CLAMap.put(temp.get(0),new TreeCell(i.get(0)));
				ob.put(temp,(long)1);
			}
			else
				ob.put(temp,ob.get(temp)+1);
		}
		return ob;
	}
	
	/**
	 * Method to generate the Conditional Pattern Base (CPB).
	 * @return A map in which each frequent item(FI) is mapped to its CPB
	 */
	Map<Long, Map<Long, Long>> get_CPB()
	{
		Map<Long,Map<Long, Long>> CPB=new TreeMap<Long,Map<Long, Long>>();
//		System.out.println(FImap);
		for(Long ch:FImap.keySet())
		{
			Map<Long, Long> templ=new TreeMap<Long, Long>();
			for(TreeCell i:CLAMap.values())
			{
				if(i.root==null)
					continue;
				List<Long> temp1=new ArrayList<Long>();
				temp1.add(i.root.type);
				i.traverseCPB(i.root,temp1,templ, ch);
			}
			templ = templ.entrySet().stream()
					.filter(x -> x.getValue()>=this.minsup)
					.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
				CPB.put(ch,templ);
		}
		return CPB;
	}
	
	/**
	 * Method to return the powerSet of a given set
	 * @param originalSet a set
	 * @return the powerset of the given set
	 * @throws IOException exception if error reading or writing files
	 */
	public static <T> Set<Set<T>> powerSets(Set<T> originalSet)
	{
	    Set<Set<T>> sets = new HashSet<Set<T>>();
	    if (originalSet.isEmpty()) {
	        sets.add(new HashSet<T>());
	        return sets;
	    }
	    List<T> list = new ArrayList<T>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
	    for (Set<T> set : powerSets(rest)) {
	        Set<T> newSet = new HashSet<T>();
	        newSet.add(head);
	        newSet.addAll(set);
	        sets.add(newSet);
	        sets.add(set);
	    }       
	    return sets;
	}
	/**
	 * Method to generate the Frequent Patterns from the CPB(Conditional Pattern Base)
	 * @param CPB the Conditional Pattern Base
	 * @return a set of all the frequent patterns
	 */
	Set<Set<Long>> get_FP(Map<Long,Map<Long, Long>> CPB)
	{
		Set<Set<Long>> out=new TreeSet<>(this.SetLongComparator);
		for(Entry<Long, Map<Long, Long>> j:CPB.entrySet())
		{
			Set<Set<Long>> pow=powerSets(j.getValue().keySet());
			for(Set<Long> kk:pow)
			{
				kk.add(j.getKey());
				out.add(kk);
			}
		}
		return out;
	}
	
	/**
	 * Method to run the CAFP algorithm.
	 * @param input the path to an input file containing a transaction database.
	 * @param delim the delimiter between each item in the input file.For example it will be "," for csv files
	 * @param output the output file path for saving the result (if null, the result will also be printed).
	 * @param minsupp the minimum support threshold.
	 * @return the result which is a set containing a set of each Frequent Pattern
	 * @throws Exception IOException if there is an issue while reading the files.InterruptedExcetion if there is an issue with the threads
	 */
	public Set<Set<Long>> runAlgorithm(String input,String delim,String output,double minsupp) throws Exception
	{
		this.TDB=new TDBReader().readFromFile(input,delim);
		this.minsup=(long)Math.ceil(minsupp*TDB.size());
		m.reset();
		long start = System.currentTimeMillis();
		find_freq_item();
		m.checkUsage();
		Map<List<Long>, Long> CDS=get_CDS();
		m.checkUsage();
		
		ThreadChannel channel=new ThreadChannel(FImap.keySet());
		for (Map.Entry<List<Long>,Long> entry : CDS.entrySet())
		{
			long tempi=entry.getKey().get(0);
			channel.add(tempi,new Thread(()->CLAMap.get(tempi).add(entry.getKey(),entry.getValue())));
		}
		channel.shutdown();
		Map<Long, Map<Long, Long>> CPB=this.get_CPB();
		Set<Set<Long>> FP=this.get_FP(CPB);

		long end = System.currentTimeMillis();
		if(output==null)
		{
			System.out.println("Following are the Frequent Patterns ---");
			FP.forEach(i->System.out.println(i));
		}
		else
		{
			FileWriter outFile = new FileWriter(output);
			FP.forEach(i->
				{
					try {
						outFile.write(i.toString().replace("[","").replace("]","").replace(","," ")+"\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			outFile.close();
		}
		System.out.println("==================Summary==================");
		System.out.println("Number of FPs Generated : " + FP.size());
		System.out.println("Number of transactions : " + TDB.size());
		System.out.println("Minsup value : "+minsup);
		System.out.println("Max memory : "+ m.getMaxMemory() +" MB");
		System.out.println("Time taken : "+ (end-start) +" ms");
		System.out.println("===========================================");
		return FP;
	}
}
