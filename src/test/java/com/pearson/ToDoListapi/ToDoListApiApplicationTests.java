package com.pearson.ToDoListapi;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.pearson.ToDoListapi.model.Todo;
import com.pearson.ToDoListapi.repository.TodoRepository;
import com.pearson.ToDoListapi.service.TodoService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ToDoListApiApplicationTests {

	@Autowired
	private TodoService todoService;
	
	@MockBean
	private TodoRepository todoRepository;
	
	@Test
	public void getTodosTest() {
		when(todoRepository.findAll()).thenReturn(Stream
				.of(new Todo("title1","des1"),new Todo("title2","des2")).collect(Collectors.toList()));
		assertEquals(2,todoService.getToDo().size());
	}
	
	@Test
	public void getTodoByIdTest() {
		ObjectId _id = new ObjectId();
		Todo todo = new Todo("title3","des3");
		when(todoRepository.findBy_id(_id)).thenReturn(todo);
		assertEquals(todo,todoService.getToDoById(_id));
	}
	
	@Test
	public void createTodoTest() {
		Todo todo = new Todo("title4","des4");
		when(todoRepository.save(todo)).thenReturn(todo);
		assertEquals(todo,todoService.create(todo));
	}
	
	@Test
	public void deleteTodoTest() {
		ObjectId id = new ObjectId();
		todoService.deleteToDoById(id);
		verify(todoRepository,times(1)).delete(todoRepository.findBy_id(id));
		
	}
	
	@Test
	public void updateTodoTest() {
		ObjectId _id = new ObjectId();
		Todo todo = new Todo("title3","des3");
		todo.set_id(_id);
		when(todoRepository.findBy_id(_id)).thenReturn(todo);
		todoService.updateToDo(_id,todo);
        verify(todoRepository,times(1)).save(todo);
	
		
	}
	
	@Test
	public void getToDosByUidTest() {
		String id = "128";
		when(todoRepository.findByuId(id)).thenReturn(Stream
				.of(new Todo("title1","des1"),new Todo("title2","des2")).collect(Collectors.toList()));
		assertEquals(2,todoService.getToDosByUid(id).size());
	}

}
