package com.pearson.ToDoListapi.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpTransport;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.Calendar.Events.CalendarImport;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.pearson.ToDoListapi.model.Todo;
import com.pearson.ToDoListapi.repository.TodoRepository;

@Service
public class TodoServiceImpl implements TodoService {
	
	private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalendarImport.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerPort.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
	
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
		saveEvent(todo);
		return todoRepository.save(todo);	
	}	
	
	private void saveEvent(Todo todo) {
		Event event = new Event()
			    .setSummary(todo.getTitle())
			    .setDescription(todo.getDescription());

			DateTime startDateTime = new DateTime(todo.getTimestamp());
			EventDateTime start = new EventDateTime()
			    .setDateTime(startDateTime)
			    .setTimeZone("America/Los_Angeles");
			event.setStart(start);

			DateTime endDateTime = new DateTime(todo.getTimestamp());
			EventDateTime end = new EventDateTime()
			    .setDateTime(endDateTime)
			    .setTimeZone("America/Los_Angeles");
			event.setEnd(end);

			String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
			event.setRecurrence(Arrays.asList(recurrence));			

			EventReminder[] reminderOverrides = new EventReminder[] {
			    new EventReminder().setMethod("email").setMinutes(24 * 60),
			    new EventReminder().setMethod("popup").setMinutes(10),
			};
			Event.Reminders reminders = new Event.Reminders()
			    .setUseDefault(false)
			    .setOverrides(Arrays.asList(reminderOverrides));
			event.setReminders(reminders);

			// Initialize Calendar service with valid OAuth credentials
			Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
			    .setApplicationName("applicationName").build();
			
			String calendarId = "primary";
			event = service.events().insert(calendarId, event).execute();
			System.out.printf("Event created: %s\n", event.getHtmlLink());
		
	}

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

	@Override
	public void createCalendar() {
		JsonFactory jsonFactory;
		com.google.api.client.http.HttpTransport httpTransport;
		HttpRequestInitializer credentials;
		// Initialize Calendar service with valid OAuth credentials
		Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
		    .setApplicationName("applicationName").build();

		// Create a new calendar
		com.google.api.services.calendar.model.Calendar calendar = new Calendar();
		calendar.setSummary("calendarSummary");
		calendar.setTimeZone("America/Los_Angeles");

		// Insert the new calendar
		Calendar createdCalendar = service.calendars().insert(calendar).execute();

		System.out.println(createdCalendar.getId());
		
	}
	
	
}
