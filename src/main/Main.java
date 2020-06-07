package main;

import algorithm.CAFPAlgorithm;

public class Main
{
	public static void main(String[] args) throws Exception
	{
		CAFPAlgorithm ob=new CAFPAlgorithm();
		ob.runAlgorithm(args[0],args[1],args[2],Double.parseDouble(args[3]));
	}

}
