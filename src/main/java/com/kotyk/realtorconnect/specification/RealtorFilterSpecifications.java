package com.kotyk.realtorconnect.specification;

import com.kotyk.realtorconnect.dto.realtor.RealtorFilter;
import com.kotyk.realtorconnect.entity.realtor.Realtor;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RealtorFilterSpecifications {

    public static Specification<Realtor> withFilter(RealtorFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            if (filter.getPhone() != null) {
                predicates.add(criteriaBuilder.like(root.get("phone"), "%" + filter.getPhone() + "%"));
            }
            if (filter.getAgency() != null) {
                predicates.add(criteriaBuilder.like(root.get("agency"), "%" + filter.getAgency() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
