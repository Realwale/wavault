package com.backend.wavault.model.dao.wallet;

import com.backend.wavault.model.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
}