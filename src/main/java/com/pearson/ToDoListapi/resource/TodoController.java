package com.pearson.ToDoListapi.resource;


import java.security.Principal;
import java.util.List;



import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pearson.ToDoListapi.model.Todo;
import com.pearson.ToDoListapi.service.TodoService;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {
	@Autowired
	private TodoService todoService;
	
	@GetMapping("/")
	public List<Todo> getToDos(){
		return todoService.getToDo();
	}
	
	@PostMapping
	public Todo addToDo(@RequestBody final Todo toDo){
		return todoService.create(toDo);
	}
	
	@GetMapping("/{id}")
	public Todo getToDoById(@PathVariable("id") final ObjectId id) {
		return todoService.getToDoById(id);
	}
	
	@GetMapping("user/{userid}")
	public List<Todo> getToDosByUserId(@PathVariable("userid") final String uid) {
		return todoService.getToDosByUid(uid);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Todo> delToDoById(@PathVariable("id") final ObjectId id) {
		return todoService.deleteToDoById(id);
	}
	
	@PutMapping("/{id}")
	public Todo updateUser(@PathVariable("id") final ObjectId id,@RequestBody final Todo toDo) {
		return todoService.updateToDo(id, toDo);
	}
	
	@GetMapping(value = "/user")
	public String currentUserName(Principal principal) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            final String currentPrincipalName = principal.getName();
            System.out.println("Authentication: " + authentication);
            System.out.println("Principal: " + authentication.getPrincipal());
            todoService.settingUserId(authentication.getPrincipal());
            return currentPrincipalName;
        }

        return null;
    }
}
