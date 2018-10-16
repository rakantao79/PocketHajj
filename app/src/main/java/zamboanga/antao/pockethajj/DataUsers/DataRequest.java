package zamboanga.antao.pockethajj.DataUsers;

/**
 * Created by abdulrahmanantao on 31/08/2017.
 */

public class DataRequest {

    private String request_type;

    public DataRequest() {
    }

    public DataRequest(String request_type) {
        this.request_type = request_type;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }
}
