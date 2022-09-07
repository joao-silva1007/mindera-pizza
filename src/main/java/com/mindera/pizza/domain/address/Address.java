package com.mindera.pizza.domain.address;

import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.utils.DataValidationConstants;
import com.mindera.pizza.utils.Errors;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@EqualsAndHashCode(callSuper = false)
public class Address extends DatabaseTimestamps{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    @NotBlank(message = DataValidationConstants.INVALID_STREET_NAME)
    private String streetName;

    @Getter @Setter
    @Positive(message = DataValidationConstants.INVALID_STREET_NUMBER)
    private int streetNumber;

    @Getter @Setter
    @Pattern(regexp = "[1-9][0-9]{3}-[0-9]{3}", message = DataValidationConstants.INVALID_ZIP_CODE)
    private String zipCode;

    @Getter @Setter
    @NotBlank(message = DataValidationConstants.INVALID_CITY)
    private String city;

    @Getter @Setter
    @NotBlank(message = DataValidationConstants.INVALID_NICKNAME)
    private String nickname;

    @Getter @Setter
    @Pattern(regexp = "[a-zA-Z][a-zA-Z0-9,. ]*", message = DataValidationConstants.INVALID_APARTMENT_INFORMATION)
    private String apartmentInformation;

    @ManyToOne
    @Getter @Setter
    @NotNull(message = DataValidationConstants.INVALID_CLIENT)
    private Client client;

    protected Address() {}

    @Builder
    public Address(String streetName, int streetNumber, String zipCode, String city, String nickname, Client client) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.nickname = nickname;
        this.client = client;
    }
}
