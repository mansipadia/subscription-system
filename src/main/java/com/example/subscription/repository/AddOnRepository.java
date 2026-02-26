package com.example.subscription.repository;

import com.example.subscription.entity.AddOns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddOnRepository extends JpaRepository<AddOns,Long> {

    List<AddOns> findByActiveTrue();
}
