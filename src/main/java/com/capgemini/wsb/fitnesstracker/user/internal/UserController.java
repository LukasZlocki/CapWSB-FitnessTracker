package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.exception.api.NotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;


    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable("id") Long id) {
        var user = userService.getUser(id);
        if(user.isPresent()){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No user found with this id");
    }

    @GetMapping("/email")
    public ResponseEntity<Object> getUserByEmail(@RequestParam("email") String email) {
        var user = userService.getUserByEmail(email);
        if(user.isPresent()){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No user found with this email");
    }

    @GetMapping("/older/{date}")
    public ResponseEntity<Object> getUsersOlderThen(@PathVariable("date") LocalDate date) {
        var users = userService.getUsersOlderThen(date);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No users found with this date");
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/simple")
    public ResponseEntity<Object> getUsersSimple() {
        var users = userService.getAllUsersSimple();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No users found");
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        var users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No users found");
        }
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@RequestBody UserDto userDto) {

        if(userService.getUserByEmail(userDto.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User already exists");
        }

        System.out.println("User with e-mail: " + userDto.email() + " passed to the request");

        // saveUser with Service and return User or Exception
        try{
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userService.createUser(userDto));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {

        try {
            userService.deleteUser(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("User with id " + id + " successfully deleted");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {

        try {
            return ResponseEntity.ok(userService.updateUser(id, userDto));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
