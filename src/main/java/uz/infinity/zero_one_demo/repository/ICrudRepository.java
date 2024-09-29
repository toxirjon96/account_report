package uz.infinity.zero_one_demo.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudRepository <D>{
    ResponseEntity<?> create(D request);
    ResponseEntity<?> update(String id, D request);
    ResponseEntity<?> delete(String id);
}