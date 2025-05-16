package com.cmr.streetfixer;

public class Reports {
	String issue, description, suggestions, location, image, time, isSolved;

	public Reports() {
	}

	public String getImage() { return image; }

	public void setImage(String image) { this.image = image; }

	public String getTime() { return time; }

	public void setTime(String time) { this.time = time; }

	public String getIsSolved() { return isSolved; }

	public void setIsSolved(String isSolved) { this.isSolved = isSolved; }

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Reports(String issue, String description, String suggestions, String location, String image, String time, String isSolved) {
		this.issue = issue;
		this.description = description;
		this.suggestions = suggestions;
		this.location = location;
		this.image = image;
		this.time = time;
		this.isSolved = isSolved;
	}
}
