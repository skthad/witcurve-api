package com.witcurve.service.impl;

import com.google.common.base.Strings;
import com.witcurve.domain.Student;
import com.witcurve.domain.User;
import com.witcurve.domain.enumeration.UserType;
import com.witcurve.repository.StudentRepository;
import com.witcurve.service.*;
import com.witcurve.service.dto.StaffDTO;
import com.witcurve.service.dto.UserContextDTO;
import com.witcurve.service.mapper.UserMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserContextServiceImpl implements UserContextService {

    private final Logger log  = LoggerFactory.getLogger(StaffServiceImpl.class);

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StaffService staffService;

    @Autowired
    StudentStandardService studentStandardService;

    @Override
    public UserContextDTO getCurrentUserContext() throws WitcurveException {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.getUserWithAuthoritiesByLogin(user.getUsername()).get();

        UserContextDTO contextDTO = new UserContextDTO();
        contextDTO.setCurrentUser(userMapper.userToUserDTO(currentUser));
        if (UserType.STAFF.equals(contextDTO.getCurrentUser().getType())) {
            StaffDTO staffDTO = staffService.getStaffByUserId(currentUser.getId());
            if (Strings.isNullOrEmpty(currentUser.getPassword())) {
                staffDTO.setHasPassword(Boolean.FALSE);
            } else {
                staffDTO.setHasPassword(Boolean.TRUE);
            }
            contextDTO.getCurrentUser().setStaffDTO(staffDTO);
        } else if (UserType.PARENT.equals(contextDTO.getCurrentUser().getType())) {
            Student student = studentRepository.getStudentByUserId(currentUser.getId());
            contextDTO.setStudentStandardDTO(studentStandardService.getByStudentId(student.getId()));
            if (Strings.isNullOrEmpty(currentUser.getPassword())) {
                contextDTO.getStudentStandardDTO().getStudent().setHasPassword(Boolean.FALSE);
            } else {
                contextDTO.getStudentStandardDTO().getStudent().setHasPassword(Boolean.TRUE);
            }
        }
        return contextDTO;
    }
}
