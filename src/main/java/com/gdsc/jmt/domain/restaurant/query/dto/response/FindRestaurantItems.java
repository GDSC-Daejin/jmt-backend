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

        String differenceInDistance,

        Long groupId,

        String groupName
) {
    public static FindRestaurantItems createWithDistance(Long id,
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
                                                         Long groupId,
                                                         String groupName) {
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
                "",
                groupId,
                groupName
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
                differenceInDistance,
                findRestaurantItems.groupId,
                findRestaurantItems.groupName
        );
    }
}
