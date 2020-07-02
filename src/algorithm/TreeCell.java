package algorithm;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** 
 * 
 * This is used for representing a Cell in the Cellular Learning Automata
 * @author Pritam Sil
 */
class TreeCell
{
	public long type;//the cell type
	public TNode root;//the root node
	
	/**
	 * Constructor
	 * @param ch The type of the cell
	 */
	public TreeCell(long ch)
	{
		type=ch;
		root=null;
	}
	
	/**
	 * Method to add a Transaction to the FPTree
	 * @param list The transaction
	 * @param count the count of the transaction
	 */
	public void add(List<Long> list,long count)
	{
		if(root==null)
		{
			Iterator<Long> i=list.iterator();
			root=new TNode(i.next(),count);
			TNode temp=root;
			while(i.hasNext())
			{
				long ii=i.next();
				temp.child.add(new TNode(ii,count));
				temp=temp.child.get(0);
			}
			
		}
		else
		{
			TNode temp=root;
			boolean flag;
			for(int i=0;i<list.size()-1;i++)
			{
//				System.out.println(temp.ch);
				temp.count+=count;
				flag=true;
				for(TNode k:temp.child)
					if(k.type==list.get(i+1))
					{
						temp=k;
						flag=false;
						break;
					}
				if(flag)
				{
					TNode temp1=new TNode(list.get(i+1),0);
					temp.child.add(temp1);
					temp=temp1;
				}
			}
//			System.out.println(s);
//			System.out.println("--------------");
			temp.count+=count;
		}
	}
	
	/**
	 * Method to generate the Conditional Pattern Base (CPB) of an item from the FPTree
	 * @param temp the current TNode of the tree
	 * @param s The path traversed till now
	 * @param map A map to store the CPB
	 * @param search The item whose CPB is to be generated
	 */
	public void traverseCPB(TNode temp,List<Long> s,Map<Long,Long> map,long search)
	{
		if(temp.type==search)
		{
			if(s.size()!=1)
			{
				for(int i=0;i<s.size()-1;i++)
				{
					long ch=s.get(i);
					if(map.containsKey(ch))
						map.put(ch,map.get(ch)+temp.count);
					else
						map.put(ch,temp.count);
				}
			}
		}
		for(TNode i:temp.child)
		{
			List<Long> temp1=new ArrayList<Long>();
			temp1.addAll(s);
			temp1.add(i.type);
			traverseCPB(i,temp1,map,search);
		}
	}

}
