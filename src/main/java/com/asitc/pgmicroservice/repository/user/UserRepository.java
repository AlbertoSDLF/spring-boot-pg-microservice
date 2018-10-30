package com.asitc.pgmicroservice.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.asitc.pgmicroservice.repository.user.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    @Query("select u from User u where lower(u.firstName) like lower(concat('%', :firstName,'%'))")
    List<User> findByPattern(@Param("firstName") String firstName);

    List<User> findByFirstName(String firstName);

}