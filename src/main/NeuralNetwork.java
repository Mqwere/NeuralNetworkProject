package main;

import java.util.ArrayList;

import main.exceptions.NeuralNetworkException;

/*/
public NeuralNetwork(int i,int h,int o) {
        weights_ih = new Matrix(h,i);
        weights_ho = new Matrix(o,h);
        
        bias_h= new Matrix(h,1);
        bias_o= new Matrix(o,1);
    }
/**/


public class NeuralNetwork
{   
    private double learningRate;
    
    private ArrayList<Matrix> weightsForEdges;
    private ArrayList<Matrix> biasesForEdges;
    
    private ArrayList<Integer> numberOfNodesByLayer = new ArrayList<>();
    
    NeuralNetwork(NeuralNetworkBuilder builder)
    {
    	this.learningRate = builder.learningRate;
    	
    	this.numberOfNodesByLayer.add(builder.inputLayerSize);
    	this.numberOfNodesByLayer.addAll(builder.layersOfHiddenNodes);
    	this.numberOfNodesByLayer.add(builder.outputLayerSize);
    	
    	this.generateMatricesForExistingNodes();
    }
    
    private void generateMatricesForExistingNodes()
    {
    	weightsForEdges = new ArrayList<>();
    	biasesForEdges = new ArrayList<>();
    	
    	for(int i = 0; i < numberOfNodesByLayer.size() - 1; i++)
    	{
    		weightsForEdges.add(
    			new Matrix(
    				numberOfNodesByLayer.get(i + 1), 
    				numberOfNodesByLayer.get(i)
    			)
    		);
    		
    		biasesForEdges.add(
    			new Matrix(
    				numberOfNodesByLayer.get(i + 1),
    				1
    			)
    		);
    	}
    }
    
}

class NeuralNetworkBuilder
{
	private boolean hasInputLayer = false;
	private boolean hasOutputLayer = false;    
	
    double learningRate;
	
	int inputLayerSize = 0;
	ArrayList<Integer> layersOfHiddenNodes = new ArrayList<>();
	int outputLayerSize = 0;
	
	public NeuralNetworkBuilder()
	{
		this(0.01);
	}
	
	public NeuralNetworkBuilder(double learningRate) 
	{
		this.learningRate = learningRate;
	}
	
	public NeuralNetworkBuilder withInputLayerOfSize(int size)
	{
		hasInputLayer = true;
		inputLayerSize = size;
		return this;
	}
	
	public NeuralNetworkBuilder withHiddenLayerOfSize(int size)
	{
		layersOfHiddenNodes.add(size);
		return this;
	}
	
	public NeuralNetworkBuilder withOutputLayerOfSize(int size)
	{
		hasOutputLayer = true;
		outputLayerSize = size;
		return this;
	}
	
	public NeuralNetwork build()
	{
		if(!canBeBuilt()) 
			throw new 
			NeuralNetworkException(
				"Neural network couldn't have been built due to the lack of"
				+	(!hasInputLayer
					? " the input layer," : "")
				+	((layersOfHiddenNodes.size()<=0)
					? " at least one hidden layer," : "")
				+	(!hasOutputLayer
					? " the output layer," : "")
			);
		
		return new NeuralNetwork(this);
	}
	
	private boolean canBeBuilt() 
	{ 
		return hasInputLayer 
			&& layersOfHiddenNodes.size() > 0 
			&& hasOutputLayer;
	}
}
