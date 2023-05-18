package org.example.repository;
// new class
import java.util.List;
import java.util.Optional;

import jakarta.validation.constraints.NotNull;
import org.example.entity.Tutorial;
import org.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface TutorialRepository extends MongoRepository<Tutorial, String>{
    Page<Tutorial> findByPublished(boolean published, Pageable pageable);

    Page<Tutorial> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    @Query("{'title': ?0}")
    Optional<Tutorial> findByTitel(String tutorial);
    List<Tutorial> findByTitleContaining(String title, Sort sort);
    @NotNull
    Optional<Tutorial> findById(Tutorial tutorial,String id);


}
