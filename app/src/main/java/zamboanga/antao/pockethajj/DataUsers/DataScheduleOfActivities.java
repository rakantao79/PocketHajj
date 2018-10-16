package zamboanga.antao.pockethajj.DataUsers;

/**
 * Created by abdulrahmanantao on 02/09/2017.
 */

public class DataScheduleOfActivities {

    public String title;
    public String content;
    public String date;
    public String time;

    public DataScheduleOfActivities() {
    }

    public DataScheduleOfActivities(String title, String content, String date, String time) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
