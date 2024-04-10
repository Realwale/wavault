package com.backend.wavault.model.dao.user;

import com.backend.wavault.model.entity.KycDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KycDetailsRepository extends JpaRepository<KycDetails, Long> {
}
