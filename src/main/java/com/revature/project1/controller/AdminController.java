package com.revature.project1.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.project1.entity.Reimbursement;
import com.revature.project1.entity.User;
import com.revature.project1.service.AuthenticationService;
import com.revature.project1.service.ReimbursementService;
import com.revature.project1.service.UserService;
import com.revature.project1.utility.JwtUtil;

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

    @PutMapping("/denyticket/{id}")
    private ResponseEntity<Reimbursement> updateTicketStatus(@PathVariable int id,
            @RequestBody String update, @RequestHeader("Authorization") String myToken) {
        if (myToken == null || !myToken.startsWith("Bearer ")) {
            return ResponseEntity.status(403).body(new Reimbursement());
        } else {
            String possibleToken = myToken.substring(7);
            try {
                String username = JwtUtil.validateToken(possibleToken);
                List<User> users = userService.getAllUsers();
                List<String> usernames = new ArrayList<>();
                for (User oneUser : users) {
                    usernames.add(oneUser.getUsername());
                }
                if (usernames.contains(username)) {
                    if (userService.findUSerByUsername(username).getRole().getRoleId() == 1) {
                        return ResponseEntity.ok().body(reimbursementService.updateStatus((long) id, "DENIED"));
                    }
                }
            } catch (RuntimeException e) {
                return ResponseEntity.status(403).body(new Reimbursement());
            }
        }
        return ResponseEntity.status(403).body(new Reimbursement());

    }

    @PutMapping("/acceptticket/{id}")
    private ResponseEntity<Reimbursement> denyTicket(@PathVariable int id,
            @RequestBody String update) {
        return ResponseEntity.ok().body(reimbursementService.updateStatus((long) id, "APPROVED"));

    }

    @PostMapping("/login")
    private ResponseEntity<User> loginIntoAccount(@RequestBody User user) {
        int statusCode = 404;
        String theToken = "";
        List<User> users = userService.getAllUsers();
        User targetUser = new User();
        if (authenticationService.isLoggedIn(user)) {
            statusCode = 200;
            for (User someUser : users) {
                if (user.getUsername().equals(someUser.getUsername())) {
                    targetUser = someUser;
                    theToken = JwtUtil.generateToken(targetUser.getUsername());
                    break;
                }

            }
        } else if (users.contains(user)) {
            statusCode = 403;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", theToken);
        return new ResponseEntity<>(targetUser, headers, statusCode);
    }

    /*
     * @DeleteMapping("/deleteuser")
     * private ResponseEntity<User> deleteUser(@Requestbody String userName) {
     * User theUser = userService.findUSerByUsername(userName);
     * userService.deleteUser(theUser);
     * return ResponseEntity.ok().body(theUser);
     * }
     */

}
