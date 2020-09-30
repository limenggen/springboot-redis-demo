package com.morgan.controller;

import com.morgan.entity.User;
import com.morgan.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/redis")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/queryAll")
    public List<User> queryAll(){
        return userService.queryAll();
    }

    @RequestMapping("/findUserById")
    public Map<String, Object> findUserById(@RequestParam int id){
        User user = userService.findUserById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("uid", user.getUid());
        result.put("uname", user.getUserName());
        result.put("pass", user.getPassword());
        result.put("salary", user.getSalary());
        return  result;
    }

    @RequestMapping("updateUser")
    public String updateUser(){
        User user = new User();
        user.setUid(123);
        user.setUserName("cat");
        user.setPassword("miaomiao");
        user.setSalary(4000);
        int result = userService.updateUser(user);
        if(result != 0)
            return "update user success";
        return "fail";
    }

    @RequestMapping("/deleteUserById")
    public String deleteUserById(@RequestParam int id){
        int result = userService.deleteUserById(id);
        if(result != 0)
            return "delete success";
        return "delete fail";
    }
}
