package ua.kusakabe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.kusakabe.dto.UserRR;
import ua.kusakabe.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public String test(){
        return "Test request to USER-SERVICE";
    }

    @GetMapping("/get-all")
    public ResponseEntity<UserRR> getAll(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRR> getById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

}
