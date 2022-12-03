package org.sleekflow.lwd.service;

import org.sleekflow.lwd.bo.CollaborateInfo;
import org.sleekflow.lwd.bo.MemberTodos;
import org.sleekflow.lwd.model.Todo;

import java.util.List;

public interface CollaborationService {
    boolean isMember(CollaborateInfo c);
    CollaborateInfo getCollaborationInfo(String CollaNo);
    List<MemberTodos> getAllTodos(CollaborateInfo c);
    boolean editTodos(CollaborateInfo c, List<Todo> todos);
    boolean isShared(CollaborateInfo c, Long id);
}
