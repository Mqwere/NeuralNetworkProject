package main.exceptions;

public class MatrixException extends ProjectException
{
	private static final long serialVersionUID = 1L;

	public MatrixException(Object message, Object... args)
	{
		super(message, args);
	}

}
