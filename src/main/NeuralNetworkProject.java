package main;

import java.time.Instant;

public class NeuralNetworkProject
{
	private static final String DEFAULT_DIRECTORY = "./data/";

	public static void main( String[] args )
	{
		//File directory = new File("./data/");
		System.out.print(Instant.now());
	}
	
	public static void saveNetwork(NeuralNetwork network)
	{
		
	}
	
	public static void saveNetwork(String directory, String fileName, NeuralNetwork network)
	{
		
	}
	
	private static String generateFileName()
	{
		Instant now = Instant.now();
		
		now.toString();
		
		return null;
	}

}
