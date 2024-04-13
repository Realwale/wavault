package com.backend.wavault.model.request;



import com.backend.wavault.utils.UserUtils;
import jakarta.validation.ValidationException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String email;
    private String password;


    public void validateLoginRequest() {
        List<String> errors = new ArrayList<>();

        validateEmail(email, errors);
        validatePassword(password, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(", ", errors));
        }
    }

    private static void validateEmail(String email, List<String> errors) {
        Pattern pat = Pattern.compile(UserUtils.EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is missing or empty");
        } else if (!pat.matcher(email).matches()) {
            errors.add("Invalid email format");
        }
    }

    private static void validatePassword(String password, List<String> errors) {
        if (password == null || password.trim().isEmpty()) {
            errors.add("Password is missing or empty");
        }
    }
}
