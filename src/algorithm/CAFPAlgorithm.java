package algorithm;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import helper.MemoryTracker;
import helper.PowerSet;
import helper.TDBReader;;
/** 
 * This is an implementation of the CAFP algorithm.
 * CAFP is described here:
 * <br/><br/>
 * Paper Name
 * <br/><br/>
 * 
 * It saves the result to a file and returns it to the user when runAlgorithm() is executed.
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
	Map<Set<Long>, Long> outputTemp;//To store the output
	
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
//			System.out.println("Comparing : "+o1+" "+o2);
			if(o1.size()>o2.size())
				return 1;
			else if(o1.size()<o2.size())
				return -1;
			if(new HashSet<>(o1).equals(new HashSet<>(o2)))
					return 0;
			return 1;
		}
		
	};
	
	//A comparator to compare between two lists
	private Comparator<Entry<Set<Long>,Set<Long>>> 
		coverComp=new Comparator<Entry<Set<Long>,Set<Long>>>()
	{
		public int compare(Entry<Set<Long>, Set<Long>> o1, Entry<Set<Long>, Set<Long>> o2)
		{
			if(o1.getKey().containsAll(o2.getKey()) && 
					o1.getValue().containsAll(o2.getValue()))
			{
				return 0;
			}

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
		//create the FImap which maps each frequent item to its frquency.
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
		//the lambda expression below filters out the elements whose frequency is less than the minsup value
		this.FImap=FImap.entrySet().stream().filter(x->x.getValue()>=this.minsup).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));	
	}
	
	/**
	 * Method to convert the Transactional Database (TDB) into Dense Dataset (DDS)
	 * @return the Dense Dataset
	 */
	Map<List<Long>, Long> get_DDS()
	{
		//create the DDS which maps each row to its frequency. The rows containing only the frequent items are added to the DDS. 
		Map<List<Long>,Long> DDS=new TreeMap<List<Long>,Long>(this.ListLongComparator);
		for(List<Long> i:TDB)
		{
			//the lambda expression below filters out the elements whose frequency is less than the minsup value in a row
			List<Long> temp=i.stream().filter(x->FImap.containsKey(x)).sorted(this.FIorderComparator).collect(Collectors.toList());
			if(temp.size()==0)
				continue;
			
			if(!DDS.containsKey(temp))
			{
				CLAMap.put(temp.get(0),new TreeCell(i.get(0)));
				DDS.put(temp,(long)1);
			}
			else
				DDS.put(temp,DDS.get(temp)+1);
		}
		return DDS;
	}
	
	/**
	 * Method to generate the Conditional Pattern Base (CPB).
	 * @return A map in which each frequent item(FI) is mapped to its CPB
	 */
