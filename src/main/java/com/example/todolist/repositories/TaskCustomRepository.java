package com.example.todolist.repositories;

import com.example.todolist.models.Task;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskCustomRepository implements CustomRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Task save(Task task) {
        return mongoTemplate.save(task);
    }

    @Override
    public DeleteResult deleteById(String id) {
        return mongoTemplate.remove(findById(id));
    }

    @Override
    public Optional<Task> findById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        List<Task> task_list = mongoTemplate.find(query, Task.class);
        return task_list.isEmpty() ? Optional.empty() : Optional.of(task_list.get(0));
    }

    @Override
    public Page<Task> findByStatus(String status, PageRequest pageRequest) {
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is(status));
        query.with(pageRequest);
        long total = mongoTemplate.count(query, Task.class);
        List<Task> tasks = mongoTemplate.find(query, Task.class);
        return new PageImpl<>(tasks, pageRequest, total);
    }

    @Override
    public Page<Task> findAll(PageRequest pageRequest) {
        Query query = new Query();
        long total = mongoTemplate.count(query, Task.class);
        List<Task> tasks = mongoTemplate.find(query, Task.class);
        return new PageImpl<>(tasks, pageRequest, total);
    }

    @Override
    public boolean existsByTitle(String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(title));
        long count = mongoTemplate.count(query, Task.class);
        return count > 0;
    }
}
