package main.exceptions;

public abstract class ProjectException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public ProjectException(Object message, Object...args)
	{
		super(
			args.length > 0
			? String.format(message.toString(), args)
			: message.toString()
		);
	}
}
