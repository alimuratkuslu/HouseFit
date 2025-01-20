package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.TipDto;
import com.alikuslu.housefit.demo.model.Tip;
import com.alikuslu.housefit.demo.model.TipCategory;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.repository.TipRepository;
import com.alikuslu.housefit.demo.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipService {

    private final TipRepository tipRepository;
    private final UserRepository userRepository;

    public TipService(TipRepository tipRepository, UserRepository userRepository) {
        this.tipRepository = tipRepository;
        this.userRepository = userRepository;
    }

    public Tip createTip(TipDto dto) {
        String username = getLoggedInUsername();

        User trainer = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        /*
        if (trainer.getUserType() != UserType.TRAINER) {
            throw new UnauthorizedException("Only trainers can create tips");
        }
        */

        Tip tip = new Tip();
        tip.setTitle(dto.getTitle());
        tip.setContent(dto.getContent());
        tip.setCategory(dto.getCategory());
        tip.setTrainer(trainer);

        return tipRepository.save(tip);
    }

    public List<Tip> getAllTips() {
        return tipRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Tip> getTipsByCategory(TipCategory category) {
        return tipRepository.findByCategory(category);
    }

    public Tip getRandomTip() {
        return tipRepository.findRandomTip()
                .orElseThrow(() -> new RuntimeException("No tips available"));
    }

    private String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
