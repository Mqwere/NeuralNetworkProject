package main;

import java.util.ArrayList;
import java.util.Objects;



public class NeuralNetwork
{
	static final double DEFAULT_LEARNING_RATE = 0.01D;
	
	private double learningRate;

	private ArrayList<Matrix>	weightsForEdges;
	private ArrayList<Matrix>	biasesForEdges;
		
	// this is a little redundant, since you could get its data from the matrices above
	// but I decided I don't want to generate the array every time I'd like to have it, so there
	private ArrayList<Integer>	numberOfNodesByLayer; 

	NeuralNetwork( Builder builder )
	{
		this.learningRate = builder.learningRate;

		numberOfNodesByLayer = new ArrayList<>();

		numberOfNodesByLayer.add( builder.inputLayerSize );
		numberOfNodesByLayer.addAll( builder.layersOfHiddenNodes );
		numberOfNodesByLayer.add( builder.outputLayerSize );

		this.generateMatricesForExistingNodes( );
	}
	
	public int getInputLayerValuesLength()
	{
		return weightsForEdges.get(0).numberOfRows;
	}
	
	//	this is a FORWARD PROPAGATION function	//
	public ArrayList<Matrix> process(Matrix inputLayerValues)
	{
		ArrayList<Matrix> activatedNeurons = new ArrayList<>();
		Matrix processedMatrix = inputLayerValues.clone();
		
		for(int i = 0; i < weightsForEdges.size(); i++)
		{
			processedMatrix = Matrix.getMultiplication(weightsForEdges.get(i), processedMatrix);
			processedMatrix.add(biasesForEdges.get(i));
			processedMatrix.sigmoid();
			activatedNeurons.add(processedMatrix);
		}
		
		return activatedNeurons;
	}
	//----------------------------------------//
	
	//	this is a CHOO CHOO function	//
	public void train(Matrix inputValues, Matrix expectedOutputValues)
	{
		ArrayList<Matrix> 
			activatedNeurons = process(inputValues);
		
		Matrix //output
			valuesAtNeuronLevel = activatedNeurons.get(activatedNeurons.size()-1);
		
		Matrix 									// target
			error = Matrix.getSubtraction(expectedOutputValues, valuesAtNeuronLevel);

		for(int i = activatedNeurons.size()-1; i > 0; i--)
		{
			Matrix 
				gradient = Matrix.getDerivativeSigmoidOf(valuesAtNeuronLevel);
				gradient.multiplyValueWise(error);
				gradient.multiply(learningRate);
			
			Matrix 
				earlierLevelValues = activatedNeurons.get(i-1);
			//hidden = 
			Matrix 
				earlierLevelValuesTransposed = Matrix.transpose(earlierLevelValues);
			
		   Matrix 
		   	delta =  Matrix.getMultiplication(gradient, earlierLevelValuesTransposed);
		   
		   weightsForEdges.get(i).add(delta);
		   biasesForEdges	.get(i).add(gradient);
		   
		   error = Matrix.getMultiplication(Matrix.transpose(weightsForEdges.get(i)), error);
		   valuesAtNeuronLevel = earlierLevelValues;
		}
		
	}
	//----------------------------------------//

	public NeuralNetwork clone()
	{
		return new NeuralNetwork(this);
	}


	public ArrayList<Integer> getNumberOfNodesByLayer()
	{
		return numberOfNodesByLayer;
	}
	
	public static NeuralNetwork parse( String input ) throws NeuralNetworkException
	{
		try {
			String[] lines = input.split("\n+");
			
			ArrayList<Matrix>	weightsForEdges = new ArrayList<>();
			ArrayList<Matrix>	biasesForEdges = new ArrayList<>();
			
			double learningRate = Double.parseDouble(lines[0]);
			for(int i = 1; i < lines.length; i ++)
			{
				if( i%2 == 1 )
				{
					weightsForEdges.add(Matrix.parse(lines[i]));
				}
				else
				{
					biasesForEdges.add(Matrix.parse(lines[i]));
				}
			}
			
			if(weightsForEdges.size() != biasesForEdges.size()) 
				throw new NeuralNetworkException("Unequal number of weight and bias matrices (%d > %d)", weightsForEdges.size(), biasesForEdges.size());
			
			return new NeuralNetwork(weightsForEdges, biasesForEdges, learningRate);
		}
		catch(Exception e)
		{
			throw new NeuralNetworkException(e);
		}
	}

	@Override
	public String toString()
	{
		String output = learningRate+"\n";

		for (int i = 0; i < weightsForEdges.size(); i++)
		{
			output	+= weightsForEdges.get( i ).toString() + "\n";
			output	+= biasesForEdges.get( i ).toString() + "\n";
		}

		return output.substring( 0, output.length() - 1 );
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash( biasesForEdges, learningRate, weightsForEdges );
	}

