package com.mindera.pizza.domain.address;

import com.mindera.pizza.domain.DatabaseTimestamps;
import com.mindera.pizza.domain.client.Client;
import com.mindera.pizza.utils.Errors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
@EqualsAndHashCode
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter @Setter
    private String streetName;

    @Getter @Setter
    private int streetNumber;

    @Getter @Setter
    private String zipCode;

    @Getter @Setter
    private String city;

    @Getter @Setter
    private String nickname;

    @Getter
    private String apartmentInformation;

    @ManyToOne
    @Getter @Setter
    private Client client;

    @Getter
    @Embedded
    private final DatabaseTimestamps timestamps = new DatabaseTimestamps();

    protected Address() {}

    public Address(String streetName, int streetNumber, String zipCode, String city, String nickname, Client client) {
        if (streetName.isBlank()) {
            throw new IllegalArgumentException(Errors.INVALID_STREET_NAME.toString());
        }

        if (streetNumber <= 0) {
            throw new IllegalArgumentException(Errors.INVALID_STREET_NUMBER.toString());
        }

        if (!verifyZipCode(zipCode)) {
            throw new IllegalArgumentException(Errors.INVALID_ZIP_CODE.toString());
        }

        if (city.isBlank()) {
            throw new IllegalArgumentException(Errors.INVALID_CITY.toString());
        }

        if (nickname.isBlank()) {
            throw new IllegalArgumentException(Errors.INVALID_NICKNAME.toString());
        }

        if (client == null) {
            throw new IllegalArgumentException(Errors.INVALID_CLIENT.toString());
        }

        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.zipCode = zipCode;
        this.city = city;
        this.nickname = nickname;
        this.client = client;
    }

    private boolean verifyZipCode(String zipCode) {
        String zipCodeRegex = "[1-9][0-9]{3}-[0-9]{3}";
        return Pattern.matches(zipCodeRegex, zipCode);
    }

    public void setApartmentInformation(String apartmentInformation) {
        if (apartmentInformation.isBlank()) {
            throw new IllegalArgumentException(Errors.INVALID_APARTMENT_INFORMATION.toString());
        }
        this.apartmentInformation = apartmentInformation;
    }
}
