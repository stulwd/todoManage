package org.sleekflow.lwd.service;

import org.sleekflow.lwd.model.Todo;

import java.util.List;

public interface TodoService {
    boolean create(Todo todo);
    boolean updateById(Long id, Todo todo);
    boolean deleteById(Long id);
    Todo getById(Long id);
    List<Todo> getTodosByUserId(Long id);
    List<Todo> filter(Todo todo);
    List<Todo> sortList(List<String> sortBy);
    Long getOwner(Long id);
}
