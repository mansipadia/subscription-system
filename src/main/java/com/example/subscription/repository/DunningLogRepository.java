package com.example.subscription.repository;

import com.example.subscription.entity.DunningLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DunningLogRepository extends JpaRepository<DunningLog, Long> {

    List<DunningLog> findBySubscriptionId(Long subscriptionId);

}
