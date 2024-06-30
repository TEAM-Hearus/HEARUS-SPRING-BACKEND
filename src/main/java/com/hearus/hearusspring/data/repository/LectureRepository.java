package com.hearus.hearusspring.data.repository;

import com.hearus.hearusspring.data.model.LectureModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LectureRepository extends MongoRepository<LectureModel, String> {

    // READ
    List<LectureModel> findAll();
    LectureModel findFirstById(String id);
    LectureModel findFirstByName(String id);
    boolean existsById(String id);
    boolean existsByName(String name);

    // DELETE
    void deleteById(String id);
}