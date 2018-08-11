package com.witcurve.service.mapper;

import com.witcurve.domain.Note;
import com.witcurve.service.dto.NoteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    @Mapping(source = "courseTeacherId", target = "courseTeacher.id")
    @Mapping(source = "studentId", target = "student.id")
    Note noteDTOToNote(NoteDTO noteDTO);

    @Mapping(target = "courseTeacherId", source = "courseTeacher.id")
    @Mapping(target = "studentId", source = "student.id")
    NoteDTO noteToNoteDTO(Note note);

    List<Note> noteDTOsToNotes(List<NoteDTO> noteDTOS);

    List<NoteDTO> notesToNoteDTOs(List<Note> notes);


}
