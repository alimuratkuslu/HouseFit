package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.Tip;
import com.alikuslu.housefit.demo.model.TipCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipRepository extends JpaRepository<Tip, Long> {

    List<Tip> findAllByOrderByCreatedAtDesc();
    List<Tip> findByCategory(TipCategory category);
}
