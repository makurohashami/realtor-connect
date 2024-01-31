package com.kotyk.realtorconnect.specification;

import com.kotyk.realtorconnect.dto.realestate.RealEstateFilter;
import com.kotyk.realtorconnect.entity.realestate.RealEstate;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RealEstateSpecifications {
    public static Specification<RealEstate> withFilter(RealEstateFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getRealtorId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("realtor").get("id"), filter.getRealtorId()));
            }
            if (filter.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + filter.getName() + "%"));
            }
            if (filter.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filter.getMinPrice()));
            }
            if (filter.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), filter.getMaxPrice()));
            }
            if (filter.getCity() != null) {
                predicates.add(criteriaBuilder.like(root.get("location").get("city"), "%" + filter.getCity() + "%"));
            }
            if (filter.getDistrict() != null) {
                predicates.add(criteriaBuilder.like(root.get("location").get("district"), "%" + filter.getDistrict() + "%"));
            }
            if (filter.getResidentialArea() != null) {
                predicates.add(criteriaBuilder.like(root.get("location").get("residentialArea"), "%" + filter.getResidentialArea() + "%"));
            }
            if (filter.getStreet() != null) {
                predicates.add(criteriaBuilder.like(root.get("location").get("street"), "%" + filter.getStreet() + "%"));
            }
            if (filter.getLoggiaType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("loggia").get("type"), filter.getLoggiaType()));
            }
            if (filter.getLoggiasCount() != null) {
                predicates.add(criteriaBuilder.equal(root.get("loggia").get("count"), filter.getLoggiasCount()));
            }
            if (filter.getIsLoggiaGlassed() != null) {
                predicates.add(criteriaBuilder.equal(root.get("loggia").get("glassed"), filter.getIsLoggiaGlassed()));
            }
            if (filter.getBathroomType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("bathroom").get("type"), filter.getBathroomType()));
            }
            if (filter.getBathroomsCount() != null) {
                predicates.add(criteriaBuilder.equal(root.get("bathroom").get("count"), filter.getBathroomsCount()));
            }
            if (filter.getIsBathroomCombined() != null) {
                predicates.add(criteriaBuilder.equal(root.get("bathroom").get("combined"), filter.getIsBathroomCombined()));
            }
            if (filter.getMinTotalArea() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("area").get("total"), filter.getMinTotalArea()));
            }
            if (filter.getMaxTotalArea() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("area").get("total"), filter.getMaxTotalArea()));
            }
            if (filter.getMinLivingArea() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("area").get("living"), filter.getMinLivingArea()));
            }
            if (filter.getMaxLivingArea() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("area").get("living"), filter.getMaxLivingArea()));
            }
            if (filter.getMinKitchenArea() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("area").get("kitchen"), filter.getMinKitchenArea()));
            }
            if (filter.getMaxKitchenArea() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("area").get("kitchen"), filter.getMaxKitchenArea()));
            }
            if (filter.getMinFloor() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("floor"), filter.getMinFloor()));
            }
            if (filter.getMaxFloor() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("floor"), filter.getMaxFloor()));
            }
            if (filter.getBuildingType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("buildingType"), filter.getBuildingType()));
            }
            if (filter.getHeatingType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("heatingType"), filter.getHeatingType()));
            }
            if (filter.getWindowsType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("windowsType"), filter.getWindowsType()));
            }
            if (filter.getHotWaterType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("hotWaterType"), filter.getHotWaterType()));
            }
            if (filter.getStateType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("stateType"), filter.getStateType()));
            }
            if (filter.getAnnouncementType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("announcementType"), filter.getAnnouncementType()));
            }
            if (filter.getRoomsCount() != null) {
                predicates.add(criteriaBuilder.equal(root.get("roomsCount"), filter.getRoomsCount()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
