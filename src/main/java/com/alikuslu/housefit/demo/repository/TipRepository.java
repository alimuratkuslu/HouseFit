package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.Tip;
import com.alikuslu.housefit.demo.model.TipCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TipRepository extends JpaRepository<Tip, Long> {

    List<Tip> findAllByOrderByCreatedAtDesc();
    List<Tip> findByCategory(TipCategory category);

    @Query(value = "SELECT * FROM tip ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Tip> findRandomTip();
}
