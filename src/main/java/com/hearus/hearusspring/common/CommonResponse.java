package com.hearus.hearusspring.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CommonResponse {
    private boolean isSuccess;
    HttpStatus resCode;
    private String msg;

    public CommonResponse(boolean isSuccess, String msg) {
        this.isSuccess = isSuccess;
        this.msg = msg;
    }

    public CommonResponse(boolean isSuccess, HttpStatus resCode, String msg) {
        this.isSuccess = isSuccess;
        this.resCode = resCode;
        this.msg = msg;
    }
}
