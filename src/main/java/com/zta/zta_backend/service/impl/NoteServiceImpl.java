/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.service.impl;

/**
 *
 * @author hcdc
 */
import com.zta.zta_backend.dto.CreateNoteRequest;
import com.zta.zta_backend.dto.NoteResponse;
import com.zta.zta_backend.entity.Note;
import com.zta.zta_backend.entity.User;
import com.zta.zta_backend.exception.AccessDeniedException;
import com.zta.zta_backend.exception.ResourceNotFoundException;
import com.zta.zta_backend.repository.NoteRepository;
import com.zta.zta_backend.repository.UserRepository;
import com.zta.zta_backend.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Override
    public NoteResponse createNote(CreateNoteRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        Note note = Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .user(user)
                .build();

        Note savedNote = noteRepository.save(note);

        return mapToResponse(savedNote);
    }

    @Override
    public List<NoteResponse> getUserNotes(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        return noteRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public NoteResponse getNoteById(Long noteId, String userEmail) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + noteId));

        //  LEAST PRIVILEGE (ZTA CORE)
        if (!note.getUser().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You are not allowed to access this note");
        }

        return mapToResponse(note);
    }

    @Override
    public NoteResponse updateNote(Long id, CreateNoteRequest request, String userEmail) {

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());

        noteRepository.save(note);

        return mapToResponse(note);
    }
    
    
    @Override
    public void deleteNote(Long id, String userEmail) {

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        noteRepository.delete(note);
    }
    //  Mapping method
    private NoteResponse mapToResponse(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .build();
    }
}