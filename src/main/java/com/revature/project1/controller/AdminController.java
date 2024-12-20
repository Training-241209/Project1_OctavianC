package com.revature.project1.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.project1.entity.Reimbursement;
import com.revature.project1.entity.User;
import com.revature.project1.service.AuthenticationService;
import com.revature.project1.service.ReimbursementService;
import com.revature.project1.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ReimbursementService reimbursementService;
    private final AuthenticationService authenticationService;

    @Autowired
    public AdminController(UserService userService, ReimbursementService reimbursementService,
            AuthenticationService authenticationService) {
        this.userService = userService;
        this.reimbursementService = reimbursementService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/allusers")
    private ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        if (allUsers.size() > 0) {
            return ResponseEntity.ok().body(allUsers);
        } else {
            return ResponseEntity.ok().body(new ArrayList<>());
        }
    }

    @GetMapping("/allpending")
    private ResponseEntity<List<Reimbursement>> allPendingTickets() {
        return ResponseEntity.ok().body(reimbursementService.findPendingTickets("PENDING"));
    }

    @PutMapping("/acceptticket/{id}")
    private ResponseEntity<Reimbursement> updateTicketStatus(@PathVariable int id,
            @RequestBody String update) {
        return ResponseEntity.ok().body(reimbursementService.updateStatus((long) id, "APPROVED"));

    }

    @PutMapping("/denyticket/{id}")
    private ResponseEntity<Reimbursement> denyTicket(@PathVariable int id,
            @RequestBody String update) {
        return ResponseEntity.ok().body(reimbursementService.updateStatus((long) id, "DENIED"));

    }

    @PostMapping("/login")
    private ResponseEntity<User> loginIntoAccount(@RequestBody User user) {
        int statusCode = 404;
        List<User> users = userService.getAllUsers();
        User targetUser = new User();
        if (authenticationService.isLoggedIn(user)) {
            statusCode = 200;
            for (User someUser : users) {
                if (user.getUsername().equals(someUser.getUsername())) {
                    targetUser = someUser;
                    break;
                }

            }
        } else if (users.contains(user)) {
            statusCode = 403;
        }

        return ResponseEntity.status(statusCode).body(targetUser);
    }

}
