package com.backend.wavault.service.wallet;


import com.backend.wavault.model.entity.AppUser;
import com.backend.wavault.model.entity.Wallet;
import com.backend.wavault.model.request.VirtualAccountCreationRequest;
import com.backend.wavault.model.response.VirtualAccountCreationResponse;

import java.io.IOException;

public interface WalletService {

    Wallet createWallet(VirtualAccountCreationResponse response, AppUser user);
    VirtualAccountCreationResponse createVirtualAcc(VirtualAccountCreationRequest request) throws IOException;
}