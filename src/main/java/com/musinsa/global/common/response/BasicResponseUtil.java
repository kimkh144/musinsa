package com.musinsa.global.common.response;

public class BasicResponseUtil {

    public static <T> BasicResponseDTO<T> success(T response) {
        return new BasicResponseDTO<>(response, null);
    }

    public static BasicResponseDTO<?> error(BasicResponseDTO.BasicResponseError e) {
        return new BasicResponseDTO<>(null,null, e);
    }

}
