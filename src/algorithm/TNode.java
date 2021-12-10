package algorithm;
import java.util.LinkedList;
import java.util.List;
/** 
 * 
 * This is used for representing a Node in the FPTree
 * @author Pritam Sil
 */
public class TNode
{
	public long type;//the type of the node i.e. the item
	public long count;// the count of that item
	public List<TNode> child;//a list to store all the children of this node
	/**
	 * Constructor
	 * @param ch the type of the Node
	 * @param count the count of the Node
	 */
	public TNode(long ch, long count)
	{
		this.type=ch;
		this.count=count;
		child=new LinkedList<TNode>();
	}
	/**
	 * Method to convert the TNode Object to a String
	 * @return the String format of the TNode Object
	 */
	public String toString()
	{
		return "<"+type+" "+count+">";
	}
}
