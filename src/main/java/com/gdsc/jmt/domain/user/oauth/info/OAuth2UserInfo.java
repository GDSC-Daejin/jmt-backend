package com.gdsc.jmt.domain.user.oauth.info;

import java.util.Map;

public abstract class OAuth2UserInfo {
    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();
}
