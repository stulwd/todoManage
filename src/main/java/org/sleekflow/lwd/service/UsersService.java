package org.sleekflow.lwd.service;

import org.sleekflow.lwd.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UsersService {
    User create(String userName, String password);
    String login(String userName, String password);
    int updateRole(Long id, List<Long> roleIds);
    UserDetails loadUserByUsername(String username);
    User getCurrentUser();
}
