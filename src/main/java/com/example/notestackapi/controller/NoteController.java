package com.example.notestackapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.notestackapi.dto.NoteDTO;
import com.example.notestackapi.model.Note;
import com.example.notestackapi.model.NotePriority;
import com.example.notestackapi.model.User;
import com.example.notestackapi.security.JwtProvider;
import com.example.notestackapi.service.NoteService;
import com.example.notestackapi.service.UserService;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private static final String TOKEN_PREFIX = "Bearer ";

    @Autowired
    private NoteService noteService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;

    private User extractUserFromToken(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            throw new IllegalArgumentException("Invalid Authorization token");
        }

        String actualToken = token.substring(TOKEN_PREFIX.length());
        String email = jwtProvider.getEmailFromToken(actualToken);

        System.out.println("👉 Extracted email from token: " + email);

        User user = userService.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found for email: " + email);
        }

        return user;
    }

    @GetMapping
    public ResponseEntity<List<Note>> getUserNotes(@RequestHeader("Authorization") String token) {
        User user = extractUserFromToken(token);
        return ResponseEntity.ok(noteService.getNotes(user));
    }

    @GetMapping("/archived")
    public ResponseEntity<List<Note>> getArchivedNotes(@RequestHeader("Authorization") String token) {
        User user = extractUserFromToken(token);
        return ResponseEntity.ok(noteService.getArchivedNotes(user));
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestHeader("Authorization") String token,
            @RequestBody NoteDTO noteDTO) {
        User user = extractUserFromToken(token);
        Note createdNote = noteService.createNote(
                user,
                noteDTO.getTitle(),
                noteDTO.getContent(),
                noteDTO.getPriority());
        return ResponseEntity.ok(createdNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody NoteDTO noteDTO) {
        User user = extractUserFromToken(token);
        Note note = noteService.findById(id);

        if (note == null || !note.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        Note updatedNote = noteService.updateNote(
                note,
                noteDTO.getTitle(),
                noteDTO.getContent(),
                noteDTO.getPriority());
        return ResponseEntity.ok(updatedNote);
    }

    @GetMapping("/priority/{level}")
    public ResponseEntity<List<Note>> getNotesByPriority(@RequestHeader("Authorization") String token,
            @PathVariable("level") String level) {
        User user = extractUserFromToken(token);
        try {
            NotePriority priority = NotePriority.valueOf(level.toUpperCase());
            return ResponseEntity.ok(noteService.getNotesByPriority(user, priority));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // Invalid priority
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        User user = extractUserFromToken(token);
        Note note = noteService.findById(id);

        if (note == null || !note.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        noteService.deleteNoteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<Void> archiveNote(@RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        User user = extractUserFromToken(token);
        Note note = noteService.findById(id);

        if (note == null || !note.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        noteService.archiveNote(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/unarchive")
    public ResponseEntity<Void> unarchiveNote(@RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        User user = extractUserFromToken(token);
        Note note = noteService.findById(id);

        if (note == null || !note.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        noteService.unarchiveNote(id);
        return ResponseEntity.ok().build();
    }
}
