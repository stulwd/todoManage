package org.sleekflow.lwd.service.impl;

import org.apache.logging.log4j.util.Strings;
import org.sleekflow.lwd.mapper.TodoMapper;
import org.sleekflow.lwd.model.Todo;
import org.sleekflow.lwd.model.TodoExample;
import org.sleekflow.lwd.service.TodoService;
import org.sleekflow.lwd.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoMapper todoMapper;
    @Autowired
    private UsersService usersService;

    @Override
    public boolean create(Todo todo) {
        todo.setId(null);
        todo.setUserId(usersService.getCurrentUser().getId());
        return todoMapper.insert(todo) > 0;
    }

    @Override
    public Long getOwner(Long id){
        TodoExample example = new TodoExample();
        example.createCriteria().andIdEqualTo(id);
        List<Todo> todos = todoMapper.selectByExample(example);
        if (todos.size() > 0){
            return todos.get(0).getUserId();
        }
        return null;
    }

    @Override
    public boolean updateById(Long id, Todo todo) {
        todo.setId(id);
        return todoMapper.updateByPrimaryKeySelective(todo) > 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return todoMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public Todo getById(Long id) {
        Todo todo = todoMapper.selectByPrimaryKey(id);
        return todo;
    }

    @Override
    public List<Todo> getTodosByUserId(Long id) {
        TodoExample example = new TodoExample();
        TodoExample.Criteria c = example.createCriteria();
        c.andUserIdEqualTo(id);
        List<Todo> todos = todoMapper.selectByExample(example);
        return todos;
    }

    @Override
    public List<Todo> filter(Todo todo) {
        todo.setId(null);
        TodoExample example = new TodoExample();
        TodoExample.Criteria criteria = example.createCriteria();
        if (todo.getName() != null){
            criteria.andNameLike("%" + todo.getName() + "%");
        }
        if (todo.getDueDate() != null){
            criteria.andDueDateLessThanOrEqualTo(todo.getDueDate());
        }
        if (todo.getUserId() != null){
            criteria.andUserIdEqualTo(todo.getUserId());
        }
        if (todo.getStatus() != null){
            criteria.andStatusEqualTo(todo.getStatus());
        }
        List<Todo> todos = todoMapper.selectByExample(example);
        return todos;
    }

    @Override
    public List<Todo> sortList(List<String> sortBy) {
        TodoExample example = new TodoExample();
        TodoExample.Criteria criteria = example.createCriteria();
        StringBuilder sb = new StringBuilder();
        String orderByClause = Strings.join(sortBy, ',');
        example.setOrderByClause(orderByClause);
        List<Todo> todos = todoMapper.selectByExample(example);
        return todos;
    }
}
