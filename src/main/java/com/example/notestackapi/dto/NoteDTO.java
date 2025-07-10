package com.example.notestackapi.dto;

import com.example.notestackapi.model.NotePriority;

public class NoteDTO {
    private String title;
    private String content;

    public NoteDTO() {
    }

    private NotePriority priority = NotePriority.MEDIUM; // Default

    public NoteDTO(String title, String content, NotePriority priority) {
        this.title = title;
        this.content = content;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotePriority getPriority() {
        return priority;
    }

    public void setPriority(NotePriority priority) {
        this.priority = priority;
    }
}
