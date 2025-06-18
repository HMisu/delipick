package com.delipick.user.domain.repository;

import com.delipick.user.domain.enums.EmailVerificationPurposeEnum;
import com.delipick.user.domain.model.EmailAuth;

import java.util.Optional;

public interface EmailAuthRepository {
    EmailAuth save(EmailAuth emailAuth);
    
    Optional<EmailAuth> findByEmailAndPurpose(String email, EmailVerificationPurposeEnum purpose);

    void deleteByEmailAndPurpose(String email, EmailVerificationPurposeEnum purpose);
}