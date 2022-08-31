package com.mindera.pizza.domain.order;

import org.springframework.data.jpa.domain.Specification;

public class RestaurantOrderSpecifications {
    private static Specification<RestaurantOrder> clientNameEquals(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("client").get("name"), name);
    }

    private static Specification<RestaurantOrder> clientVatNumberEquals(String vatNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("client").get("vatNumber"), vatNumber);
    }

    private static Specification<RestaurantOrder> clientPhoneNumberEquals(String phoneNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("client").get("phoneNumber"), phoneNumber);
    }

    private static Specification<RestaurantOrder> orderStatusEquals(String orderStatus) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("currentStatus"), OrderStatus.findValue(orderStatus));
    }

    public static Specification<RestaurantOrder> getSpecificationFromFilterName(String filterName, String value) {
        return switch (filterName) {
            case "clientName" -> clientNameEquals(value);
            case "vatNumber" -> clientVatNumberEquals(value);
            case "phoneNumber" -> clientPhoneNumberEquals(value);
            case "currentStatus" -> orderStatusEquals(value);
            default -> null;
        };
    }
}