	@Override
	public boolean equals( Object obj )
	{
		if (this == obj)
			return true;
		
		if (!(obj instanceof NeuralNetwork))
			return false;
		
		NeuralNetwork other = (NeuralNetwork) obj;
		
//		System.out.printf("%s,%s,%s\n",
//				Objects.equals( biasesForEdges, other.biasesForEdges ),
//				Double.doubleToLongBits( learningRate ) == Double.doubleToLongBits( other.learningRate ),
//				Objects.equals( weightsForEdges, other.weightsForEdges ));
				
		return 
				Objects.equals( biasesForEdges, other.biasesForEdges ) && 
				Double.doubleToLongBits( learningRate ) == Double.doubleToLongBits( other.learningRate ) && 
				Objects.equals( weightsForEdges, other.weightsForEdges );
	}
	
	private NeuralNetwork( ArrayList<Matrix> weightsForEdges, ArrayList<Matrix> biasesForEdges, double learningRate )
	{
		this.weightsForEdges	= weightsForEdges;
		this.biasesForEdges	= biasesForEdges;
		this.learningRate = learningRate;
		
		generateNodesNumbersForExistingMatrices( );
	}
	
	private void generateMatricesForExistingNodes( )
	{
		weightsForEdges	= new ArrayList<>();
		biasesForEdges		= new ArrayList<>();

		for (int i = 0; i < numberOfNodesByLayer.size() - 1; i++)
		{
			weightsForEdges.add(
					new Matrix(
							numberOfNodesByLayer.get( i + 1 ), 
							numberOfNodesByLayer.get( i )
					)
			);

			biasesForEdges.add(
					new Matrix(
							numberOfNodesByLayer.get( i + 1 ), 
							1
					)
			);
		}
	}
	
	private void generateNodesNumbersForExistingMatrices( )
	{
		numberOfNodesByLayer	= new ArrayList<>();
		
		int i = 0;
		for(; i < weightsForEdges.size(); i++)
			numberOfNodesByLayer.add(weightsForEdges.get(i).numberOfRows);

		numberOfNodesByLayer.add(weightsForEdges.get(i-1).numberOfColumns);
	}
	
	private NeuralNetwork(NeuralNetwork other)
	{
		this.learningRate = other.learningRate;
		
		weightsForEdges	= new ArrayList<>();
		biasesForEdges		= new ArrayList<>();
		
		for(Matrix m: other.weightsForEdges)
			weightsForEdges.add(m.clone());
		
		for(Matrix m: other.biasesForEdges)
			biasesForEdges.add(m.clone());
		
		generateNodesNumbersForExistingMatrices( );
	}

	public static class Builder
	{
		private boolean	hasInputLayer	= false;
		private boolean	hasOutputLayer	= false;

		double learningRate;

		int						inputLayerSize			= 0;
		ArrayList<Integer>	layersOfHiddenNodes	= new ArrayList<>();
		int						outputLayerSize		= 0;

		public Builder()
		{
			this( NeuralNetwork.DEFAULT_LEARNING_RATE );
		}

		public Builder( double learningRate )
		{
			this.learningRate = learningRate;
		}

		public Builder withInputLayerOfSize( int size )
		{
			if(size > 0)
			{
				hasInputLayer	= true;
				inputLayerSize	= size;
			}
			return this;
		}

		public Builder withHiddenLayerOfSize( int size )
		{
			if(size > 0)
			{
				layersOfHiddenNodes.add(size);
			}
			return this;
		}

		public Builder withOutputLayerOfSize( int size )
		{
			if(size > 0)
			{
				hasOutputLayer	= true;
				outputLayerSize	= size;
			}
			return this;
		}

		public NeuralNetwork build()
		{
			if (!canBeBuilt())
				throw new NeuralNetworkException(
						"Neural network couldn't have been built due to the lack of"
								+ (!hasInputLayer ? " the input layer," : "")
								+ ((layersOfHiddenNodes.size() <= 0) ? " at least one hidden layer," : "")
								+ (!hasOutputLayer ? " the output layer," : "")
				);

			return new NeuralNetwork( this );
		}

		private boolean canBeBuilt()
		{
			return hasInputLayer && layersOfHiddenNodes.size() > 0 && hasOutputLayer;
		}
	}

	public static class NeuralNetworkException extends ProjectException
	{
		private static final long serialVersionUID = 1L;

		public NeuralNetworkException(Object message, Object... args)
		{
			super(message, args);
		}
		
	}
}
