package com.m4rc310.rcp.graphql;

public class GraphQLQueryException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GraphQLQueryException() {
		super();
	}
	
	public GraphQLQueryException(String message) {
		super(message);
	}
	
	public GraphQLQueryException(String message, Throwable cause) {
        super(message, cause);
	}
	
	public GraphQLQueryException(Throwable cause) {
		super(cause);
	}
	
}
