package com.delipick.user.domain.repository;

import com.delipick.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    Optional<User> findByEmailAndIsDeletedFalse(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    User save(User user);
}
