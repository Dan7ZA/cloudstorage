package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

/*
    @PostConstruct
    private void postConstruct(){
        Note initialNote1 = new Note("User 1 Note 1","Description",1);
        noteMapper.insertNote(initialNote1);
        Note initialNote2 = new Note("User 1 Note 2","Description",1);
        noteMapper.insertNote(initialNote2);
        Note initialNote3 = new Note("User 2 Note 1","Description",2);
        noteMapper.insertNote(initialNote3);
        Note initialNote4 = new Note("User 2 Note 2","Description",2);
        noteMapper.insertNote(initialNote4);
    }
*/

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void saveNote(NoteForm noteForm) {

        Note newNote = new Note();
        newNote.setNoteTitle(noteForm.getNoteTitle());
        newNote.setNoteDescription(noteForm.getNoteDescription());
        newNote.setUserId(noteForm.getUserId());

        if(noteForm.getNoteId()==null){
            noteMapper.insertNote(newNote);
        } else {
            newNote.setNoteId(noteForm.getNoteId());
            noteMapper.updateNote(newNote);
        }
    }

    public List<Note> getNotes(Integer userId){
        return noteMapper.getAllNotes(userId);
    };

    public void deleteNote(Integer noteId) {
        noteMapper.deleteNote(noteId);
    }

}

