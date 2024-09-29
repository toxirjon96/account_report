package uz.infinity.zero_one_demo.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
@Component
public class ResponseHelper {
    public ResponseEntity<?> prepareResponse(Object object, Message message, HttpStatus responseStatus) {
        Map<String, Object> result = new HashMap<>();
        result.put("data", object);
        result.put("message", message);
        result.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(result, responseStatus);
    }
}