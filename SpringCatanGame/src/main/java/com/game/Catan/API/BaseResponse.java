package com.game.Catan.API;

public class BaseResponse {
    private Integer code;
    private String status;

    public BaseResponse() {
    }
    public BaseResponse(Integer code,String status) {
        this.status = status;
        this.code = code;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", status='" + status + '\'' +
                '}';
    }
}
