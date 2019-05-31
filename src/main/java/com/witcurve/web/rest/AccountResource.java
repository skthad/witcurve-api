package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.repository.UserRepository;
import com.witcurve.service.MailService;
import com.witcurve.service.UserService;
import com.witcurve.service.dto.PasswordChangeDTO;
import com.witcurve.service.dto.UserDTO;
import com.witcurve.web.rest.errors.InvalidPasswordException;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.vm.ManagedUserVM;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(UserRepository userRepository, UserService userService, MailService mailService) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     * @throws RuntimeException 500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    @Timed
    public UserDTO getAccount() throws  WitcurveException {
        return userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new WitcurveException("User could not be found"));
    }

    /**
     * POST  /account/change-password : changes the current user's password
     *
     * @param passwordChangeDto current and new password
     * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect
     */
    @PostMapping(path = "/account/change-password")
    @Timed
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) throws WitcurveException{
        checkValidPassword(passwordChangeDto.getNewPassword());
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
   }

    /**
     * POST  /account/set-password : sets the current user's password
     *
     * @param passwordChangeDto new password
     * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect
     */
    @PostMapping(path = "/account/set-password")
    @Timed
    public void setPassword(@RequestBody PasswordChangeDTO passwordChangeDto) throws WitcurveException{
        checkValidPassword(passwordChangeDto.getNewPassword());
        userService.setPassword(passwordChangeDto.getNewPassword());
    }


    /**
     * POST  /account/request-reset-password : request mail to reset password
     *
     * @param passwordChangeDto username for password reset
     * @return email to which password reset url has been sent
     * @throws InvalidPasswordException 400 (Bad Request) if the user does not exist
     */
    @PostMapping(path = "/account/request-reset-password")
    @Timed
    public ResponseEntity requestPasswordMail(@RequestBody PasswordChangeDTO passwordChangeDto) throws WitcurveException{
        String email = userService.requestPasswordEmail(passwordChangeDto.getUsername());
        return ResponseEntity.ok(Collections.singletonMap("email", email));
    }

    /**
     * POST  /account/reset-password : resets the given user's password
     *
     * @param passwordChangeDto with username and new password
     * @throws InvalidPasswordException 400 (Bad Request) if the new password is incorrect or user does not exist
     */
    @PostMapping(path = "/account/reset-password")
    @Timed
    public void resetPassword(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) DateTime tokenDateTime,
                              @RequestBody PasswordChangeDTO passwordChangeDto) throws WitcurveException{
        if (tokenDateTime.plusMinutes(10).isBefore(DateTime.now())) {
            throw new WitcurveException("The reset password link has been expired!");
        }

        checkValidPassword(passwordChangeDto.getNewPassword());
        userService.resetPassword(passwordChangeDto.getUsername(), passwordChangeDto.getNewPassword(), false);
    }

    private static void checkValidPassword(String password) throws WitcurveException {
        if(!password.matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{"+
            ManagedUserVM.PASSWORD_MIN_LENGTH+","+ManagedUserVM.PASSWORD_MAX_LENGTH+"}$")) {
            throw new WitcurveException("Password must be at least "+ManagedUserVM.PASSWORD_MIN_LENGTH
                +" characters, no more than "+ManagedUserVM.PASSWORD_MAX_LENGTH+" characters," +
                " and must include at least one upper case letter, one lower case letter, and" +
                " one numeric digit and a special character (@, #, $, %, &. etc..)");
        }
    }
}
