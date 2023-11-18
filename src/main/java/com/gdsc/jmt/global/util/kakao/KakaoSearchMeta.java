package com.gdsc.jmt.global.util.kakao;

import lombok.Getter;

@Getter
public class KakaoSearchMeta {
    private KakaoSearchMetaSameName same_name;

    private Integer pageable_count;

    private Integer total_count;

    private boolean is_end;
}
