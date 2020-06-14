package io.swagger.client.model.mls;

public enum MStatus {
	CLOSED("closed"),
	PAUSED("paused"), 
	ACTIVE("active");
	
	
	private String status;

	private MStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
}
