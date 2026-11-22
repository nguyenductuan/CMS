package com.vt.cms.Exception;

import com.vt.cms.base.enums.MessageCode;

public class BusinessException extends RuntimeException {

  private final String code;
  private final Object data;

  /** Thực hiện xử lý business exception. Tham số và kết quả tuân theo chữ ký phương thức. */
  public BusinessException(MessageCode messageCode) {
    super(messageCode.getMessage());
    this.code = messageCode.getCode();
    this.data = null;
  }

  public BusinessException(MessageCode messageCode, Throwable cause) {
    super(messageCode.getMessage(), cause);
    this.code = messageCode.getCode();
    this.data = null;
  }

  // Constructor bổ sung data
  public BusinessException(MessageCode messageCode, Object data) {
    super(messageCode.getMessage());
    this.code = messageCode.getCode();
    this.data = data;
  }

  // Constructor nhận message, data và cause
  public BusinessException(MessageCode messageCode, Object data, Throwable cause) {
    super(messageCode.getMessage(), cause);
    this.code = messageCode.getCode();
    this.data = data;
  }

  /** Lấy dữ liệu get code. Tham số và kết quả tuân theo chữ ký phương thức. */
  public String getCode() {
    return code;
  }

  public Object getData() {
    return data;
  }
}
