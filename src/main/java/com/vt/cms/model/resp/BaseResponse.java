package com.vt.cms.model.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic response wrapper that corresponds to  Response[T any] struct
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {

    private T data;


    public static <T> BaseResponse<T> of(T data) {

        return new BaseResponse<>(data);
    }
}