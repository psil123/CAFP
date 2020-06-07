package helper;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/** 
 * 
 * This is responsible for reading the TDB file from the given input file
 * @author Pritam Sil
 */
public class TDBReader
{
	/**
	 * Method to read the Transactional Database from a file
	 * @param input the path to an input file containing a transaction database.
	 * @param delim the delimiter between each item in the input file.For example it will be "," for csv files
	 * @return the Transaction Database 
	 * @throws Exception 
	 */
	public List<List<Long>> readFromFile(String path,String delim) throws Exception
	{
		BufferedReader read= new BufferedReader(new FileReader(path));
		String line=null;
		List<List<Long>> list=new LinkedList<List<Long>>();
		line=read.readLine();
		String k[]=line.split(delim);
		List<Long> temp=new ArrayList<Long>();
		for(String i:k)
			temp.add(Long.parseLong(i.trim()));
		list.add(temp);

//		System.out.println(list.get(list.size()-1));
		while((line=read.readLine())!=null)
		{
			k=line.split(delim);
			temp=new ArrayList<Long>();
			for(String i:k)
				temp.add(Long.parseLong(i.trim()));
			list.add(temp);
		}
		read.close();
		return list;
	}
}
