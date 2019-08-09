package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.witcurve.domain.Student;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.SubscriptionModel;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.UserRepository;
import com.witcurve.security.OtpAuthenticationProvider;
import com.witcurve.security.jwt.JWTConfigurer;
import com.witcurve.security.jwt.TokenProvider;
import com.witcurve.web.rest.vm.LoginVM;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final OtpAuthenticationProvider otpAuthenticationProvider;

    private final UserRepository userRepository;

    private final StudentRepository studentRepository;

    public UserJWTController(TokenProvider tokenProvider,
                             AuthenticationManager authenticationManager,
                             OtpAuthenticationProvider otpAuthenticationProvider,
                             UserRepository userRepository,
                             StudentRepository studentRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.otpAuthenticationProvider = otpAuthenticationProvider;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM, @RequestParam(value = "otp", required = false)Boolean otp) {

        Authentication authentication = null;
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        if(otp!= null && otp) {
            authentication = this.otpAuthenticationProvider.authenticate(authenticationToken);
        } else {
            authentication = this.authenticationManager.authenticate(authenticationToken);
        }

        String username = authentication.getName();
        Optional<User> result = userRepository.findOneWithAuthoritiesByLogin(username);
        if (!result.isPresent()) {
            throw new BadCredentialsException("Username not found.");
        }
        User user = result.get();
        if(!user.getFirstTimeLogin()) {
            user.setFirstTimeLogin(true);
            userRepository.save(user);
            Student student = studentRepository.getStudentByUserId(user.getId());
            if(student != null) {
                if(student.getSchoolInfo().getSchool().getInstitute().getSubscriptionModel().equals(SubscriptionModel.STUDENT)) {
                    student.setSubscriptionStartDate(LocalDate.now());
                    student.setSubscriptionEndDate(LocalDate.now().plusDays(5));
                    studentRepository.save(student);
                }
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
