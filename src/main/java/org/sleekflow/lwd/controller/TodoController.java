package org.sleekflow.lwd.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.sleekflow.lwd.common.CommonResult;
import org.sleekflow.lwd.model.Todo;
import org.sleekflow.lwd.model.TodoExample;
import org.sleekflow.lwd.service.TodoService;
import org.sleekflow.lwd.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Api(tags = "TodoController")
@RequestMapping("/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;
    @Autowired
    private UsersService usersService;


    @ApiOperation("get my todos")
    @RequestMapping(value = "/getMyTodos", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<Todo>> getMyTodos() {
        List<Todo> todos = todoService.getTodosByUserId(usersService.getCurrentUser().getId());
        return CommonResult.success(todos);
    }

    @ApiOperation("get todo item")
    @RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<Todo> getById(@PathVariable Long id) {
        Todo todo = todoService.getById(id);
        return CommonResult.success(todo);
    }

    @ApiOperation("add my todo")
    @RequestMapping(value = "/addMyTodo", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult addMyTodo(@RequestBody Todo todo) {
        Boolean success = todoService.create(todo);
        if (success) {
            return CommonResult.success(todo);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("update todo item by id")
    @RequestMapping(value = "/updateById/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateById(@PathVariable Long id,
                               @RequestBody Todo todo) {
        Long owner = todoService.getOwner(id);
        if (owner.equals(usersService.getCurrentUser().getId())){
            if (todoService.updateById(id, todo)){
                return CommonResult.success(todo);
            }
        }
        return CommonResult.failed();
    }

    @ApiOperation("delete todo item by id")
    @RequestMapping(value = "/deleteById/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult deleteById(@PathVariable Long id) {
        Long owner = todoService.getOwner(id);
        if (owner.equals(usersService.getCurrentUser().getId())){
            if (todoService.deleteById(id)){
                return CommonResult.success(null);
            }
        }
        return CommonResult.failed();
    }

    @ApiOperation("filter searching")
    @RequestMapping(value = "/filtering", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<Todo>> filter(@RequestBody Todo todo){
        List<Todo> res = todoService.filter(todo);
        return CommonResult.success(res);
    }

    @ApiOperation("sort searching")
    @RequestMapping(value = "/sorting", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<Todo>> sort(@RequestBody List<String> sortBy){
        List<Todo> res = todoService.sortList(sortBy);
        return CommonResult.success(res);
    }


}
