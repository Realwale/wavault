package com.backend.wavault.model.request;

import jakarta.validation.ValidationException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private String bvn;
    private String address;

    public void validateRegistrationRequest() {
        List<String> errors = new ArrayList<>();

        validateFirstName(firstName, errors);
        validateLastName(lastName, errors);
        validateEmail(email, errors);
        validatePassword(password, errors);
        validatePhoneNumber(phoneNumber, errors);
        validateBvn(bvn, errors);
        validateAddress(address, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(", ", errors));
        }
    }

    private static void validateFirstName(String firstName, List<String> errors) {
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("First name is missing or empty");
        } else if (firstName.length() < 3) {
            errors.add("First name should be more than 2 characters");
        }
    }

    private static void validateLastName(String lastName, List<String> errors) {
        if (lastName == null || lastName.trim().isEmpty()) {
            errors.add("Last name is missing or empty");
        } else if (lastName.length() < 3) {
            errors.add("Last name should be more than 2 characters");
        }
    }

    private static void validatePassword(String password, List<String> errors) {
        if (password == null || password.trim().isEmpty()) {
            errors.add("Password field cannot be empty");
        } else {
            if (password.length() < 8 || password.length() > 24) {
                errors.add("Password cannot be less than 8 or more than 24 characters");
            }
            if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%&]).*$")) {
                errors.add("Password must contain at least one letter, one digit, and one special character");
            }
        }
    }

    private static void validateEmail(String email, List<String> errors) {
        Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", Pattern.CASE_INSENSITIVE);
        if (email == null || !emailPattern.matcher(email).matches()) {
            errors.add("Invalid email format");
        }
    }

    private static void validatePhoneNumber(String phoneNumber, List<String> errors) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            errors.add("Phone number field cannot be empty");
        } else if (phoneNumber.length() != 11 || !phoneNumber.matches("\\d+")) {
            errors.add("Phone number must be a 11-digit string containing only digits");
        }
    }

    private static void validateBvn(String bvn, List<String> errors) {
        if (bvn == null || bvn.trim().isEmpty()) {
            errors.add("BVN field cannot be empty");
        } else if (bvn.length() != 11 || !bvn.matches("\\d+")) {
            errors.add("BVN must be a 11-digit string containing only digits");
        }
    }

    private static void validateAddress(String address, List<String> errors) {
        if (address == null || address.trim().isEmpty()) {
            errors.add("Address is missing or empty");
        } else if (address.length() < 5) {
            errors.add("Address should be more than 5 characters");
        }
    }

}
