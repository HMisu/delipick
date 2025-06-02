package com.delipick.user.domain.repository;

import com.delipick.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    User save(User user);
}
