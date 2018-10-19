package com.witcurve.security;

import com.witcurve.domain.Authority;
import com.witcurve.domain.User;
import com.witcurve.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {


    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        Optional<User> result = userRepository.findOneWithAuthoritiesByLogin(username);

        if (!result.isPresent()) {
            throw new BadCredentialsException("Username not found.");
        }
        User user = result.get();
        if (!password.equals(user.getOtp())) {
            throw new BadCredentialsException("Wrong Otp.");
        } else {
            if(user.getOtpExpiry().isBefore(Instant.now())) {
                user.setOtp(null);
                user.setOtpExpiry(null);
                userRepository.save(user);
                throw new BadCredentialsException("Otp Expired.");
            }
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority = null;
        for(Authority authority : user.getAuthorities()) {
            simpleGrantedAuthority= new SimpleGrantedAuthority(authority.getName());
            authorities.add(simpleGrantedAuthority);
        }
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
            UsernamePasswordAuthenticationToken.class);
    }

}
