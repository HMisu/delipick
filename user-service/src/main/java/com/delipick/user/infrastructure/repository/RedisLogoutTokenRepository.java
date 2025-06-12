package com.delipick.user.infrastructure.repository;

import com.delipick.user.domain.model.LogoutToken;
import com.delipick.user.domain.repository.LogoutTokenRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisLogoutTokenRepository extends CrudRepository<LogoutToken, String>, LogoutTokenRepository {

}