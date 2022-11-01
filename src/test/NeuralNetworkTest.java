package test;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import main.Matrix;
import main.NeuralNetwork;

public class NeuralNetworkTest
{
	
	@Test
	public void neuralNetworkEqualsMethodWorks()
	{
		NeuralNetwork n1, n2;
		
		n1 = new NeuralNetwork.Builder()
				.withInputLayerOfSize(3)
				.withHiddenLayerOfSize(3)
				.withOutputLayerOfSize(3)
				.build();
		
		n2 = new NeuralNetwork.Builder()
				.withInputLayerOfSize(3)
				.withHiddenLayerOfSize(3)
				.withOutputLayerOfSize(3)
				.build();
		
		Assertions.assertNotEquals(n1, n2);
		Assertions.assertEquals(n1, n1.clone());
	}
	
	@Test
	public void neuralNetworkParseMethodWorks()
	{
		NeuralNetwork n1;
		
		n1 = new NeuralNetwork.Builder()
				.withInputLayerOfSize(3)
				.withHiddenLayerOfSize(3)
				.withOutputLayerOfSize(3)
				.build();

		Assertions.assertEquals(n1, NeuralNetwork.parse(n1.toString()));
	}
	
	@Test
	public void numberOfNodesByLayerAreEqualForCloneAndOriginNeuralNetwork()
	{
		NeuralNetwork n1;
		
		n1 = new NeuralNetwork.Builder()
				.withInputLayerOfSize(3)
				.withHiddenLayerOfSize(3)
				.withOutputLayerOfSize(3)
				.build();

		Assertions.assertEquals(n1.getNumberOfNodesByLayer(), n1.clone().getNumberOfNodesByLayer());
	}
	
	@Test
	public void verySimpleNeuralNetworkCanChooChooWithNoExceptionsBeingThrown()
	{
		NeuralNetwork n1;
		
		n1 = new NeuralNetwork.Builder()
				.withInputLayerOfSize(3)
				.withHiddenLayerOfSize(3)
				.withOutputLayerOfSize(3)
				.build();
		
		Matrix input = Matrix.fromOneDimensionalArray(new double[]{1,2,3});
		Matrix output = Matrix.fromOneDimensionalArray(new double[]{1,2,3});

		n1.train(input, output);
	}
	
	@Test
	public void verySimpleNeuralNetworkCanChooChooMultipleTimesWithNoExceptionsBeingThrown()
	{
		NeuralNetwork n1;
		
		n1 = new NeuralNetwork.Builder()
				.withInputLayerOfSize(3)
				.withHiddenLayerOfSize(3)
				.withOutputLayerOfSize(3)
				.build();
		
		Matrix input = Matrix.fromOneDimensionalArray(new double[]{1,2,3});
		Matrix output = Matrix.fromOneDimensionalArray(new double[]{1,2,3});

		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
		n1.train(input, output);
	}
	
	@Test
	public void aLittleBiggerNeuralNetworkCanChooChooWithNoExceptionsBeingThrown()
	{
		NeuralNetwork n1;
		
		n1 = new NeuralNetwork.Builder()
				.withInputLayerOfSize(5)
				.withHiddenLayerOfSize(4)
				.withHiddenLayerOfSize(3)
				.withOutputLayerOfSize(2)
				.build();
		
		Matrix input = Matrix.fromOneDimensionalArray(new double[]{1,2,3,4,5});
		Matrix output = Matrix.fromOneDimensionalArray(new double[]{1,2});

		n1.train(input, output);
	}
	
	@Test
	public void aVeryBeegNeuralNetworkCanChooChooWithNoExceptionsBeingThrown()
	{
		NeuralNetwork n1;
		
		n1 = new NeuralNetwork.Builder()
				.withInputLayerOfSize(10)
				.withHiddenLayerOfSize(9)
				.withHiddenLayerOfSize(9)
				.withHiddenLayerOfSize(9)
				.withHiddenLayerOfSize(9)
				.withOutputLayerOfSize(9)
				.build();
		
		Matrix input = Matrix.fromOneDimensionalArray(new double[]{1,2,3,4,5,6,7,8,9,10});
		Matrix output = Matrix.fromOneDimensionalArray(new double[]{1,2,3,4,5,6,7,8,9});

		n1.train(input, output);
	}
	
}
