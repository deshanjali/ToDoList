package com.pearson.ToDoListapi.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pearson.ToDoListapi.model.Todo;

@Service
public interface TodoService {

	List<Todo> getToDo();

	Todo create(Todo todo);

	Todo getToDoById(ObjectId _id);

	ResponseEntity<Todo> deleteToDoById(ObjectId _id);

	Todo updateToDo(ObjectId _id, Todo todo);

	List<Todo> getToDosByUid(String id);

	void settingUserId(Object principal);
	
	void createCalendar();

}