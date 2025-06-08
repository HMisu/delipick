package com.delipick.user.infrastructure.repository;

import com.delipick.user.domain.model.User;
import com.delipick.user.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

}