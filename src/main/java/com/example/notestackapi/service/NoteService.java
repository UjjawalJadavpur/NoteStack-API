package com.example.notestackapi.service;

import com.example.notestackapi.model.Note;
import com.example.notestackapi.model.User;
import com.example.notestackapi.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(User user, String title, String content) {
        Note note = new Note();
        note.setUser(user);
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedAt(LocalDateTime.now());
        return noteRepository.save(note);
    }

    public List<Note> getNotes(User user) {
        return noteRepository.findByUser(user);
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }
}
