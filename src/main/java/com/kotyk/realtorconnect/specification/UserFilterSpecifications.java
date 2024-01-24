package com.kotyk.realtorconnect.specification;

import com.kotyk.realtorconnect.dto.user.UserFilter;
import com.kotyk.realtorconnect.entity.user.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserFilterSpecifications {

    public static Specification<User> withFilter(UserFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            if (filter.getEmail() != null) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + filter.getEmail() + "%"));
            }
            if (filter.getPhone() != null) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + filter.getPhone() + "%"));
            }
            if (filter.getRole() != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), filter.getRole()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
