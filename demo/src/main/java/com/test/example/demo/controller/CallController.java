package com.test.example.demo.controller;

import com.test.example.demo.model.CallDetailDto;
import com.test.example.demo.model.Credentials;
import com.test.example.demo.model.UserDto;
import com.test.example.demo.security.UserAuthProvider;
import com.test.example.demo.service.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class CallController {

    @Autowired
    private CallService callService;

    @Autowired
    private UserAuthProvider userAuthProvider;


    /**
     * This is a dummy implementation, just for JWT token generation
     */
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody Credentials credentials) {
        UserDto user = callService.login(credentials);
        user.setToken(userAuthProvider.createToken(user));
        System.out.println("");
        return ResponseEntity.ok(user);
    }

    @GetMapping("/fetchAllSession")
    public ResponseEntity<Set<String>> getAllUniqueCallIDs() throws IOException {
        Set<String> uniqueCallIDs = callService.getUniqueCallIDs();
        System.out.println(uniqueCallIDs.size());
        return ResponseEntity.ok(uniqueCallIDs);
    }

    @GetMapping("/GetSessionMetaData/{callID}")
    public ResponseEntity<CallDetailDto> getCallIDDetails(@PathVariable String callID) throws IOException {
        CallDetailDto callDetailDto = callService.getCallIDDetails(callID);
        return ResponseEntity.ok(callDetailDto);
    }
}
