package com.backend.wavault.service.user;

import com.backend.wavault.exception.ResourceAlreadyExistsException;
import com.backend.wavault.model.dao.user.AppUserRepository;
import com.backend.wavault.model.entity.AppUser;
import com.backend.wavault.model.entity.Wallet;
import com.backend.wavault.model.enums.Role;
import com.backend.wavault.model.request.RegisterRequest;
import com.backend.wavault.model.request.VirtualAccountCreationRequest;
import com.backend.wavault.model.response.APIResponse;
import com.backend.wavault.model.response.VirtualAccountCreationResponse;
import com.backend.wavault.service.token.TokenService;
import com.backend.wavault.service.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;


@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService{

    private final AppUserRepository userRepository;
    private final TokenService tokenService;
    private final WalletService walletService;

    @Override
    public APIResponse createAccount(RegisterRequest request) throws IOException {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("User already exists with email " + request.getEmail());
        }

        AppUser newUser = AppUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .bvn(request.getBvn())
                .password(request.getPassword())
                .address(request.getAddress())
                .role(Role.USER)
                .isVerified(false)
                .build();

        AppUser user = userRepository.save(newUser);
        tokenService.saveConfirmationToken(user);
        VirtualAccountCreationResponse creationResponse = walletService.createVirtualAcc(
              VirtualAccountCreationRequest.builder()
                      .firstname(request.getFirstName())
                      .lastname(request.getLastName())
                      .phoneNumber(request.getPhoneNumber())
                      .email(request.getEmail())
                      .is_permanent(true)
                      .bvn(request.getBvn())
                      .narration(request.getFirstName()+" "+request.getLastName())
                      .build()
        );

        Wallet userWallet = walletService.createWallet(creationResponse, user);
        user.setWallet(userWallet);
        userRepository.save(user);
        return APIResponse.builder()
                .hasError(false)
                .statusCode(HttpStatus.CREATED.value())
                .message("Your registration is almost complete. " +
                        "Please check your email for the confirmation link to complete the process.")
                .data(null)
                .build();
    }
}
