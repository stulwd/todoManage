package org.sleekflow.lwd.bo;

import lombok.Data;
import org.sleekflow.lwd.model.Todo;

import java.util.List;

@Data
public class MemberTodos {
    public Long memberId;
    public List<Todo> todos;

    public MemberTodos(Long memberId, List<Todo> todos) {
        this.memberId = memberId;
        this.todos = todos;
    }
}
