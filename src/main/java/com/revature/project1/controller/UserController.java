package com.revature.project1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.project1.entity.User;
import com.revature.project1.service.ReimbursementService;
import com.revature.project1.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final ReimbursementService reimbursementService;

    @Autowired
    public UserController(UserService userService, ReimbursementService reimbursementService) {
        this.userService = userService;
        this.reimbursementService = reimbursementService;
    }

    @PostMapping("/register")
    private ResponseEntity<User> resgisterUser(@RequestBody User user) {
        if (!user.getUsername().isBlank() && (user.getPassword().length() >= 6) &&
                !isDuplicateAttempt(userService.getAllUsers(), user)) {
            return ResponseEntity.ok().body(userService.registerUser(user));
        } else {
            return ResponseEntity.status(409).body(new User());
        }
    }

    public boolean isDuplicateAttempt(List<User> users, User user) {
        boolean isDuplicate = false;
        for (User theUser : users) {
            if (user.getUsername().equals(theUser.getUsername())) {
                isDuplicate = true;
                break;
            }
        }
        return isDuplicate;
    }

}
