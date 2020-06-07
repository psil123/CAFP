package main;

import algorithm.CAFPAlgorithm;

public class MainTest
{
	public static void main(String[] args) throws Exception
	{
		CAFPAlgorithm ob=new CAFPAlgorithm();
			ob.runAlgorithm("src/sample.txt"," ","src/output.txt",0.5);
	}
}
