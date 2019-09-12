package com.pearson.ToDoListapi.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="todos")
public class Todo {
	
	@Id
	ObjectId _id;
	String title;
	String description;
	Date timestamp;
	
	Object uId;

	public Todo(String title, String description) {
		this.title = title;
		this.description = description;
		this.timestamp = new Date();
	}

	public String get_id() {
		return _id.toHexString();
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public Object getUserId() {
		return uId;
	}

	public void setUserId(Object userId) {
		this.uId = userId;
	}
	
}
