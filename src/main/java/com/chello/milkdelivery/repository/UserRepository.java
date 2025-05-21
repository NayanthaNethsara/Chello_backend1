package com.chello.milkdelivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chello.milkdelivery.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByIdAndUsernameAndRole(Long id, String username, User.Role role);

    List<User> findByRole(User.Role role);


}