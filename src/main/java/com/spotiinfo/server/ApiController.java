package com.spotiinfo.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        System.out.println("GET request received at /test");
        return ResponseEntity.status(HttpStatus.OK).body("yeah it works");
    }
}
