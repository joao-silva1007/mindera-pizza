package com.mindera.pizza.domain.client;

import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.utils.DataValidationConstants;
import com.mindera.pizza.utils.Errors;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
public class Client extends DatabaseTimestamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    @Pattern(regexp = "[0-9]{9}", message = DataValidationConstants.INVALID_VAT_NUMBER)
    private String vatNumber;

    @Getter @Setter
    @Pattern(regexp = "(9[1236][0-9]{7})|(2[0-9]{8})", message = DataValidationConstants.INVALID_PHONE_NUMBER)
    private String phoneNumber;

    @Getter @Setter
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])",
            message = DataValidationConstants.INVALID_EMAIL)
    private String email;

    @Getter @Setter
    @NotBlank(message = DataValidationConstants.INVALID_NAME)
    private String name;

    protected Client() {}
}
