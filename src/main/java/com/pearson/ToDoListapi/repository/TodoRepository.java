package com.pearson.ToDoListapi.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import com.pearson.ToDoListapi.model.Todo;

@Repository
public interface TodoRepository extends MongoRepository<Todo,String>{
	Todo findBy_id(ObjectId _id);
	List<Todo> findByuId(String id);
}
