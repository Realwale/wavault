package com.backend.wavault.model.entity;

import com.backend.wavault.model.enums.IdentificationDocument;
import com.backend.wavault.model.enums.KycStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "kyc_details")
public class KycDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateOfBirth;
    private String address;
    @Enumerated(EnumType.STRING)
    private IdentificationDocument identificationDocument;
    private String identificationNumber;
    private String proofOfAddress;
    private String occupation;
    @Enumerated(EnumType.STRING)
    private KycStatus status;
    @OneToOne(mappedBy = "kycDetails")
    private AppUser user;
}
