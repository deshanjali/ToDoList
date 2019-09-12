package com.pearson.ToDoListapi.service;

//import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import com.google.api.client.util.DateTime;
//import com.google.api.services.calendar.Calendar;
//import com.google.api.services.calendar.model.Event;
//import com.google.api.services.calendar.model.EventAttendee;
//import com.google.api.services.calendar.model.EventDateTime;
//import com.google.api.services.calendar.model.EventReminder;
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
		//saveEvent();
		return todoRepository.save(todo);	
	}	
	
//	private void saveEvent() {
//		Event event = new Event()
//			    .setSummary("Google I/O 2015")
//			    .setLocation("800 Howard St., San Francisco, CA 94103")
//			    .setDescription("A chance to hear more about Google's developer products.");
//
//			DateTime startDateTime = new DateTime("2015-05-28T09:00:00-07:00");
//			EventDateTime start = new EventDateTime()
//			    .setDateTime(startDateTime)
//			    .setTimeZone("America/Los_Angeles");
//			event.setStart(start);
//
//			DateTime endDateTime = new DateTime("2015-05-28T17:00:00-07:00");
//			EventDateTime end = new EventDateTime()
//			    .setDateTime(endDateTime)
//			    .setTimeZone("America/Los_Angeles");
//			event.setEnd(end);
//
//			String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
//			event.setRecurrence(Arrays.asList(recurrence));
//
//			EventAttendee[] attendees = new EventAttendee[] {
//			    new EventAttendee().setEmail("lpage@example.com"),
//			    new EventAttendee().setEmail("sbrin@example.com"),
//			};
//			event.setAttendees(Arrays.asList(attendees));
//
//			EventReminder[] reminderOverrides = new EventReminder[] {
//			    new EventReminder().setMethod("email").setMinutes(24 * 60),
//			    new EventReminder().setMethod("popup").setMinutes(10),
//			};
//			Event.Reminders reminders = new Event.Reminders()
//			    .setUseDefault(false)
//			    .setOverrides(Arrays.asList(reminderOverrides));
//			event.setReminders(reminders);
//
//			String calendarId = "primary";
//			event = service.events().insert(calendarId, event).execute();
//			System.out.printf("Event created: %s\n", event.getHtmlLink());
//		
//	}

	@Override
	public Todo getToDoById(ObjectId _id) {
		return todoRepository.findBy_id(_id);
	}
	
	@Override
	public ResponseEntity<Todo> deleteToDoById(ObjectId _id) {
		todoRepository.delete(todoRepository.findBy_id(_id));
		return new ResponseEntity<Todo>(HttpStatus.NO_CONTENT);
	}
	
	@Override
	public Todo updateToDo(ObjectId _id, Todo todo) {
		Todo updatedToDo = todoRepository.findBy_id(_id);
		updatedToDo.setTitle(todo.getTitle());
		updatedToDo.setDescription(todo.getDescription());
		return todoRepository.save(updatedToDo);
	}

	@Override
	public List<Todo> getToDosByUid(String id) {
		return todoRepository.findByuId(id);
	}

	@Override
	public void settingUserId(Object principal) {
		this.uid= principal;
	}

//	@Override
//	public void createCalendar() {
//		// Initialize Calendar service with valid OAuth credentials
//		Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
//		    .setApplicationName("applicationName").build();
//
//		// Create a new calendar
//		com.google.api.services.calendar.model.Calendar calendar = new Calendar();
//		calendar.setSummary("calendarSummary");
//		calendar.setTimeZone("America/Los_Angeles");
//
//		// Insert the new calendar
//		Calendar createdCalendar = service.calendars().insert(calendar).execute();
//
//		System.out.println(createdCalendar.getId());
//		
//	}
//	
	
}
