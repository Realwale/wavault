package com.backend.wavault.service.user;

import com.backend.wavault.exception.InvalidArgumentException;
import com.backend.wavault.exception.ResourceAlreadyExistsException;
import com.backend.wavault.exception.ResourceNotFoundException;
import com.backend.wavault.model.dao.token.TokenRepository;
import com.backend.wavault.model.dao.user.AppUserRepository;
import com.backend.wavault.model.entity.AppUser;
import com.backend.wavault.model.entity.JwtToken;
import com.backend.wavault.model.entity.KycDetails;
import com.backend.wavault.model.entity.Wallet;
import com.backend.wavault.model.enums.KycStatus;
import com.backend.wavault.model.enums.Role;
import com.backend.wavault.model.request.LoginRequest;
import com.backend.wavault.model.request.RegisterRequest;
import com.backend.wavault.model.request.VirtualAccountCreationRequest;
import com.backend.wavault.model.response.APIResponse;
import com.backend.wavault.model.response.VirtualAccountCreationResponse;
import com.backend.wavault.security.JwtService;
import com.backend.wavault.service.token.TokenService;
import com.backend.wavault.service.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;


@RequiredArgsConstructor
@Service
public class AppUserServiceImpl implements AppUserService{

    private final AppUserRepository userRepository;
    private final TokenService tokenService;
    private final WalletService walletService;
    private final PasswordEncoder encoder;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

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
                .password(encoder.encode(request.getPassword()))
                .address(request.getAddress())
                .role(Role.USER)
                .isVerified(false)
                .build();

        AppUser user = userRepository.save(newUser);
        KycDetails kycDetails = KycDetails.builder()
                .status(KycStatus.Pending)
                .build();
        user.setKycDetails(kycDetails);
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

    @Override
    public APIResponse authenticateLogin(LoginRequest request) {
        AppUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with email " + request.getEmail()));

        if (!user.isVerified()) {
            throw new InvalidArgumentException("Account is not verified. " +
                    "Please check your email to verify your account or request a new confirmation link.");
        }
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidArgumentException("Incorrect email or password");
        }
        JwtToken existingToken = tokenRepository.findByUser(user);
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtToken tokens = JwtToken.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .generatedAt(Date.from(Instant.now()))
                .expiryDate(jwtService.extractExpiration(accessToken))
                .user(user)
                .isRevoked(false)
                .isExpired(false)
                .build();

        JwtToken savedTokens = tokenRepository.save(tokens);

        HashMap<String, String> returnToken = new HashMap<>();

        returnToken.put("access-token", savedTokens.getToken());
        returnToken.put("refresh-token", savedTokens.getRefreshToken());
        return APIResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .hasError(false)
                .message("Login successful")
                .data(returnToken)
                .build();
    }
}
