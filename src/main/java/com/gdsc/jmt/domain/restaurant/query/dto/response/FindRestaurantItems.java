package com.gdsc.jmt.domain.restaurant.query.dto.response;


public record FindRestaurantItems(
        Long id,
        String name,
        String placeUrl,
        String phone,

        String address,

        String roadAddress,
        Double x,
        Double y,
        String restaurantImageUrl,
        String introduce,
        String category,
        String userNickName,
        String userProfileImageUrl,
        Boolean canDrinkLiquor,

        String differenceInDistance
) {
    public static FindRestaurantItems createDefault(Long id,
                               String name,
                               String placeUrl,
                               String phone,
                               String address,
                               String roadAddress,
                               Double x,
                               Double y,
                               String restaurantImageUrl,
                               String introduce,
                               String category,
                               String userNickName,
                               String userProfileImageUrl,
                               Boolean canDrinkLiquor) {
        return new FindRestaurantItems(
                id,
                name,
                placeUrl,
                phone,
                address,
                roadAddress,
                x,
                y,
                restaurantImageUrl,
                introduce,
                category,
                userNickName,
                userProfileImageUrl,
                canDrinkLiquor,
                ""
        );
    }

    public static FindRestaurantItems createWithDistance(FindRestaurantItems findRestaurantItems,
                                                    String differenceInDistance) {
        return new FindRestaurantItems(
                findRestaurantItems.id,
                findRestaurantItems.name,
                findRestaurantItems.placeUrl,
                findRestaurantItems.phone,
                findRestaurantItems.address,
                findRestaurantItems.roadAddress,
                findRestaurantItems.x,
                findRestaurantItems.y,
                findRestaurantItems.restaurantImageUrl,
                findRestaurantItems.introduce,
                findRestaurantItems.category,
                findRestaurantItems.userNickName,
                findRestaurantItems.userProfileImageUrl,
                findRestaurantItems.canDrinkLiquor,
                differenceInDistance
        );
    }
}
