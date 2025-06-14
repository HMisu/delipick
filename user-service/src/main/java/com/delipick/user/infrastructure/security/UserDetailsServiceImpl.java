package com.delipick.user.infrastructure.security;

import com.delipick.user.domain.model.User;
import com.delipick.user.domain.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("해당하는 사용자가 존재하지 않습니다."));

        if (user.isDeleted()) {
            throw new BadCredentialsException("탈퇴한 사용자입니다.");
        }

        return new UserDetailsImpl(user);
    }

}
