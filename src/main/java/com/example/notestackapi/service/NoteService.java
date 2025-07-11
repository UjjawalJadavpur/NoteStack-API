package com.example.notestackapi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.notestackapi.model.Note;
import com.example.notestackapi.model.NotePriority;
import com.example.notestackapi.model.User;
import com.example.notestackapi.repository.NoteRepository;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(User user, String title, String content, NotePriority priority) {
        if (user == null || title == null || content == null || priority == null) {
            throw new IllegalArgumentException("Missing required fields");
        }

        Note note = new Note();
        note.setUser(user);
        note.setTitle(title);
        note.setContent(content);
        note.setPriority(priority);
        note.setCreatedAt(LocalDateTime.now());

        return noteRepository.save(note);
    }

    public Note updateNote(Note note, String newTitle, String newContent, NotePriority newPriority) {
        note.setTitle(newTitle);
        note.setContent(newContent);
        note.setPriority(newPriority);
        return noteRepository.save(note);
    }

    // ✅ Main 3 filtering methods
    public List<Note> getAllNotes(User user) {
        return noteRepository.findByUser(user); // All notes
    }

    public List<Note> getActiveNotes(User user) {
        return noteRepository.findByUserAndArchivedFalse(user); // Only active
    }

    public List<Note> getArchivedNotes(User user) {
        return noteRepository.findByUserAndArchivedTrue(user); // Only archived
    }

    public List<Note> getNotesByPriority(User user, NotePriority priority) {
        return noteRepository.findByUserAndPriorityAndArchivedFalse(user, priority);
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }

    public Note findById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public void archiveNote(Long id) {
        Note note = findById(id);
        if (note != null) {
            note.setArchived(true);
            noteRepository.save(note);
        }
    }

    public void unarchiveNote(Long id) {
        Note note = findById(id);
        if (note != null) {
            note.setArchived(false);
            noteRepository.save(note);
        }
    }
}

