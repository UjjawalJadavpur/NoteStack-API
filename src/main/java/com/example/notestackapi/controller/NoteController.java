package com.example.notestackapi.controller;

import com.example.notestackapi.dto.NoteDTO;
import com.example.notestackapi.model.Note;
import com.example.notestackapi.model.User;
import com.example.notestackapi.service.NoteService;
import com.example.notestackapi.service.UserService;
import com.example.notestackapi.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired private NoteService noteService;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private UserService userService;

    @GetMapping
    public ResponseEntity<List<Note>> getUserNotes(@RequestHeader("Authorization") String token) {
        String email = jwtProvider.getEmailFromToken(token.substring(7));
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(noteService.getNotes(user));
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestHeader("Authorization") String token,
                                           @RequestBody NoteDTO noteDTO) {
        String email = jwtProvider.getEmailFromToken(token.substring(7));
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(noteService.createNote(user, noteDTO.getTitle(), noteDTO.getContent()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        noteService.deleteNoteById(id);
        return ResponseEntity.noContent().build();
    }
}
