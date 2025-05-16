package com.cmr.streetfixer;

public class StatusItem {

	private final String issue;
	private final String status;

	public StatusItem(String issue, String status) {
		this.issue = issue;
		this.status = status;
	}

	public String getIssue() {
		return issue;
	}

	public String getStatus() {
		return status;
	}
}
