package com.test.example.demo.service;

import com.test.example.demo.exception.AppException;
import com.test.example.demo.model.CallDetailDto;
import com.test.example.demo.model.Credentials;
import com.test.example.demo.model.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Service
public class CallService {

    private static final String Log_File_Path = "/static/SIPServer.log_mskd.log";

    private final Map<String, CallDetailDto> callDetailHashMap = new HashMap<>();


    /**
     * Retrieves unique callIDs from the log file
     *
     * @return Set<String> containing unique callIds
     * @throws IOException
     */
    public Set<String> getUniqueCallIDs() throws IOException {
        readLogFile();
        return callDetailHashMap.keySet();
    }

    /**
     * Retrieves callID metadata
     *
     * @return CallDetailDto containing callID metadata
     * @throws IOException
     */
    public CallDetailDto getCallIDDetails(String callID) {
        return Optional.ofNullable(callDetailHashMap.get(callID))
                .orElseThrow(() -> new AppException("Call detail not found.", HttpStatus.NOT_FOUND));
    }

    private void readLogFile() throws IOException {
        boolean conditionFound = false;
        String callId = null;
        CallDetailDto dto = null;
        InputStream inputStream = CallService.class.getResourceAsStream(Log_File_Path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String from = null;
        String to = null;
        String cseq = null;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("From:")) {
                from = line.substring(line.indexOf(":") + 1).trim();
            } else if (line.startsWith("To:")) {
                to = line.substring(line.indexOf(":") + 1).trim();
            } else if (line.startsWith("Call-ID")) {
                callId = line.substring(line.indexOf(":") + 1).trim();
            } else if (line.startsWith("CSeq")) {
                cseq = line.substring(line.indexOf(":") + 1).trim();
            } else if (line.startsWith("Content-Length")) {
                if (cseq.contains("BYE")) {
                    callDetailHashMap.put(callId, new CallDetailDto(callId, from, to));
                } else
                    callDetailHashMap.put(callId, null);
            }
        }
    }

    /**
     * validates user details
     *
     * @return UserDto containing user details
     * @throws IOException
     */
    public UserDto login(Credentials credentials) {

        char[] Password123;
        Credentials sample = new Credentials("Megha", "Password123".toCharArray());
        UserDto userDto = new UserDto("Megha M S", "Megha", "Password123");
        if (credentials.getLogin().equals(sample.getLogin())) {
            if (Arrays.equals(credentials.getPassword(), sample.getPassword())) {
                return userDto;
            }
            throw new AppException("Invalid Password", HttpStatus.BAD_REQUEST);
        } else throw new AppException("Unknown User", HttpStatus.NOT_FOUND);
    }

}
