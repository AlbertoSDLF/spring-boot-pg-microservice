package com.asitc.pgmicroservice.service.user;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import com.asitc.pgmicroservice.repository.user.model.User;

public interface UserService {

    public User findUserById(Long id);

    public Collection<User> findUsersByPattern(String firstName);

    public Collection<User> findUsersByFirstName(String firstName);

    public User createUser(User user);

    public User updateUser(Long id, User user) throws IllegalAccessException, InvocationTargetException;

    public void deleteUser(Long id);

}
