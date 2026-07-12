package com.grapevine.finance.cash;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CashMovementRepository
        extends JpaRepository<CashMovement, Long> {
}