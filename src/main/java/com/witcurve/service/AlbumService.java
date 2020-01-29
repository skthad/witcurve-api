package com.witcurve.service;

import com.witcurve.service.dto.AlbumDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface AlbumService {

    AlbumDTO createAlbum(String name, String description, Long schoolInfoId, List<MultipartFile> photos);

    AlbumDTO updateAlbum(Long albumId, String name, String description);

    AlbumDTO getAlbumById(Long albumId);

    List<AlbumDTO> getAlbumBySchoolInfoId(Long schoolInfoId);

    AlbumDTO addPhotosToAlbum(Long albumId, List<MultipartFile> photos);

    void deleteById(Long albumId);

    void deletePhotosFromAlbum(Long albumId, List<Long> ids);
}
