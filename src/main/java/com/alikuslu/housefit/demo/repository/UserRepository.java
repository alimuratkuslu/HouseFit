package com.alikuslu.housefit.demo.repository;

import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
    User findByReferralCode(String referralCode);
    List<User> findByUserType(UserType userType);

    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.surname) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "u.userType = :type")
    List<User> searchByNameOrSurnameAndType(String query, UserType type);
}
