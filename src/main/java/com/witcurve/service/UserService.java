package com.witcurve.service;

import com.google.common.base.Strings;
import com.witcurve.config.ApplicationProperties;
import com.witcurve.config.Constants;
import com.witcurve.domain.Authority;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.AuthorityRepository;
import com.witcurve.repository.StaffRepository;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.UserRepository;
import com.witcurve.security.AuthoritiesConstants;
import com.witcurve.security.SecurityUtils;
import com.witcurve.service.dto.UserDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private ApplicationProperties applicationProperties;

    private final CacheManager cacheManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        if (UserType.SUPER_USER.equals(userDTO.getType())) {
            throw new WitcurveException("Invalid User Type");
        } else if (UserType.INSTITUTE_MANAGER.equals(userDTO.getType())) {
            userDTO.setAuthorities(new HashSet<>());
            userDTO.addAuthority(AuthoritiesConstants.INSTITUTE_ADMIN);
        } else if (UserType.PARENT.equals(userDTO.getType())) {
            userDTO.setAuthorities(new HashSet<>());
            userDTO.addAuthority(AuthoritiesConstants.PARENT);
        } else if (UserType.TEACHING_STAFF.equals(userDTO.getType())) {
            userDTO.setAuthorities(new HashSet<>());
            userDTO.addAuthority(AuthoritiesConstants.FACULTY);
        } else if (UserType.NON_TEACHING_STAFF.equals(userDTO.getType())) {
            userDTO.setAuthorities(new HashSet<>());
            userDTO.addAuthority(AuthoritiesConstants.NON_TEACHING);
        }
        user.setType(userDTO.getType());
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        if (!Strings.isNullOrEmpty(userDTO.getPassword())) {
            String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
            user.setPassword(encryptedPassword);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setActivated(userDTO.isActivated());
                if (UserType.SUPER_USER.equals(userDTO.getType())) {
                    throw new WitcurveException("Invalid User Type");
                } else if (UserType.INSTITUTE_MANAGER.equals(userDTO.getType())) {
                    userDTO.setAuthorities(new HashSet<>());
                    userDTO.addAuthority(AuthoritiesConstants.INSTITUTE_ADMIN);
                } else if (UserType.PARENT.equals(userDTO.getType())) {
                    userDTO.setAuthorities(new HashSet<>());
                    userDTO.addAuthority(AuthoritiesConstants.PARENT);
                } else if (UserType.TEACHING_STAFF.equals(userDTO.getType())) {
                    userDTO.setAuthorities(new HashSet<>());
                    userDTO.addAuthority(AuthoritiesConstants.FACULTY);
                } else if (UserType.NON_TEACHING_STAFF.equals(userDTO.getType())) {
                    userDTO.setAuthorities(new HashSet<>());
                    userDTO.addAuthority(AuthoritiesConstants.NON_TEACHING);
                }
                user.setType(userDTO.getType());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                if (userDTO.getAuthorities() != null) {
                    userDTO.getAuthorities().stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);
                }
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login.toLowerCase()).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) throws WitcurveException{
        Optional<String> userName = SecurityUtils.getCurrentUserLogin();
        if(userName.isPresent()) {
            Optional<User> user = userRepository.findOneByLogin(userName.get());
            if(user.isPresent()) {
                String currentEncryptedPassword = user.get().getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new WitcurveException("Invalid Password!");
//                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.get().setPassword(encryptedPassword);
                user.get().setActivated(Boolean.TRUE);
                user.get().setForcePassword(Boolean.FALSE);
                this.clearUserCaches(user.get());
                log.debug("Set password for User: {}", user.get());
            } else {
                throw new WitcurveException("There is no user with session user name in session!");
            }
        } else {
            throw new WitcurveException("Error getting user name from session!");
        }
    }

    public void setPassword(String newPassword) throws WitcurveException{
        Optional<String> userName = SecurityUtils.getCurrentUserLogin();
        if(userName.isPresent()) {
            resetPassword(userName.get(), newPassword);
        } else {
            throw new WitcurveException("Error getting user name from session!");
        }
    }

    public void resetPassword(String username, String newPassword) {
        if (StringUtils.isNotBlank(username)) {
            Optional<User> user = userRepository.findOneByLogin(username);
            if(user.isPresent()) {
                if (Strings.isNullOrEmpty(user.get().getPassword())) {
                    throw new WitcurveException("Empty password");
//                  throw new PasswordAlreadySetException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.get().setPassword(encryptedPassword);
                user.get().setActivated(Boolean.TRUE);
                user.get().setForcePassword(Boolean.FALSE);
                this.clearUserCaches(user.get());
                log.debug("Set password for User: {}", user.get());
            } else {
                throw new WitcurveException("Password cannot be reset as we can’t find any user to reset password");
            }
        } else {
            throw new WitcurveException("Invalid request, user name is required!");
        }
    }

    public void requestPasswordEmail(String username) {
        if (StringUtils.isNotBlank(username)) {
            Optional<User> user = userRepository.findOneByLogin(username);
            if(user.isPresent()) {
                String token = username + "|" + DateTime.now().toString();
                Map<String, Object> params = new HashMap();
                params.put("userName", user.get().getFirstName() + " " + user.get().getLastName());
                params.put("resetUrl", applicationProperties.getDomain().getUrl()
                    + Constants.RESET_URL + Base64.getEncoder().encodeToString(token.getBytes()));
                mailService.sendResetPasswordMail(user.get().getEmail(), params);
                log.debug("Reset password email sent for User: {}", user.get());
            } else {
                throw new WitcurveException("Password cannot be reset as we can’t find any user to reset password");
            }
        } else {
            throw new WitcurveException("Invalid request, user name is required!");
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login.toLowerCase());
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
    }

}
