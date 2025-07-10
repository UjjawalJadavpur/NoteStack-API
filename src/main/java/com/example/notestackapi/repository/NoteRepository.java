package com.example.notestackapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.notestackapi.model.Note;
import com.example.notestackapi.model.NotePriority;
import com.example.notestackapi.model.User;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
    List<Note> findByUserAndArchivedFalse(User user);  // Fetch non-archived notes
    List<Note> findByUserAndArchivedTrue(User user);   // Fetch archived notes
    List<Note> findByUserAndPriorityAndArchivedFalse(User user, NotePriority priority);
}
