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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;
    

    @PreAuthorize("hasAuthority('SCOPE_notes.write')")
    @PostMapping
    public NoteResponse createNote(
        @Valid @RequestBody CreateNoteRequest request,
        Authentication authentication) {

    String userEmail = authentication.getName();

    return noteService.createNote(request, userEmail);
}


    
    
    @PreAuthorize("hasAuthority('SCOPE_notes.read')")
    @GetMapping
    public List<NoteResponse> getUserNotes(Authentication authentication) {

            Jwt jwt = (Jwt) authentication.getPrincipal();
        return noteService.getUserNotes(authentication.getName());
    }

    
    @PreAuthorize("hasAuthority('SCOPE_notes.read')")
    @GetMapping("/{id}")
    public NoteResponse getNoteById(@PathVariable Long id, Authentication authentication) {
        return noteService.getNoteById(id, authentication.getName());
    }
    
    
    @PreAuthorize("hasAuthority('SCOPE_notes.write')")
    @PutMapping("/{id}")
    public NoteResponse updateNotes(@PathVariable Long id, 
                @RequestBody CreateNoteRequest request, 
                Authentication authentication){
        String userEmail = authentication.getName();
        
        return noteService.updateNote(id,request, userEmail);
    }
    
    
    

    @PreAuthorize("hasAuthority('SCOPE_notes.write')")
    @DeleteMapping("/{id}")
    public String deleteNote(@PathVariable Long id,
                             Authentication authentication) {

        String userEmail = authentication.getName();

        noteService.deleteNote(id, userEmail);

        return "Deleted successfully";
    }
}