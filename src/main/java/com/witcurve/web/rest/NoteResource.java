package com.witcurve.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.witcurve.service.NoteService;
import com.witcurve.service.dto.NoteDTO;
import com.witcurve.web.rest.errors.WitcurveException;
import com.witcurve.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class NoteResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    @Autowired
    NoteService noteService;

    /**
     * creates a note
     * @param noteDTO
     * @return
     * @throws WitcurveException
     * @throws URISyntaxException
     */
    @PostMapping("/note")
    @Timed
    public ResponseEntity<NoteDTO> createNote(@RequestBody @Valid NoteDTO noteDTO) throws WitcurveException, URISyntaxException {
        log.debug("Request Save Note");
        if (noteDTO.getId() != null) {
            throw new WitcurveException("New Note can't already have an id");
        }
        NoteDTO result = noteService.saveOrUpdate(noteDTO);
        return ResponseEntity.created(new URI("/api/note/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("note", result.getId().toString()))
            .body(result);
    }

    /**
     * get note by id
     * @param noteId
     * @return
     * @throws WitcurveException
     */

    @GetMapping("/note/{noteId}")
    @Timed
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable("noteId") Long noteId) throws WitcurveException {
        log.debug("Request to get Note with id {}", noteId);
        NoteDTO result = noteService.getNoteById(noteId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * update the given note
     * @param noteDTO
     * @return
     * @throws WitcurveException
     */

    @PutMapping("/note")
    @Timed
    public ResponseEntity<NoteDTO> updateNote(@RequestBody @Valid NoteDTO noteDTO) throws WitcurveException {
        log.debug("Request to update note");
        if (noteDTO.getId() == null) {
            throw new WitcurveException("Id is required for update request");
        }
        NoteDTO result = noteService.saveOrUpdate(noteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("note", noteDTO.getId().toString()))
            .body(result);
    }

    /**
     * delete the note
     * @param noteId
     * @return
     * @throws WitcurveException
     */
    @DeleteMapping("/note/{noteId}")
    @Timed
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) throws WitcurveException {
        log.debug("REST request to delete Note: {}", noteId);
        noteService.deleteNote(noteId);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A note is deleted with identifier " + noteId,
            noteId.toString())).build();
    }
}
