package zamboanga.antao.pockethajj.DataUsers;

/**
 * Created by abdulrahmanantao on 27/08/2017.
 */

public class DataUsers {

    public String lastname;
    public String firstname;
    public String mobile;
    public String thumbimage;
    public String accounttype;

    public DataUsers() {
    }

    public DataUsers(String lastname, String firstname, String mobile, String thumbimage, String accounttype) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.mobile = mobile;
        this.thumbimage = thumbimage;
        this.accounttype = accounttype;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getThumbimage() {
        return thumbimage;
    }

    public void setThumbimage(String thumbimage) {
        this.thumbimage = thumbimage;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }
}
