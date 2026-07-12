package com.grapevine.inventory.transfer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferGuideRepository extends JpaRepository<TransferGuide, Long> {
    List<TransferGuide> findAllByOrderByCreatedAtDesc();
}