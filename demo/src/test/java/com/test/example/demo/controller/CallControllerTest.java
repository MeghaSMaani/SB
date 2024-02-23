package com.test.example.demo.controller;

import com.test.example.demo.exception.AppException;
import com.test.example.demo.model.CallDetailDto;
import com.test.example.demo.service.CallService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Set;

import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class CallControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CallService mockCallService;

    private static final String jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJNZWdoYSIsIm5hbWUiOiJNZWdoYSBNIFMiLCJleHAiOjE3MDg1OTY5MjYsImlhdCI6MTcwODU5MzMyNn0.cQI4hmkLs20Pg-3vvaCRPbva-EOrlSotIgbX1hXlmlg";

    @Test
    void testGetAllUniqueCallIDs_success() throws Exception {

        when(mockCallService.getUniqueCallIDs()).thenReturn(Set.of("123", "234"));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fetchAllSession")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    void testGetAllUniqueCallIDs_failure() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fetchAllSession"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void testGetCallIDDetails_success() throws Exception {

        CallDetailDto callDetailDto = new CallDetailDto("123", "Abc", "def");
        when(mockCallService.getCallIDDetails("123")).thenReturn(callDetailDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/GetSessionMetaData/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.from").value("Abc"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.to").value("def"));
    }

    @Test
    void testGetCallIDDetails_failure() throws Exception {

        CallDetailDto callDetailDto = new CallDetailDto("123", "Abc", "def");
        when(mockCallService.getCallIDDetails("123")).thenThrow(new AppException("Invalid Class ID", HttpStatus.NOT_FOUND));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/GetSessionMetaData/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
