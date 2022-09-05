package com.mindera.pizza.domain.client;

import com.mindera.pizza.domain.DatabaseTimestamps;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.regex.Pattern;

@Entity
@EqualsAndHashCode
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private String vatNumber;

    @Getter
    private String phoneNumber;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String name;

    @Getter
    @Embedded
    private final DatabaseTimestamps timestamps = new DatabaseTimestamps();

    protected Client() {}

    public Client (String name, String email) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Invalid name");
        }

        if (!verifyEmail(email)) {
            throw new IllegalArgumentException("Invalid email");
        }

        this.name = name;
        this.email = email;
    }

    private boolean verifyEmail(String email) {
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";
        return Pattern.matches(emailRegex, email);
    }

    private boolean verifyPhoneNumber(String phoneNumber) {
        String phoneNumberRegex = "(9[1236][0-9]{7})|(2[0-9]{8})";
        return Pattern.matches(phoneNumberRegex, phoneNumber);
    }

    private boolean verifyVatNumber(String vatNumber) {
        String vatNumberRegex = "[0-9]{9}";
        return Pattern.matches(vatNumberRegex, vatNumber);
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!verifyPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setVatNumber(String vatNumber) {
        if (!verifyVatNumber(vatNumber)) {
            throw new IllegalArgumentException("Invalid VAT Number");
        }
        this.vatNumber = vatNumber;
    }
}
