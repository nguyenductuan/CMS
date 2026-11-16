package com.vt.cms.base.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic response wrapper that corresponds to Response[T any] struct
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private String message;
    private T data;



}