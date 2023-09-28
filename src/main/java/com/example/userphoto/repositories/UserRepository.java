package com.example.userphoto.repositories;

import com.example.userphoto.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   User findUserById (Long id);

   Optional <User> findUserByName (String name);
}
