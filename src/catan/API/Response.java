package catan.API;

public class Response {
    private Integer code;
    private String status;
    private String arguments;

    public Response(Integer code, String status, String arguments) {
        this.code = code;
        this.status = status;
        this.arguments = arguments;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
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
                ", data='" + arguments + '\'' +
                '}';
    }
}
