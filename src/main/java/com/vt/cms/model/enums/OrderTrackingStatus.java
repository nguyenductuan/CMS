package com.vt.cms.model.enums;

/**
 * Mã sự kiện tracking nhận từ đơn vị vận chuyển.
 * Các mã được đặt tên theo đúng mã tracking để thuận tiện đối chiếu callback.
 */
public enum OrderTrackingStatus {
    S101,
    S103,
    S104,
    S200,
    S320,
    S400,
    S410,
    S500,
    S505,
    S506,
    S507,
    S508,
    S509,
    RETURN_PICKUP_SUCCESS,
    RETURN_PICKUP_FAILED,
    RETURN_DELIVERY_SUCCESS,
    RETURN_DELIVERY_FAILED
}
