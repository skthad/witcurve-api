package com.witcurve.service;

import com.google.common.base.Strings;
import com.witcurve.config.Constants;
import com.witcurve.domain.Authority;
import com.witcurve.domain.Staff;
import com.witcurve.domain.Student;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.AuthorityRepository;
import com.witcurve.repository.StaffRepository;
import com.witcurve.repository.StudentRepository;
import com.witcurve.repository.UserRepository;
import com.witcurve.security.SecurityUtils;
import com.witcurve.service.dto.UserDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private final CacheManager cacheManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
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
        user.setActivated(true);
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
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
                user.setLogin(userDTO.getLogin());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
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
            Optional<User> user = userRepository.findOneByLogin(userName.get());
            if(user.isPresent()) {
                if (!Strings.isNullOrEmpty(user.get().getPassword())) {
                    throw new WitcurveException("Empty password");
//                  throw new PasswordAlreadySetException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.get().setPassword(encryptedPassword);
                this.clearUserCaches(user.get());
                log.debug("Set password for User: {}", user.get());
            } else {
                throw new WitcurveException("There is no user with session user name in session!");
            }
        } else {
            throw new WitcurveException("Error getting user name from session!");
        }

    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
            this.clearUserCaches(user);
        }
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }

    public List<String> getContactNumbersOfUser(String username, UserType type) throws WitcurveException{
        List<String> result = null;
        Optional<User> user = userRepository.findOneByLogin(username);
        if (user.isPresent()) {
            if (type.equals(user.get().getType())) {
                if(type.equals(UserType.STAFF)) {
                    Staff staff = staffRepository.getStaffByUserId(user.get().getId());
                    if (staff != null) {
                        result = new ArrayList<>();
                        result.add(staff.getPrimaryPhone());
                        if (staff.getSecondaryPhone() != null) {
                            result.add(staff.getSecondaryPhone());
                        }
                    } else {
                        throw new WitcurveException("There is not staff related to given user");
                    }

                } else if(type.equals(UserType.PARENT)) {
                    Student student = studentRepository.getStudentByUserId(user.get().getId());
                    if (student != null) {
                        result= new ArrayList<>();
                        result.add(student.getRegisteredMobileNumber());
                        if (student.getAlternateMobileNumbers() != null && student.getAlternateMobileNumbers().size() > 0) {
                            result.addAll(student.getAlternateMobileNumbers());
                        }
                    } else {
                        throw new WitcurveException("There is not student related to given user");
                    }
                }
            } else {
               throw new WitcurveException("No user with given login and type exists");
            }
        }
        return result;
    }
}
