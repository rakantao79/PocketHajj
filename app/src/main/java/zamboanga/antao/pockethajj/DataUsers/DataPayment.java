package zamboanga.antao.pockethajj.DataUsers;

/**
 * Created by abdulrahmanantao on 12/09/2017.
 */

public class DataPayment {

    public String screenshoturl;
    public Double amount;
    public String dateupload;

    public DataPayment() {
    }

    public DataPayment(String screenshoturl, Double amount, String dateupload) {
        this.screenshoturl = screenshoturl;
        this.amount = amount;
        this.dateupload = dateupload;
    }

    public String getScreenshoturl() {
        return screenshoturl;
    }

    public void setScreenshoturl(String screenshoturl) {
        this.screenshoturl = screenshoturl;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDateupload() {
        return dateupload;
    }

    public void setDateupload(String dateupload) {
        this.dateupload = dateupload;
    }
}
