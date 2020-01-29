package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.AlbumService;
import com.witcurve.service.dto.AlbumDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AlbumResource {

    private final Logger log = LoggerFactory.getLogger(AlbumResource.class);

    @Autowired
    AlbumService albumService;


    /**
     * creates an album
     *
     * @param name
     * @param description
     * @param schoolInfoId
     * @return albumDTO
     */
    @PostMapping("/album/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<AlbumDTO> createAlbum(@RequestParam String name, @RequestParam(required = false) String description, @PathVariable Long schoolInfoId, @RequestParam List<MultipartFile> file) throws WitcurveException {
        log.debug("Request to create album");
        AlbumDTO result = albumService.createAlbum(name, description, schoolInfoId, file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * updates an album
     *
     * @param name
     * @param description
     * @return albumDTO
     */
    @PutMapping("/album/{albumId}")
    @Timed
    public ResponseEntity<AlbumDTO> updateAlbum(@PathVariable Long albumId, @RequestParam String name, @RequestParam(required = false) String description) throws WitcurveException {
        log.debug("Request to update album");
        AlbumDTO result = albumService.updateAlbum(albumId, name, description);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * add photos to an album
     *
     * @param albumId
     * @param file
     * @return albumDTO
     */
    @PatchMapping("/album/add/add-photos/{albumId}")
    @Timed
    public ResponseEntity<AlbumDTO> addPhotosToAlbum(@PathVariable Long albumId, @RequestParam List<MultipartFile> file) {
        log.debug("Request to add photos to album");
        AlbumDTO result = albumService.addPhotosToAlbum(albumId, file);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get an album by id
     *
     * @param albumId
     * @return albumDTO
     */
    @GetMapping("/album/{albumId}")
    @Timed
    public ResponseEntity<AlbumDTO> getAlbumById(@PathVariable Long albumId) {
        log.debug("Request to get album by id ", albumId);
        AlbumDTO result = albumService.getAlbumById(albumId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * get an album by schoolInfoId
     *
     * @param schoolInfoId
     * @return albumDTOs
     */
    @GetMapping("/album/school-info/{schoolInfoId}")
    @Timed
    public ResponseEntity<List<AlbumDTO>> getAlbumBySchoolInfoId(@PathVariable Long schoolInfoId) {
        log.debug("Request to get album with schoolInfo id  ", schoolInfoId);
        List<AlbumDTO> result = albumService.getAlbumBySchoolInfoId(schoolInfoId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * delete an album by id
     *
     * @param albumId
     */
    @DeleteMapping("/album/delete/{albumId}")
    @Timed
    public void deleteAlbumById(@PathVariable Long albumId) {
        log.debug("Request to delete album with id  ", albumId);
        albumService.deleteById(albumId);
    }

    /**
     * deletes photos from album
     *
     * @param albumId
     * @param photoIds
     */
    @DeleteMapping("/album/delete/photos/{albumId}")
    @Timed
    public void deletePhotosFromAlbum(@PathVariable Long albumId, @RequestParam List<Long> photoIds) {
        log.debug("Request to photos from album with album id  ", albumId);
        albumService.deletePhotosFromAlbum(albumId, photoIds);
    }
}
