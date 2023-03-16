package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class NeuralNetworkProject
{
	private static final String DEFAULT_DIRECTORY = "./data";
	private static final String DEFAULT_EXTENSION = ".mtrx";
	
	private static File saveFile;

	public static void main( String[] args )
	{
		NeuralNetwork A, B;
		saveNetwork(
			A = new NeuralNetwork.Builder().withInputLayerOfSize( 3 ).withHiddenLayerOfSize( 3 ).withOutputLayerOfSize( 3 ).build( ),
			DEFAULT_DIRECTORY,
			"testKwi"
		);
		B = loadNetwork(new File(DEFAULT_DIRECTORY + "/testKwi" + DEFAULT_EXTENSION));
		
		System.out.println(A.equals( B ));
	}
	
	public static NeuralNetwork loadNetwork(File file)
	{
		if(file == null) return null;
		
		BufferedReader fileReader;

		try
		{
			fileReader = new BufferedReader(new FileReader(file));
			StringBuilder fileContent = new StringBuilder();
			String line;
			while( (line = fileReader.readLine( )) != null)
			{
				fileContent.append( line ).append( "\n" );
			}
			fileReader.close( );
			return NeuralNetwork.parse( fileContent.toString( ) );
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void saveNetwork(NeuralNetwork network)
	{
		saveNetwork(network, DEFAULT_DIRECTORY);
	}
	
	public static void saveNetwork(NeuralNetwork network, String directoryString)
	{
		saveNetwork(network, directoryString, createFileName());
	}
	
	public static void saveNetwork(NeuralNetwork network, String directoryString, String fileName)
	{
		if(saveFile == null && !createSaveFile(directoryString, fileName)) 
		{
			return;
		}
		
		PrintWriter fileWriter;
		
		try
		{
			fileWriter = new PrintWriter(saveFile.getPath());
			fileWriter.print(network.toString( ));
			fileWriter.close();
			System.out.printf("Network saved at \"%s\"\n", saveFile.getPath());
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace( );
		}
	}
	
	private static String createFileName()
	{
		ZonedDateTime now = Instant.now().atZone(ZoneOffset.ofHours(2));
		String nowString = 
		String.format(
			"%d%02d%02d_%02d%02d%02d", 
			now.getYear(), 
			now.getMonthValue(), 
			now.getDayOfMonth(),
			
			now.getHour(),
			now.getMinute(),
			now.getSecond()
		);
		
		return nowString;
	}
	
	private static boolean createSaveFile(String directoryString, String fileName)
	{
		try
		{
			File directory = new File(directoryString);
			if(!directory.exists()) directory.mkdirs();
			
			saveFile = new File(directoryString + "/" + fileName + DEFAULT_EXTENSION);
			return saveFile.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace( );
			return false;
		}
	}

}
