package com.gdsc.jmt.domain.user.query.dto;

import com.gdsc.jmt.domain.user.util.KakaoLocationDocument;
import com.gdsc.jmt.global.util.kakao.KakaoSearchMeta;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoUserLocationResponse {
    private KakaoSearchMeta meta;
    private List<KakaoLocationDocument> documents;
}
