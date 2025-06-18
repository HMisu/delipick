package com.delipick.user.infrastructure.repository;

import com.delipick.user.domain.model.EmailAuth;
import com.delipick.user.domain.repository.EmailAuthRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisEmailAuthRepository extends CrudRepository<EmailAuth, String>, EmailAuthRepository {

}