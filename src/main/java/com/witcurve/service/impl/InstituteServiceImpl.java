package com.witcurve.service.impl;

import com.witcurve.domain.*;
import com.witcurve.domain.enumeration.AttachmentType;
import com.witcurve.domain.enumeration.SubscriptionModel;
import com.witcurve.repository.*;
import com.witcurve.service.AttachmentService;
import com.witcurve.service.InstituteService;
import com.witcurve.service.dto.AuthorityDTO;
import com.witcurve.service.dto.InstituteDTO;
import com.witcurve.service.dto.SchoolDTO;
import com.witcurve.service.mapper.InstituteMapper;
import com.witcurve.service.mapper.SchoolInfoMapperLite;
import com.witcurve.service.mapper.SchoolMapperLite;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
@Transactional
public class InstituteServiceImpl implements InstituteService {

    private final Logger log = LoggerFactory.getLogger(InstituteServiceImpl.class);

    private static final List<String> ALLOWED_LOGO_MEDIA_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg");

    @Autowired
    InstituteRepository instituteRepository;

    @Autowired
    InstituteMapper instituteMapper;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    SchoolInfoMapperLite schoolInfoMapperLite;

    @Autowired
    SchoolMapperLite schoolMapperLite;

    @Autowired
    AttachmentService attachmentService;

    @Override
    public InstituteDTO saveOrUpdate(InstituteDTO instituteDTO) {
        log.debug("Request to save or update institute");

        if (instituteDTO.getSubscriptionModel() == SubscriptionModel.INSTITUTE) {
            if (instituteDTO.getSubscriptionStartDate() != null && instituteDTO.getSubscriptionEndDate() != null) {
                if (instituteDTO.getId() != null) {
                    List<Student> students = studentRepository.getStudentsByInstituteId(instituteDTO.getId());
                    if (!CollectionUtils.isEmpty(students)) {
                        students.forEach(student -> {
                            student.setSubscriptionStartDate(instituteDTO.getSubscriptionStartDate());
                            student.setSubscriptionEndDate(instituteDTO.getSubscriptionEndDate());
                        });
                    }
                }
            } else {
                throw new WitcurveException("For Institute Subscription Model start and end date for subscription are required");
            }
        } else if (instituteDTO.getSubscriptionModel() == SubscriptionModel.STUDENT) {
            if (instituteDTO.getPricing() == null) {
                throw new WitcurveException("For Student Subscription Model pricing is required");
            }
            instituteDTO.setSubscriptionStartDate(null);
            instituteDTO.setSubscriptionEndDate(null);
        }

        Institute institute = instituteMapper.toEntity(instituteDTO);
        institute = instituteRepository.save(institute);
        return instituteMapper.toDto(institute);
    }

    @Override
    public InstituteDTO getInstituteById(Long instituteId) throws WitcurveException {
        log.debug("Request to get institute with id: {}", instituteId);
        Optional<Institute> institute = instituteRepository.findById(instituteId);
        if (!institute.isPresent()) {
            throw new WitcurveException("No Institute with given id " + instituteId);
        }
        return instituteMapper.toDto(institute.get());
    }

    @Override
    public InstituteDTO addAttachment(Long instituteId, MultipartFile file) {
        log.debug("Request to add attchment to institute with id: {} with attachment file : {}", instituteId, file);
        Optional<Institute> institute = instituteRepository.findById(instituteId);
        if (!institute.isPresent()) {
            throw new WitcurveException("No Institute with given id " + instituteId);
        }
        if(institute.get().getInstituteLogo() != null) {
            attachmentService.delete(institute.get().getInstituteLogo().getId());
        }
        if(!ALLOWED_LOGO_MEDIA_TYPES.contains(file.getContentType())) {
            throw new WitcurveException("File extensions allowed for logo are png, jpeg, jpg only");
        }
        String directoryName = AttachmentType.INSTITUTE_LOGO.toString()+ File.separator+instituteId;
        Attachment attachment = attachmentService.saveAttachmentWithMultipart(file, AttachmentType.INSTITUTE_LOGO, directoryName);
        institute.get().setInstituteLogo(attachment);
        return instituteMapper.toDto(institute.get());
    }

    @Override
    public List<InstituteDTO> getAllInstitutes() {
        log.debug("Request to get all institutes");
        List<Institute> institutes = instituteRepository.findAll();
        return instituteMapper.toDto(institutes);
    }

    @Override
    public void deleteInstitute(Long instituteId) throws WitcurveException {
        log.debug("Request to delete institute with id {}", instituteId);
        Optional<Institute> institute = instituteRepository.findById(instituteId);
        if (!institute.isPresent()) {
            throw new WitcurveException("No Institute with given id " + instituteId);
        }
        instituteRepository.delete(institute.get());
    }

    @Override
    public Authority saveOrUpdateCustomAuthority(Long instituteId, AuthorityDTO authorityDTO) {
        // check if current logged in user belongs to the input institute
        // create custom dto later used to show default vs custom authorities separately
        Authority authority = new Authority();
        authority.setDisplayName(authorityDTO.getDisplayName());
        authority.setInstituteId(instituteId);
        Set<Permission> permissions = new HashSet<>();
        for(String permssion : authorityDTO.getPermissions()) {
            permissions.add(new Permission(permssion));
        }
        authority.setPermissions(permissions);
        if (authority.getName() == null) {
            authority.setName(instituteId + "-ROLE_" + authority.getDisplayName().trim().toUpperCase().replaceAll("[ ]+", "_"));
        }

        return authorityRepository.save(authority);
    }

    @Override
    public void deleteCustomAuthority(Long instituteId, String name) {
        if (name.startsWith(instituteId + "-")) {
            authorityRepository.deleteByName(name);
        }
    }

    @Override
    public List<Authority> getAuthorities(Long instituteId) {
        return authorityRepository.getByInstituteId(instituteId);
    }

    @Override
    public InstituteDTO getInstituteMapBySubDomainName(String subDomainName) throws WitcurveException {
        Institute institute = instituteRepository.findInstituteBySubDomainName(subDomainName);
        if(institute == null) {
            throw new WitcurveException("There is no institute registered with sub domain "+ subDomainName);
        }
        Map<Long, SchoolDTO> schoolDTOMap = new LinkedHashMap<>();
        InstituteDTO instituteDTO = instituteMapper.toDto(institute);
        List<School> schools = schoolRepository.findByInstituteId(institute.getId());
        for(School school : schools) {
            SchoolDTO schoolDTO = schoolMapperLite.toDto(school);
            List<SchoolInfo> schoolInfos = schoolInfoRepository.findBySchoolId(school.getId());
            for(SchoolInfo schoolInfo : schoolInfos) {
                schoolDTO.addSchoolInfo(schoolInfoMapperLite.toDto(schoolInfo));
            }
            schoolDTOMap.put(school.getId(), schoolDTO);
        }
        instituteDTO.setSchoolMap(schoolDTOMap);
        return instituteDTO;
    }

}
