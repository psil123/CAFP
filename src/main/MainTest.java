package main;

import algorithm.CAFPAlgorithm;

public class MainTest
{
	public static void main(String[] args) throws Exception
	{
		CAFPAlgorithm ob=new CAFPAlgorithm();
//			ob.runAlgorithm("src/sample.txt"," ","src/output.txt",0.5);
//			ob.runAlgorithm("D:\\work\\CLA_Mining_CAFP\\data\\biodiversity.txt"," ","D:\\work\\CLA_Mining_CAFP/output_12.txt",0.5);
//			ob.runAlgorithm("D:\\work\\CLA_Mining_CAFP\\data\\sample.txt"," ","D:\\work\\CLA_Mining_CAFP/output_12.txt",0.5);
			ob.runAlgorithm("D:\\work\\CLA_Mining_CAFP\\data\\sample\\test_files\\chess.txt"," ","D:\\work\\CLA_Mining_CAFP/output_12.txt",0.94);
	}
}
