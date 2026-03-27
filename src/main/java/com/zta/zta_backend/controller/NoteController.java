/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.controller;

/**
 *
 * @author hcdc
 */
import com.zta.zta_backend.dto.CreateNoteRequest;
import com.zta.zta_backend.dto.NoteResponse;
import com.zta.zta_backend.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    
//    //  Create Note
//    @PostMapping
//    public NoteResponse createNote(@Valid @RequestBody CreateNoteRequest request) {
//        return noteService.createNote(request, CURRENT_USER);
//    }
    
    @PostMapping
    public NoteResponse createNote(
        @Valid @RequestBody CreateNoteRequest request,
        Authentication authentication) {

    String userEmail = authentication.getName();

    return noteService.createNote(request, userEmail);
}

//    //  Get all notes of user
//    @GetMapping
//    public List<NoteResponse> getUserNotes() {
//        return noteService.getUserNotes(CURRENT_USER);
//    }
    
    @GetMapping
    public List<NoteResponse> getUserNotes(Authentication authentication) {
        return noteService.getUserNotes(authentication.getName());
    }

//    //  Get note by ID (with ownership check 🔥)
//    @GetMapping("/{id}")
//    public NoteResponse getNoteById(@PathVariable Long id) {
//        return noteService.getNoteById(id, CURRENT_USER);
//    }
    
    @GetMapping("/{id}")
    public NoteResponse getNoteById(@PathVariable Long id, Authentication authentication) {
        return noteService.getNoteById(id, authentication.getName());
    }
}