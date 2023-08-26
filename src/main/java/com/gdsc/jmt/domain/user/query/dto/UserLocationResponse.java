package com.gdsc.jmt.domain.user.query.dto;

import com.gdsc.jmt.domain.user.util.dto.Address;
import com.gdsc.jmt.domain.user.util.dto.RoadAddress;

public record UserLocationResponse (
    String address,
    String roadAddress
) {
    public UserLocationResponse(Address address, RoadAddress roadAddress) {
        this(address.getAddress_name(), roadAddress.getAddress_name());
    }
}
