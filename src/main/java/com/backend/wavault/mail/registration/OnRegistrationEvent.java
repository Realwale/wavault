package com.backend.wavault.mail.registration;

import com.backend.wavault.model.entity.AppUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Getter
@Setter
public class OnRegistrationEvent extends ApplicationEvent {

    private AppUser user;
    private String token;

    public OnRegistrationEvent(AppUser user, String token) {
        super(user);
        this.user = user;
        this.token = token;
    }
}
