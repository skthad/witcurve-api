package com.witcurve.service;

import com.witcurve.service.dto.NoteDTO;
import com.witcurve.web.rest.errors.WitcurveException;

public interface NoteService {

    NoteDTO saveOrUpdate(NoteDTO noteDTO);

    NoteDTO getNoteById(Long noteId) throws WitcurveException;

    void deleteNote(Long noteId) throws WitcurveException;
}
