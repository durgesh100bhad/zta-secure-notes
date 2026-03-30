/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.security.hmac;

/**
 *
 * @author hcdc
 */
import jakarta.servlet.*;
import jakarta.servlet.http.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] body;

    public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
        super(request);
        body = request.getInputStream().readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() {

        ByteArrayInputStream bis = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            public int read() { return bis.read(); }
            public boolean isFinished() { return bis.available() == 0; }
            public boolean isReady() { return true; }
            public void setReadListener(ReadListener readListener) {}
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }
}
