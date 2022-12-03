package org.sleekflow.lwd.service.impl;

import org.sleekflow.lwd.mapper.RoleMapper;
import org.sleekflow.lwd.mapper.UserMapper;
import org.sleekflow.lwd.model.User;
import org.sleekflow.lwd.model.UserExample;
import org.sleekflow.lwd.config.security.SecurityUserDetails;
import org.sleekflow.lwd.config.security.jwt.JwtTokenUtil;
import org.sleekflow.lwd.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public User create(String userName, String password) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(passwordEncoder.encode(password));
        return userMapper.insert(user) > 0 ? user : null;
    }

    @Override
    public String login(String userName, String password) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(userName);
        List<User> users = userMapper.selectByExample(example);
        if (users.size() > 0){
            if(passwordEncoder.matches(password, users.get(0).getPassword())){
                return jwtTokenUtil.generateToken(userName);
            }
        }
        return "";
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        UserExample example = new UserExample();
        UserExample.Criteria c = example.createCriteria();
        c.andUserNameEqualTo(username);
        List<User> users = userMapper.selectByExample(example);
        User user = null;
        if (users.size() > 0){
            user = users.get(0);
        }
        if (user != null) {
            return new SecurityUserDetails(user);
        }
        throw new UsernameNotFoundException("error userName or password");
    }

    @Override
    public User getCurrentUser() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        SecurityUserDetails userDetails = (SecurityUserDetails) auth.getPrincipal();
        return userDetails.getUser();
    }

    @Override
    public int updateRole(Long id, List<Long> roleIds) {
        return 0;
    }
}
