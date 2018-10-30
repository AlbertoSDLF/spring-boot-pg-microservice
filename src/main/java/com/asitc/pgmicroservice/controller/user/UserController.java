package com.asitc.pgmicroservice.controller.user;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asitc.pgmicroservice.repository.user.model.User;
import com.asitc.pgmicroservice.service.user.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ma.glasnost.orika.MapperFacade;

@RestController
@RequestMapping(value = "/api/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Api("User API")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MapperFacade mapper;

    @GetMapping("/{id}")
    @ApiOperation(value = "Single user retrieval", notes = "Obtain the user correspondent to the identifier passed as parameter", protocols = "https", httpMethod = "GET", code = 200, response = UserDTO.class, produces = "application/json")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") final Long id) {
        final User user = this.userService.findUserById(id);
        return new ResponseEntity<>(this.mapper.map(user, UserDTO.class), HttpStatus.OK);
    }

    @GetMapping("/search/findByPattern")
    public ResponseEntity<Collection<UserDTO>> findUsersByPattern(
            @RequestParam(value = "firstName", required = true) final String firstName) {
        final Collection<User> users = this.userService.findUsersByPattern(firstName);
        return new ResponseEntity<>(this.mapper.mapAsList(users, UserDTO.class), HttpStatus.OK);
    }

    @GetMapping("/search/findByFirstName")
    public ResponseEntity<Collection<UserDTO>> findUsersByFirstName(
            @RequestParam(value = "firstName", required = true) final String firstName) {
        final Collection<User> users = this.userService.findUsersByFirstName(firstName);
        return new ResponseEntity<>(this.mapper.mapAsList(users, UserDTO.class), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "User creation", notes = "Create a new user, whose details are passed in the request body", protocols = "https", httpMethod = "POST", code = 201, response = UserDTO.class, consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> createUser(@RequestBody final UserDTO requestUser) {
        final User user = this.mapper.map(requestUser, User.class);
        final User createdUser = this.userService.createUser(user);
        return new ResponseEntity<>(this.mapper.map(createdUser, UserDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "User update", notes = "Fully update the details of an existing user", protocols = "https", httpMethod = "PUT", code = 204, consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> updateUser(@PathVariable("id") final Long id, @RequestBody final UserDTO requestUser)
            throws IllegalAccessException, InvocationTargetException {
        final User user = this.mapper.map(requestUser, User.class);
        this.userService.updateUser(id, user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "User deletion", notes = "Delete an existing user", protocols = "https", httpMethod = "DELETE", code = 204, consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("id") final Long id) {
        this.userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
