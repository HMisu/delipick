package com.delipick.user.domain.repository;

import com.delipick.user.domain.model.LogoutToken;

public interface LogoutTokenRepository {
    LogoutToken save(LogoutToken logoutToken);

    boolean existsById(String accessToken);
}