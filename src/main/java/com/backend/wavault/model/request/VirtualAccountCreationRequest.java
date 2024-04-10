package com.backend.wavault.model.request;

import jakarta.validation.ValidationException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualAccountCreationRequest {

    private String email;
    private boolean is_permanent;
    private String bvn;
    private String phoneNumber;
    private String firstname;
    private String lastname;
    private String narration;
    private int amount;

    public void validateVirtualAccountCreationRequest() {
        List<String> errors = new ArrayList<>();

        validateEmail(email, errors);
        validateBvn(bvn, errors);
        validatePhoneNumber(phoneNumber, errors);
        validateFirstName(firstname, errors);
        validateLastName(lastname, errors);
        validateNarration(narration, errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join(", ", errors));
        }
    }

    private static void validateEmail(String email, List<String> errors) {
        Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}", Pattern.CASE_INSENSITIVE);
        if (email == null || !emailPattern.matcher(email).matches()) {
            errors.add("Invalid email format");
        }
    }

    private static void validateBvn(String bvn, List<String> errors) {
        if (bvn == null || bvn.trim().isEmpty()) {
            errors.add("BVN field cannot be empty");
        } else if (bvn.length() != 11 || !bvn.matches("\\d+")) {
            errors.add("BVN must be a 11-digit string containing only digits");
        }
    }

    private static void validatePhoneNumber(String phoneNumber, List<String> errors) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            errors.add("Phone number field cannot be empty");
        } else if (phoneNumber.length() != 11 || !phoneNumber.matches("\\d+")) {
            errors.add("Phone number must be a 11-digit string containing only digits");
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

    private static void validateNarration(String narration, List<String> errors) {
        if (narration == null || narration.trim().isEmpty()) {
            errors.add("Narration field cannot be empty");
        }
    }
}
