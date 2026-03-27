/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.service;

/**
 *
 * @author hcdc
 */

import com.zta.zta_backend.dto.CreateNoteRequest;
import com.zta.zta_backend.dto.NoteResponse;

import java.util.List;

public interface NoteService {

    NoteResponse createNote(CreateNoteRequest request, String userEmail);

    List<NoteResponse> getUserNotes(String userEmail);

    NoteResponse getNoteById(Long noteId, String userEmail);
}