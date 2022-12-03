package org.sleekflow.lwd.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.sleekflow.lwd.bo.CollaborateInfo;
import org.sleekflow.lwd.bo.MemberTodos;
import org.sleekflow.lwd.common.CommonResult;
import org.sleekflow.lwd.model.Todo;
import org.sleekflow.lwd.service.CollaborationService;
import org.sleekflow.lwd.service.RedisService;
import org.sleekflow.lwd.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@Api(tags = "CollaborationController")
@RequestMapping("/collaborate")
public class CollaborationController {

    @Autowired
    private CollaborationService collaborationService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RedisService redisService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.colla}")
    private String REDIS_COLLA;
//    @Value("${redis.key.collaId}")
//    private String REDIS_COLLA_ID;


    @ApiOperation("create my collaboration")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> createCollaboration(String password){
        Long user_id = usersService.getCurrentUser().getId();
        String key = REDIS_DATABASE + ":" + REDIS_COLLA + ":" + user_id;
        HashSet<Long> members = new HashSet<>();
        members.add(user_id);
        redisService.set(key, new CollaborateInfo(user_id, members, new HashSet<>(), password), REDIS_EXPIRE);
        return CommonResult.success(key);
    }

    @ApiOperation("close my collaboration")
    @RequestMapping(value = "/close", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> closeCollaboration(){
        Long user_id = usersService.getCurrentUser().getId();
        String key = REDIS_DATABASE + ":" + REDIS_COLLA + ":" + user_id;
        redisService.del(key);
        return CommonResult.success("close successfully");
    }

    @ApiOperation("join a collaboration")
    @RequestMapping(value = "/join", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult joinCollaboration(String collaNO, String password){
        CollaborateInfo c = (CollaborateInfo) redisService.get(collaNO);
        if (c == null){
            return CommonResult.failed("non existed Collaboration");
        }
        if (c.getPassword().equals(password)){
            c.getMembers().add(usersService.getCurrentUser().getId());
            redisService.set(collaNO, c);
            return CommonResult.success("join collaboration successfully");
        }else {
            return CommonResult.failed("error password!");
        }
    }

    @ApiOperation("exit a collaboration")
    @RequestMapping(value = "/exit", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult exitCollaboration(String collaNO){
        CollaborateInfo c = (CollaborateInfo) redisService.get(collaNO);
        if (c == null){
            return CommonResult.failed("non existed Collaboration");
        }
        c.getMembers().remove(usersService.getCurrentUser().getId());
        c.getSharedIds().remove(usersService.getCurrentUser().getId());
        redisService.set(collaNO, c);
        return CommonResult.success("exit collaboration successfully");
    }

    @ApiOperation("get collaboration info")
    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CollaborateInfo> getInfo(String collaNO){
        CollaborateInfo c = collaborationService.getCollaborationInfo(collaNO);
        if (collaborationService.isMember(c)){
            return CommonResult.success(c);
        }else{
            return CommonResult.failed("is not a member of collaboration");
        }
    }

    @ApiOperation("share My Todos")
    @RequestMapping(value = "/share", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult shareMyTodos(String collaNo){
        CollaborateInfo c = collaborationService.getCollaborationInfo(collaNo);
        if (c != null && collaborationService.isMember(c)){
            Set<Long> ids = c.getSharedIds();
            Long id = usersService.getCurrentUser().getId();
            ids.add(id);
            redisService.set(collaNo, c);
            return CommonResult.success("shared my todos successfully");
        }else{
            return CommonResult.failed("is not a member of collaboration");
        }
    }

    @ApiOperation("get todos in whole collaboration")
    @RequestMapping(value = "/getAllTodos", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<MemberTodos>> getAllTodos(String collaNo){
        CollaborateInfo c = collaborationService.getCollaborationInfo(collaNo);
        if (collaborationService.isMember(c)){
            List<MemberTodos> allTodos = collaborationService.getAllTodos(c);
            return CommonResult.success(allTodos);
        }else {
            return CommonResult.failed("is not a member of collaboration");
        }
    }

    @ApiOperation("edit in collaboration")
    @RequestMapping(value = "/editTodos", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult editTodos(String collaNo, @RequestBody List<Todo> todos){
        CollaborateInfo c = collaborationService.getCollaborationInfo(collaNo);
        if (c != null && collaborationService.isMember(c)){
            if (collaborationService.editTodos(c, todos)){
                return CommonResult.success("edit todos successfully");
            }else{
                return CommonResult.success("edit todos error");
            }
        }else{
            return CommonResult.failed("is not a member of collaboration");
        }
    }





}
