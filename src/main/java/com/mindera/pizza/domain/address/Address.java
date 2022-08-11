package com.mindera.pizza.domain.address;

import com.mindera.pizza.domain.client.Client;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private LocalDateTime createdAt;

    @Getter @Setter
    private LocalDateTime updatedAt;

    protected Address() {}

    public Address(String streetName, int streetNumber, String zipCode, String city, String nickname, Client client) {
        if (streetName.isBlank()) {
            throw new IllegalArgumentException("Invalid street name");
        }

        if (streetNumber <= 0) {
            throw new IllegalArgumentException("Invalid street number");
        }

        if (!verifyZipCode(zipCode)) {
            throw new IllegalArgumentException("Invalid zip code");
        }

        if (city.isBlank()) {
            throw new IllegalArgumentException("Invalid city");
        }

        if (nickname.isBlank()) {
            throw new IllegalArgumentException("Invalid nickname");
        }

        if (client == null) {
            throw new IllegalArgumentException("Invalid client");
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
            throw new IllegalArgumentException("Invalid apartment information");
        }
        this.apartmentInformation = apartmentInformation;
    }
}
