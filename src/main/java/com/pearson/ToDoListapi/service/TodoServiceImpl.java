package com.pearson.ToDoListapi.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.pearson.ToDoListapi.CalendarApi.CalendarApi;
import com.pearson.ToDoListapi.model.Todo;
import com.pearson.ToDoListapi.repository.TodoRepository;

@Service
public class TodoServiceImpl implements TodoService {
	@Autowired
	private TodoRepository todoRepository;
	private Object uid;
	

	@Override
	public List<Todo> getToDo(){
		return todoRepository.findByuId(uid.toString());
	}
	
	@Override
	public Todo create(Todo todo){
		todo.set_id(ObjectId.get());
		todo.setUserId(uid);
		try {
			CalendarApi.setEvent(todo);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return todoRepository.save(todo);	
	}	
	
	@Override
	public Todo getToDoById(ObjectId _id) {
		return todoRepository.findBy_id(_id);
	}
	
	@Override
	public ResponseEntity<Todo> deleteToDoById(ObjectId _id) {
		 final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(todoRepository.findBy_id(_id).getUserId().toString().equals(authentication.getName())) {
			try {
				CalendarApi.deleteEvent(todoRepository.findBy_id(_id));
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			todoRepository.delete(todoRepository.findBy_id(_id));
			return new ResponseEntity<Todo>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Todo>(HttpStatus.UNAUTHORIZED);
	}
	
	@Override
	public Todo updateToDo(ObjectId _id, Todo todo) {
		 final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if(todoRepository.findBy_id(_id).getUserId().toString().equals(authentication.getName())) {
				Todo updatedToDo = todoRepository.findBy_id(_id);
				updatedToDo.setTitle(todo.getTitle());
				updatedToDo.setDescription(todo.getDescription());
				try {
					CalendarApi.updateEvent(updatedToDo);
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return todoRepository.save(updatedToDo);
			}
			return null;
	}

	@Override
	public List<Todo> getToDosByUid(String id) {
		return todoRepository.findByuId(id);
	}

	@Override
	public void settingUserId(Object principal) {
		this.uid= principal;
	}
}
