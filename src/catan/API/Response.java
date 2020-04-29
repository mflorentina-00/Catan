package catan.API;

public class Response {
    private Integer code;
    private String status;
    private String data;

    public Response(Integer code, String status, String data) {
        this.code = code;
        this.status = status;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
        return "Response{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
