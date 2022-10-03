package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import main.exceptions.MatrixException;

public class Matrix
{
	double[][] 
		values;
	int 
		numberOfRows, 
		numberOfColumns;
	
	public Matrix(int numberOfRows, int numberOfColumns, double[] values)
	{
		this(numberOfRows, numberOfColumns, doubleArrayToArrayList(values));
	}
	
	public Matrix(int numberOfRows, int numberOfColumns, List<Double> values)
	{
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
		
		this.values = new double[numberOfRows][numberOfColumns];
		
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				this.values[y][x] = values.get(y * numberOfColumns + x);
			}
		}
	}

	public Matrix(int numberOfRows, int numberOfColumns)
	{
		this.numberOfRows = numberOfRows;
		this.numberOfColumns = numberOfColumns;
		
		this.values = new double[numberOfRows][numberOfColumns];
		
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				this.values[y][x] = Math.random()*2-1;
			}
		}
	}
	
	public void add(double addition)
	{
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				this.values[y][x] += addition;
			}
		}
	}
	
	public void add(Matrix other) throws MatrixException
	{
		if(!hasSameDimentions(other)) throw new MatrixException("Other matrix cannot be added (size mismatch).");
		
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				this.values[y][x] += other.values[y][x];
			}
		}
	}
	
	public void subtract(double subtraction)
	{
		add(-subtraction);
	}

	public void subtract(Matrix other) throws MatrixException
	{
		if(!hasSameDimentions(other)) throw new MatrixException("Other matrix cannot be subtracted (size mismatch).");
		
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				this.values[y][x] -= other.values[y][x];
			}
		}
	}

	public void multiply(double multiplication)
	{
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				this.values[y][x] *= multiplication;
			}
		}
	}
	
	public Matrix multiply(Matrix other) throws MatrixException
	{
		if(!canBeMultipliedBy(other)) throw new MatrixException("Other matrix cannot be multiplied (size mismatch).");
		
		Matrix output = new Matrix(this.numberOfRows, other.numberOfColumns);
		
		for(int ty = 0; ty < this.numberOfRows; ty++)
		{
			for(int ox = 0; ox < other.numberOfColumns; ox++)
			{
				double sum = 0;
				for(int tx = 0; tx < this.numberOfColumns; tx++)
				{
					sum += this.values[ty][tx] * other.values[tx][ox];
				}
				output.values[ty][ox] = sum;
			}
		}
		
		return output;
	}
	
	public void multiplyValueWise(Matrix other) throws MatrixException
	{
		if(!this.hasSameDimentions(other)) throw new MatrixException("Other matrix cannot be multiplied value by value (size mismatch).");
	
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				this.values[y][x] *= other.values[y][x];
			}
		}
	}

	public void sigmoid()
	{
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				this.values[y][x] = 1/(1+Math.exp(-this.values[y][x])); 
			}
		}
	}
	
	public void derivativeSigmoid()
	{
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				this.values[y][x] *= (1 - this.values[y][x]);
			}
		}
	}
	
	public Matrix clone()
	{
		return new Matrix(this);
	}
	
	public ArrayList<Double> toArray()
	{
		ArrayList<Double> output = new ArrayList<>();
		
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				output.add(this.values[y][y]);
			}
		}
		
		return output;
	}

	public void forEachRow(Consumer<? super double[]> consumer)
	{
		Arrays
		.asList(values)
		.stream()
		.forEach((d) -> {
			consumer.accept(d);	
		});
	}
	
	public void forEachDouble(Consumer<? super Double> consumer)
	{
		forEachRow((row)->{
			for(double d: row)
			{
				consumer.accept(d);
			}
		});
	}
		
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder(this.numberOfRows + " " + this.numberOfColumns);
		
		forEachDouble((d) -> {builder.append(" " + d);});
		
		return builder.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o instanceof Matrix)
		{
			Matrix m = (Matrix) o;
			
			if(m.numberOfColumns == this.numberOfColumns
			&& m.numberOfRows == this.numberOfRows)
			{
				for(int y = 0; y < this.numberOfRows; y++)
				{
					for(int x = 0; x < this.numberOfColumns; x++)
					{
						if(this.values[y][x] != m.values[y][x])
							return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public static Matrix parse(String input) throws MatrixException
	{
		try
		{
			String[] inputSplit = input.split(" +"); 
			
			int i = 0;
			
			int rowsNumber = Integer.parseInt(inputSplit[i++]);
			int colsNumber = Integer.parseInt(inputSplit[i++]);
			
			ArrayList<Double> values = new ArrayList<>();
			
			for(; i < (rowsNumber * colsNumber) + 2; i++)
			{
				values.add(Double.parseDouble(inputSplit[i]));
			}
			
			return new Matrix(rowsNumber, colsNumber, values);
		}
		catch(Exception e)
		{
			throw new MatrixException(e);
		}
	}
	
	
	public static Matrix getAddition(Matrix first, Matrix second) throws MatrixException
	{
		Matrix output = first.clone();
		output.add(second);
		return output;
	}

	public static Matrix getSubtraction(Matrix subtracted, Matrix subtractee) throws MatrixException
	{
		Matrix output = subtracted.clone();
		output.subtract(subtractee);
		return output;
	}
	
	public static Matrix getMultiplication(Matrix multiplicated, Matrix multiplicatee) throws MatrixException
	{
		return multiplicated.multiply(multiplicatee);
	}
	
	public static Matrix getMultiplicationValueWise(Matrix multiplicated, Matrix multiplicatee) throws MatrixException
	{
		Matrix output = multiplicated.clone();
		output.multiplyValueWise(multiplicatee);
		return output;
	}
	
	public static Matrix getSigmoidOf(Matrix input)
	{
		Matrix output = input.clone();
		output.sigmoid();
		return output;
	}
	
	public static Matrix getDerivativeSigmoidOf(Matrix input)
	{
		Matrix output = input.clone();
		output.derivativeSigmoid();
		return output;
	}
	
	public static Matrix transpose(Matrix input)
	{
		Matrix output = new Matrix(input.numberOfColumns, input.numberOfRows);
		
		for(int y = 0; y < input.numberOfRows; y++)
		{
			for(int x = 0; x < input.numberOfColumns; x++)
			{
				output.values[x][y] = input.values[y][x];
			}
		}
		
		return output;
	}
	
	public static Matrix fromOneDimensionalArray(double[] array)
	{
		Matrix output = new Matrix(array.length, 1);
		
		for(int y = 0; y < output.numberOfRows; y++)
		{
			output.values[y][0] = array[y];
		}
		
		return output;
	}
	
	private boolean hasSameDimentions(Matrix other)
	{
		return this.numberOfColumns == other.numberOfColumns 
			&& this.numberOfRows	== other.numberOfRows;
	}
	
	private boolean canBeMultipliedBy(Matrix other)
	{
		return this.numberOfColumns == other.numberOfRows;
	}
	
	private static ArrayList<Double> doubleArrayToArrayList(double[] values)
	{
		ArrayList<Double> output = new ArrayList<>();
		
		for(double d: values) 
		{
			output.add(d);
		}
		
		return output;
	}

	private Matrix(Matrix other)
	{
		this.numberOfRows = other.numberOfRows;
		this.numberOfColumns = other.numberOfColumns;
		
		this.values = new double[numberOfRows][numberOfColumns];
		for(int y = 0; y < numberOfRows; y++)
		{
			for(int x = 0; x < numberOfColumns; x++)
			{
				this.values[y][x] = other.values[y][x];
			}
		}
	}
	
}
