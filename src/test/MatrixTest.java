package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import main.Matrix;

public class MatrixTest
{
	static Matrix sizeThreeAllOnes;
	static Matrix sizeThreeAllTens;
	static Matrix sizeThreeIdentity;	
	static Matrix sizeThreeIncreasing;
	static Matrix sizeThreeDecreasing;
	
	@BeforeAll
	public static void setUp()
	{
		double[] data;
				
		data = new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1};
		sizeThreeAllOnes = new Matrix(3, 3, data);
		
		data = new double[]{10, 10, 10, 10, 10, 10, 10, 10, 10};
		sizeThreeAllTens = new Matrix(3, 3, data);

		data = new double[]{1, 0, 0, 0, 1, 0, 0, 0, 1};
		sizeThreeIdentity = new Matrix(3, 3, data);
		
		data = new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
		sizeThreeIncreasing = new Matrix(3, 3, data);
		
		data = new double[]{9, 8, 7, 6, 5, 4, 3, 2, 1};
		sizeThreeDecreasing = new Matrix(3, 3, data);
		
	}
	
	/*/
		     2  0  0
	         5  1  0
	         0  3  4
	1  0  8  2 24 32
	0  2  3 10 
	5  1  3 
	
	/**/
	
	@Test
	public void matrixMultiplicationWithIdentityMatrixReturnsCorrectOutcome()
	{
		Matrix expected = sizeThreeIncreasing.clone();
		Matrix actual = Matrix.getMultiplication(sizeThreeIncreasing, sizeThreeIdentity);
		
		assertEquals(
			expected,
			actual
		);
	}
	
	@Test
	public void matrixScalarMultiplicationReturnsCorrectOutcome()
	{
		Matrix actual = sizeThreeAllOnes.clone();
		actual.multiply(10d);
		
		assertEquals(
			sizeThreeAllTens,
			actual
		);
	}
	
	@Test
	public void matrixAdditionReturnsCorrectOutcome()
	{
		assertEquals(
			sizeThreeAllTens,
			Matrix.getAddition(sizeThreeDecreasing, sizeThreeIncreasing)
		);
	}
	
	@Test
	public void equalsWorksAsExpected()
	{		
		Matrix 
			m1 = new Matrix(5, 3),
			m2 = m1.clone(),
			m3 = new Matrix(5, 3);
		
		assertEquals(m1, m2);
		assertNotEquals(m2, m3);
		assertNotEquals(m1, m3);		
	}
	
	@Test
	public void toStringingCreatesParsableString()
	{
		Matrix
			m2 = new Matrix(10, 10);

		assertEquals(m2, Matrix.parse(m2.toString()));
	}
	
}
