package com.backend.wavault.service.wallet;

import com.backend.wavault.model.dao.wallet.WalletRepository;
import com.backend.wavault.model.entity.AppUser;
import com.backend.wavault.model.entity.Wallet;
import com.backend.wavault.model.flutterwave.VirtualAccountClient;
import com.backend.wavault.model.request.VirtualAccountCreationRequest;
import com.backend.wavault.model.response.VirtualAccountCreationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService{

    private final WalletRepository walletRepository;
    private final VirtualAccountClient accountClient;

    @Override
    public Wallet createWallet(VirtualAccountCreationResponse response, AppUser user) {

        Wallet wallet = Wallet.builder()
                .accountBalance(response.getData().getAmount())
                .accountNumber(response.getData().getAccount_number())
                .bankName(response.getData().getBank_name())
                .active(true)
                .user(user)
                .build();
        return walletRepository.save(wallet);
    }

    @Override
    public VirtualAccountCreationResponse createVirtualAcc(VirtualAccountCreationRequest request) throws IOException {

        return (VirtualAccountCreationResponse) accountClient.createVirtualAccount(request);
    }
}
