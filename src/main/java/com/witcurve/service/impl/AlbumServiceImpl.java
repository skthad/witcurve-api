package com.witcurve.service.impl;

import com.witcurve.domain.Album;
import com.witcurve.domain.Attachment;
import com.witcurve.domain.enumeration.AttachmentType;
import com.witcurve.repository.AlbumRepository;
import com.witcurve.repository.SchoolInfoRepository;
import com.witcurve.service.AlbumService;
import com.witcurve.service.AttachmentService;
import com.witcurve.service.dto.AlbumDTO;
import com.witcurve.service.mapper.AlbumMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Transactional
public class AlbumServiceImpl implements AlbumService {

    private final Logger log = LoggerFactory.getLogger(AlbumServiceImpl.class);

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    AlbumMapper albumMapper;

    @Autowired
    SchoolInfoRepository schoolInfoRepository;

    @Override
    public AlbumDTO createAlbum(String name, String description, Long schoolInfoId, List<MultipartFile> photos) {
        log.debug("Request to create album");
        AlbumDTO albumDTO = new AlbumDTO();
        albumDTO.setName(name);
        albumDTO.setDescription(description);
        albumDTO.setSchoolInfoId(schoolInfoId);

        String directory = AttachmentType.ALBUM_PHOTO.toString() + File.separator + schoolInfoId + "_" + name;
        List<Attachment> attachments = new ArrayList<>();
        for (MultipartFile file : photos) {
            Attachment attachment = attachmentService.saveAttachmentWithMultipart(file, AttachmentType.ALBUM_PHOTO, directory);
            attachments.add(attachment);
        }
        albumDTO.setPhotos(attachments);
        Album album = albumRepository.save(albumMapper.toEntity(albumDTO));
        return albumMapper.toDto(album);
    }

    @Override
    public AlbumDTO updateAlbum(Long albumId, String name, String description) {
        log.debug("Request to update album");
        Optional<Album> album = albumRepository.findById(albumId);
        if (!album.isPresent()) {
            throw new WitcurveException("No album is present with given id {} " + albumId);
        }
        album.get().setName(name);
        album.get().setDescription(description);
        return albumMapper.toDto(album.get());
    }

    @Override
    public AlbumDTO getAlbumById(Long albumId) {
        log.debug("Request to get album with id : {} ", albumId);
        Optional<Album> album = albumRepository.findById(albumId);
        if (!album.isPresent()) {
            throw new WitcurveException("No album is present with given id {} " + albumId);
        }
        return albumMapper.toDto(album.get());
    }

    @Override
    public List<AlbumDTO> getAlbumBySchoolInfoId(Long schoolInfoId) {
        log.debug("Request to get album with school info id : {} ", schoolInfoId);
        List<Album> albums = albumRepository.getBySchoolInfoId(schoolInfoId);
        return albumMapper.toDto(albums);
    }

    @Override
    public AlbumDTO addPhotosToAlbum(Long albumId, List<MultipartFile> photos) {
        log.debug("Request to add photos to album with id : {} ", albumId);
        Optional<Album> album = albumRepository.findById(albumId);
        if (!album.isPresent()) {
            throw new WitcurveException("No album is present with given id {} " + albumId);
        }
        List<Attachment> attachments = new ArrayList<>();
        String directory = AttachmentType.ALBUM_PHOTO.toString() + File.separator + album.get().getSchoolInfo().getId() + "_" + album.get().getName();
        for (MultipartFile file : photos) {
            Attachment attachment = attachmentService.saveAttachmentWithMultipart(file, AttachmentType.ALBUM_PHOTO, directory);
            attachments.add(attachment);
        }
        album.get().addAttachment(attachments);
        return albumMapper.toDto(album.get());
    }

    @Override
    public void deleteById(Long albumId) {
        log.debug("Request to delete album with id : {} ", albumId);
        Optional<Album> album = albumRepository.findById(albumId);
        if (!album.isPresent()) {
            throw new WitcurveException("No album present with id : {} " + albumId);
        }
        for (Attachment attachment : album.get().getPhotos()) {
            attachmentService.delete(attachment.getId());
        }
        albumRepository.deleteById(albumId);
    }

    @Override
    public void deletePhotosFromAlbum(Long albumId, List<Long> ids) {
        log.debug("Request to delete photos from album with id : {} ", albumId);
        Optional<Album> album = albumRepository.findById(albumId);
        if (!album.isPresent()) {
            throw new WitcurveException("No album present with id : {} " + albumId);
        }
        Map<Long, Attachment> attachmentPresentInAlbum = album.get().getPhotos().stream().collect(
            Collectors.toMap(Attachment::getId, Function.identity()));
        for (Long id : ids) {
            if (attachmentPresentInAlbum.keySet().contains(id)) {
                album.get().getPhotos().remove(attachmentPresentInAlbum.get(id));
                attachmentService.delete(id);
            }
        }
    }
}

