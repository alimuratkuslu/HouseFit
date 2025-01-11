package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.UserType;
import com.alikuslu.housefit.demo.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointsResetService {

    private final UserRepository userRepository;

    public PointsResetService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //TODO: Test with every minute 0 * * * * ?
    @Scheduled(cron = "0 0 1 1 * ?")
    public void resetTrainerPoints() {
        List<User> trainers = userRepository.findByUserType(UserType.TRAINER);
        for (User trainer : trainers) {
            trainer.setPoints(0.0);
        }
        userRepository.saveAll(trainers);
    }
}
