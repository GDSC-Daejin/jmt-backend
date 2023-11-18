package com.gdsc.jmt.domain.user.query.dto;

import com.gdsc.jmt.domain.user.util.KakaoSearchDocument;
import com.gdsc.jmt.global.util.kakao.KakaoSearchMeta;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoSearchLocationResponse {
        private KakaoSearchMeta meta;
        private List<KakaoSearchDocument> documents;
}
