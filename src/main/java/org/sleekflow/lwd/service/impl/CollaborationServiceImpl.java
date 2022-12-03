package org.sleekflow.lwd.service.impl;

import org.sleekflow.lwd.bo.CollaborateInfo;
import org.sleekflow.lwd.bo.MemberTodos;
import org.sleekflow.lwd.model.Todo;
import org.sleekflow.lwd.service.CollaborationService;
import org.sleekflow.lwd.service.RedisService;
import org.sleekflow.lwd.service.TodoService;
import org.sleekflow.lwd.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CollaborationServiceImpl implements CollaborationService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private TodoService todoService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.colla}")
    private String REDIS_COLLA;

    @Override
    public boolean isMember(CollaborateInfo c) {
        return c.getMembers().contains(usersService.getCurrentUser().getId());
    }

    @Override
    public CollaborateInfo getCollaborationInfo(String CollaNo){
        return (CollaborateInfo) redisService.get(CollaNo);
    }

    @Override
    public List<MemberTodos> getAllTodos(CollaborateInfo c){
        ArrayList<MemberTodos> allTodos = new ArrayList<>();
        Set<Long> ids = c.getSharedIds();
        for (Long id : ids) {
            MemberTodos memberTodos = new MemberTodos(id, todoService.getTodosByUserId(id));
            allTodos.add(memberTodos);
        }
        return allTodos;
    }

    @Override
    public boolean isShared(CollaborateInfo c, Long id){
        return c.getSharedIds().contains(id);
    }

    @Override
    public boolean editTodos(CollaborateInfo c, List<Todo> todos) {
        for (Todo todo : todos) {
            if (!isShared(c, todo.getUserId())){
                return false;
            }
        }
        for (Todo todo : todos) {
            if (todoService.updateById(todo.getId(), todo)){
                return false;
            }
        }
        return true;
    }
}
