package com.example.notestackapi.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.notestackapi.model.Note;
import com.example.notestackapi.model.User;
import com.example.notestackapi.repository.NoteRepository;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note createNote(User user, String title, String content) {
        if (user == null) {
            System.out.println("❌ User is null in createNote()");
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (title == null || content == null) {
            System.out.println("❌ Title or content is null in createNote()");
            throw new IllegalArgumentException("Title and content cannot be null");
        }

        System.out.println("✅ Creating note for user: " + user.getEmail());
        System.out.println("➡️ Title: " + title);
        System.out.println("➡️ Content: " + content);

        Note note = new Note();
        note.setUser(user);
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedAt(LocalDateTime.now());

        return noteRepository.save(note);
    }

    public List<Note> getNotes(User user) {
        return noteRepository.findByUserAndArchivedFalse(user); // Fetch only non-archived notes
    }

    public List<Note> getArchivedNotes(User user) {
        return noteRepository.findByUserAndArchivedTrue(user); // Fetch only archived notes
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }

    public Note findById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public Note updateNote(Note note, String newTitle, String newContent) {
        note.setTitle(newTitle);
        note.setContent(newContent);
        return noteRepository.save(note);
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