//	Map<Long, Map<Long, Long>> get_CPB()
	Map<Long,Map<List<Long>, Long>> get_CPB()
	{
		Map<Long,Map<List<Long>, Long>> CPB=new HashMap<Long,Map<List<Long>, Long>>();
		// For each FI and for each FPTree , traverse along the tree and generate the CPB
		for(Long ch:FImap.keySet())
		{
//			Map<List<Long>, Long> templ=new TreeMap<List<Long>, Long>(this.ListLongComparator);
			Map<List<Long>, Long> templ=new HashMap<List<Long>, Long>();
			for(TreeCell i:CLAMap.values())
			{
				if(i.root==null)
					continue;
				List<Long> temp1=new ArrayList<Long>();
				temp1.add(i.root.type);
				i.traverseCPB(i.root,temp1,templ, ch);
			}
//			templ = templ.entrySet().stream()
//					.filter(x -> x.getValue()>=this.minsup)
//					.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
				if(templ.size()>0)
					CPB.put(ch,templ);
		}
		System.out.println(CPB);
		return CPB;
	}
	
	/**
	 * Method to return the powerSet of a given set
	 * @param <T>
	 * @param originalSet a set
	 * @return the powerset of the given set
	 * @throws IOException exception if error reading or writing files
	 */
	public  <T> Set<Set<T>> powerSet_rec(Set<T> originalSet) {
	    Set<Set<T>> sets = new HashSet<Set<T>>();
	    if (originalSet.isEmpty()) {
	        sets.add(new HashSet<T>());
	        return sets;
	    }
	    List<T> list = new ArrayList<T>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
	    for (Set<T> set : powerSet(rest)) {
	        Set<T> newSet = new HashSet<T>();
	        newSet.add(head);
	        newSet.addAll(set);
	        sets.add(newSet);
	        sets.add(set);
	    }       
	    return sets;
	}
	
	public  <T> Set<Set<T>> powerSet(Set<T> originalSet) {
	    Set<Set<T>> sets = new HashSet<Set<T>>();
	    if (originalSet.isEmpty()) {
	        sets.add(new HashSet<T>());
	        return sets;
	    }
	    List<T> temp=new ArrayList<T>(originalSet);
	    long max=(long)Math.pow(2,originalSet.size());
	    
	    for(long i=1;i<max;i++)
	    {
	    	Set<T> item=new HashSet<T>();
	    	String s=Long.toBinaryString(i);
	    	String k="";
	    	if(s.length()<originalSet.size())
	    		for(int j=s.length();j<originalSet.size();j++)
	    			k+="0";
	    	s=k+s;
//	    	System.out.println(s);
	    	for(int j=0;j<s.length();j++)
	    		if(s.charAt(j)=='1')
	    			item.add(temp.get(j));
	    	sets.add(item);
	    }
//	    System.out.println("Orig : "+originalSet+"\nSUB : "+sets.size());
	    return sets;
	} 
	
	/**
	 * Method to generate the Frequent Patterns from the CPB(Conditional Pattern Base)
	 * @param CPB the Conditional Pattern Base
	 * @return a set of all the frequent patterns
	 */
	Map<Set<Long>, Long> get_filtered_CPB(Map<Long,Map<List<Long>, Long>> CPB)
	{
//		Map<Set<Long>,Long> out=new TreeMap<Set<Long>,Long>(this.SetLongComparator);
		Map<Set<Long>,Long> out=new HashMap<Set<Long>,Long>();
		// Here the CPB of each item has been taken and the item has been apended to each of the sets present in its CPB. 
		// The resulting sets form our frequent itemsets and are apended to the output.  
		for(Entry<Long, Map<List<Long>, Long>> j:CPB.entrySet())
		{
//			System.out.println("Processing : "+j);
			PowerSet<List<Long>> pow=new PowerSet<List<Long>>(j.getValue().keySet(),1,j.getValue().size());
//			Set<Set<List<Long>>> pow=powerSet(j.getValue().keySet());
//			System.out.println(pow);
//			for(Set<List<Long>> kk:pow)
			while(pow.hasNext())
			{
				List<List<Long>> kk=(LinkedList<List<Long>>)pow.next();
//				System.out.println("Power Processing : "+kk);
				if(kk.size()==0)
					continue;
				List<List<Long>> templ=new ArrayList<List<Long>>(kk);
//				System.out.println("Power Processing templ : "+templ);
				Set<Long> temp=new HashSet<>(templ.get(0));
				if(templ.size()==1)
				{
//					continue;
//					System.out.println("Cur : "+j.getValue()+" "+templ.get(0)+" "+j.getValue().get(templ.get(0)));
					long val=j.getValue().get(templ.get(0));
					if(val<this.minsup)
						continue;
					temp.add(j.getKey());
					out.put(temp,val);
				}
				else
				{
//					System.out.println("Cur : "+j.getValue()+" "+templ.get(0)+" "+j.getValue().get(templ.get(0)));
					temp.retainAll(new HashSet<>(templ.get(1)));
					long val=j.getValue().get(templ.get(0))+j.getValue().get(templ.get(1));
					for(int i=2;i<templ.size();i++)
					{
						temp.retainAll(new HashSet<>(templ.get(i)));
						val+=j.getValue().get(templ.get(i));
					}
					if(temp.size()==0 || val<this.minsup)
						continue;
					temp.add(j.getKey());
					out.put(temp,val);
				}
			}
			
		}
//		return null;
		System.out.println("Out Now : "+out);
		return out;
	}
	
	Map<Set<Long>, Long> get_FP(Map<Set<Long>, Long>  CPB)
	{
		
		Map<Set<Long>, Long>  out=new HashMap<Set<Long>, Long>(CPB);
		
		for(Entry<Set<Long>, Long> i:CPB.entrySet())
		{
//			Set<Set<Long>> temp=this.powerSet(i.getKey());
			PowerSet<Long> temp=new PowerSet<Long>(i.getKey(),1,i.getKey().size());
//			for(Set<Long> j:temp)
			while(temp.hasNext())
			{
				Set<Long> j=new HashSet<Long>((LinkedList<Long>)temp.next());
				if(j.size()<2)
					continue;
				if(out.containsKey(j))
					out.put(j,Math.max(i.getValue(),out.get(j)));
				else
					out.put(j,i.getValue());
			}
		}
		System.out.println(out);
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
	public Map<Set<Long>, Long> runAlgorithm(String input,String delim,String output,double minsupp) throws Exception
	{
		this.TDB=new TDBReader().readFromFile(input,delim);
		this.minsup=(long)Math.ceil(minsupp*TDB.size());
//		System.out.println(minsup);
		m.reset();
		long start = System.currentTimeMillis();
		find_freq_item(); // generate the FImap containing the frequency of each frequent item
		m.checkUsage();
		Map<List<Long>, Long> DDS=get_DDS(); // generate the DDS
		m.checkUsage();
		
		ThreadChannel channel=new ThreadChannel(FImap.keySet());// To maintain a separate thread pool for each FP-Tree in our FP-Forest
		for (Map.Entry<List<Long>,Long> entry : DDS.entrySet())
		{
			long tempi=entry.getKey().get(0);
			// All the items are added to an FP-Tree in separate threads which are added to their corresponding threadpools present in ThreadChannel.
			channel.add(tempi,new Thread(()->CLAMap.get(tempi).add(entry.getKey(),entry.getValue())));
		}
		channel.shutdown(); // close all ThreadPools in channel and ensure that all items have been added
		
		Map<Long, Map<List<Long>, Long>> CPB=this.get_CPB();// generate the CPB
		Map<Set<Long>, Long> CPB1=this.get_filtered_CPB(CPB);
		Map<Set<Long>, Long> FP=this.get_FP(CPB1);
		long end = System.currentTimeMillis();
		if(output==null)
		{
			System.out.println("Following are the Frequent Patterns ---");
			FP.forEach((k,v)->System.out.println(k.toString().replace("[","").replace("]","").replace(", ",",")+",#SUP="+v));
		}
		else
		{
			FileWriter outFile = new FileWriter(output);
			FP.forEach((k,v)->{
				try {
					outFile.write(k.toString().replace("[","").replace("]","").replace(", ",",")+",#SUP="+v+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
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
		this.outputTemp=FP;
		return FP;
	}
}
