package com.witcurve.security;

import com.witcurve.domain.Authority;
import com.witcurve.domain.Permission;
import com.witcurve.domain.User;
import com.witcurve.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

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

        Set<Authority> authorities = user.getAuthorities();
        Set<Permission> permissions = null;
        for (Authority authority : authorities) {
            if (authority.getPermissions().size() != 0) {
                if (permissions == null) {
                    permissions = new HashSet<>();
                }
                permissions.addAll(authority.getPermissions());
            }
        }
        List<GrantedAuthority> grantedAuthorities = permissions.stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toList());

        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return new UsernamePasswordAuthenticationToken(user.getLogin(), password, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
            UsernamePasswordAuthenticationToken.class);
    }

}
