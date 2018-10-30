package com.asitc.pgmicroservice.service.user.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.asitc.pgmicroservice.controller.error.exception.EntityNotFoundException;
import com.asitc.pgmicroservice.repository.user.UserRepository;
import com.asitc.pgmicroservice.repository.user.model.User;
import com.asitc.pgmicroservice.service.GenericService;
import com.asitc.pgmicroservice.service.user.UserService;

@Service
public class UserServiceImpl extends GenericService<User> implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User findUserById(final Long id) {
        final User user = this.userRepository.findOne(id);
        if (user == null) {
            throw new EntityNotFoundException("user", id.toString());
        }
        return user;
    }

    @Override
    public Collection<User> findUsersByPattern(final String firstName) {
        return this.userRepository.findByPattern(firstName);
    }

    @Override
    public Collection<User> findUsersByFirstName(final String firstName) {
        return this.userRepository.findByFirstName(firstName);
    }

    @Override
    public User createUser(final User user) {
        this.validateEntity(user);
        return this.userRepository.save(user);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User updateUser(final Long id, final User user) throws IllegalAccessException, InvocationTargetException {
        final User existingUser = this.findUserById(id);
        user.setId(id);
        this.validateEntity(user);
        BeanUtils.copyProperties(existingUser, user);
        return this.userRepository.save(existingUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(final Long id) {
        this.findUserById(id);
        this.userRepository.delete(id);
    }

}
