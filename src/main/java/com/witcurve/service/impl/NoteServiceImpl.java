package com.witcurve.service.impl;

import com.witcurve.domain.Note;
import com.witcurve.repository.NoteRepository;
import com.witcurve.service.NoteService;
import com.witcurve.service.dto.NoteDTO;
import com.witcurve.service.mapper.NoteMapper;
import com.witcurve.web.rest.errors.WitcurveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService {

    private final Logger log  = LoggerFactory.getLogger(NoteServiceImpl.class);

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    NoteMapper noteMapper;

    @Override
    public NoteDTO saveOrUpdate(NoteDTO noteDTO) {
        log.debug("Request to save or update note : {}", noteDTO);
        Note note = noteMapper.noteDTOToNote(noteDTO);
        note = noteRepository.save(note);
        return noteMapper.noteToNoteDTO(note);
    }

    @Override
    public NoteDTO getNoteById(Long noteId) throws WitcurveException {
        log.debug("Request to get note with id : {}", noteId);
        Note note = noteRepository.findById(noteId).get();
        if (note == null) {
            throw new WitcurveException("No note with given id");
        }
        return noteMapper.noteToNoteDTO(note);
    }

    @Override
    public void deleteNote(Long noteId) throws WitcurveException {
        log.debug("Request to get delete with id : {}", noteId);
        Note note = noteRepository.findById(noteId).get();
        if (note ==null){
            throw new WitcurveException("No note with given id");
        }
        noteRepository.delete(note);
    }
}
