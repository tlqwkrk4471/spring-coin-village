package com.coinvillage.backend.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserAssetResponse {

    private String name;
    private Long totalAsset;

    public UserAssetResponse() {}

    public UserAssetResponse(String name, Long totalAsset) {
        this.name = name;
        this.totalAsset = totalAsset;
    }
}
