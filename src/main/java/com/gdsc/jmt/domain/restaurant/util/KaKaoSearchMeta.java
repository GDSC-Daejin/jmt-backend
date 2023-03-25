package com.gdsc.jmt.domain.restaurant.util;

import lombok.Getter;

@Getter
public class KaKaoSearchMeta {
    private KaKaoSearchMetaSameName same_name;

    private Integer pageable_count;

    private Integer total_count;

    private boolean is_end;
}
