package com.delipick.user.application.service;

import com.delipick.user.domain.enums.EmailVerificationPurposeEnum;
import com.delipick.user.domain.model.EmailAuth;
import com.delipick.user.domain.repository.EmailAuthRepository;
import com.delipick.user.presentation.exception.CustomException;
import com.delipick.user.presentation.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender mailSender;

    public void sendCode(String email, EmailVerificationPurposeEnum purpose) {
        String code = createRandomCode();
        EmailAuth emailAuth = new EmailAuth(code, email, purpose);

        emailAuthRepository.deleteByEmailAndPurpose(email, purpose);
        emailAuthRepository.save(emailAuth);

        String subject = getSubjectByPurpose(purpose);
        String text = getTextByPurpose(purpose, code);

        sendEmail(email, subject, text);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILURE);
        }
    }

    private String getSubjectByPurpose(EmailVerificationPurposeEnum purpose) {
        return switch (purpose) {
            case SIGN_UP -> "회원가입 인증 메일입니다.";
            case RESET_PASSWORD -> "비밀번호 재설정 인증 메일입니다.";
            default -> "인증 메일입니다.";
        };
    }

    private String getTextByPurpose(EmailVerificationPurposeEnum purpose, String code) {
        return switch (purpose) {
            case SIGN_UP -> "회원가입 인증 코드: " + code + " 입니다.";
            case RESET_PASSWORD -> "비밀번호 재설정 인증 코드: " + code + " 입니다.";
            default -> "인증 코드: " + code + " 입니다.";
        };
    }

    public boolean verifyCode(String email, String code, EmailVerificationPurposeEnum purpose) {
        return emailAuthRepository.findByEmailAndPurpose(email, purpose)
                .map(emailAuth -> code.equals(emailAuth.getVerifyCode()))
                .orElse(false);
    }

    private String createRandomCode() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }
}