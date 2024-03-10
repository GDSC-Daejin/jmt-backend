package com.gdsc.jmt.domain.restaurant.query.service;

import com.gdsc.jmt.domain.restaurant.query.dto.request.MapLocation;
import com.gdsc.jmt.domain.restaurant.query.dto.request.RestaurantSearchFromOtherGroupRequest;
import com.gdsc.jmt.domain.restaurant.query.dto.request.RestaurantSearchMapRequest;
import com.gdsc.jmt.domain.restaurant.query.dto.request.RestaurantSearchRequest;
import com.gdsc.jmt.domain.restaurant.query.entity.RecommendRestaurantEntity;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RestaurantDynamicSearchService {

    public Specification<RecommendRestaurantEntity> searchMapRestaurant(RestaurantSearchMapRequest request, List<Long> categoryIds, Pageable pageable) {
        return ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(request.groupId() != null) {
                predicates.add(builder.equal(root.get("group").get("gid"), request.groupId()));
            }

            if (request.startLocation() != null && request.endLocation() != null) {
                String locationRange = makeLocationRange(request.startLocation(), request.endLocation());
                Expression<Boolean> distanceWithin = builder.function("ST_Within", Boolean.class,
                        root.join("restaurant").get("location"),
                        builder.function("ST_GeomFromText", Double.class,
                                builder.literal(locationRange)
                        ));
                predicates.add(builder.isTrue(distanceWithin));
            }

            if(request.filter() != null) {
                if (request.filter().isCanDrinkLiquor() != null) {
                    predicates.add(builder.equal(root.get("canDrinkLiquor"), request.filter().isCanDrinkLiquor()));
                }
                if(request.filter().categoryFilter() != null) {
                    predicates.add(root.join("category").get("id").in(categoryIds));
                }
            }

            if (request.userLocation() != null) {

                String checkDistanceSort = pageable.getSort().toString();
                if(checkDistanceSort.contains("distance")) {
                    String sortProcess = null;
                    Pattern pattern = Pattern.compile("[|](.*?)[:]");
                    Matcher matcher = pattern.matcher(checkDistanceSort);
                    while (matcher.find()) {
                        sortProcess = matcher.group(1);
                        if (matcher.group(1) == null) break;
                    }

                    if(sortProcess != null) {
                        if("asc".equals(sortProcess)) {
                            String userLocation = "POINT(" + request.userLocation().x() + " " + request.userLocation().y() + ")";
                            query.orderBy(builder.asc(builder.function("ST_DISTANCE_SPHERE", Double.class,
                                    root.join("restaurant").get("location"),
                                    builder.function("ST_GeomFromText", Double.class,
                                            builder.literal(userLocation)
                                    )
                            )));
                        }
                        else {
                            String userLocation = "POINT(" + request.userLocation().x() + " " + request.userLocation().y() + ")";
                            query.orderBy(builder.desc(builder.function("ST_DISTANCE_SPHERE", Double.class,
                                    root.join("restaurant").get("location"),
                                    builder.function("ST_GeomFromText", Double.class,
                                            builder.literal(userLocation)
                                    )
                            )));
                        }
                    }
                }

//                String userLocation = "POINT(" + request.userLocation().x() + " " + request.userLocation().y() + ")";
//                query.orderBy(builder.asc(builder.function("ST_DISTANCE_SPHERE", Double.class,
//                        root.join("restaurant").get("location"),
//                        builder.function("ST_GeomFromText", Double.class,
//                                builder.literal(userLocation)
//                        )
//                )));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public Specification<RecommendRestaurantEntity> searchKeywordRestaurant(RestaurantSearchRequest request) {
        return ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if(request.keyword() != null) {
                predicates.add(builder.like(root.join("restaurant").get("name"), "%"+ request.keyword() + "%"));
            }

//            if (request.startLocation() != null && request.endLocation() != null) {
//                String locationRange = makeLocationRange(request.startLocation(), request.endLocation());
//                Expression<Boolean> distanceWithin = builder.function("ST_Within", Boolean.class,
//                        root.join("restaurant").get("location"),
//                        builder.function("ST_GeomFromText", Double.class,
//                                builder.literal(locationRange)
//                        ));
//                predicates.add(builder.isTrue(distanceWithin));
//            }
            if (request.userLocation() != null) {
                String userLocation = "POINT(" + request.userLocation().x() + " " + request.userLocation().y() + ")";
                query.orderBy(builder.asc(builder.function("ST_DISTANCE_SPHERE", Double.class,
                        root.join("restaurant").get("location"),
                        builder.function("ST_GeomFromText", Double.class,
                                builder.literal(userLocation)
                        )
                )));
            }
            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    public Specification<RecommendRestaurantEntity> searchKeywordRestaurant(RestaurantSearchFromOtherGroupRequest request) {
        return ((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();


            if(request.keyword() != null) {
                predicates.add(builder.like(root.join("restaurant").get("name"), "%"+ request.keyword() + "%"));
            }

            if(request.currentGroupId() != null) {
                predicates.add(builder.notEqual(root.join("group").get("gid"), request.currentGroupId()));
            }

            query.orderBy(builder.asc(builder.function("RAND", Double.class)));
            return builder.and(predicates.toArray(new Predicate[0]));
        });
    }

    private String makeLocationRange(MapLocation startLocation, MapLocation endLocation) {
        String userLocationRange = "POLYGON((";
        userLocationRange += startLocation.x() + " " + startLocation.y() + ", ";
        userLocationRange += endLocation.x() + " " + startLocation.y() + ", ";
        userLocationRange += endLocation.x() + " " + endLocation.y() + ", ";
        userLocationRange += startLocation.x() + " " + endLocation.y() + ", ";
        userLocationRange += startLocation.x() + " " + startLocation.y() + "))";

        return userLocationRange;
    }
}
