package org.sleekflow.lwd.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.sleekflow.lwd.common.CommonResult;
import org.sleekflow.lwd.model.User;
import org.sleekflow.lwd.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "UserController")
@RequestMapping("/user")
public class UserController {
    /* Authorization */
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    /* Bearer  */
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private UsersService userService;

    @ApiOperation(value = "register user")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<User> register(String userName, String password) {
        User user = userService.create(userName, password);
        if (user != null) {
            return CommonResult.success(user);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation(value = "login and return token")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(String userName, String password) {
        String token = userService.login(userName, password);
        if (token == null || token.isEmpty()) {
            return CommonResult.validateFailed("error userName or password");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return CommonResult.success(tokenMap);
    }


    @ApiOperation(value = "logout")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult logout() {
        return CommonResult.success(null);
    }


    @ApiOperation("allocate roles to user")
    @RequestMapping(value = "/allocateRoles", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult updateRole(@RequestParam("userId") Long userId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = userService.updateRole(userId, roleIds);
        if (count >= 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
