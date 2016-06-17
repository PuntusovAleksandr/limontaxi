package taxi.lemon.models;

/**
 * Hold profile item info
 */
public class ProfileItem {
    public enum Titles {
        LOGIN, NAME, PHONE, BALANCE, ADDRESS, ORDERS_COUNT, DISCOUNT, PAYMENT_TYPE, BONUSES, ENTRANCE
    }

    private Titles title;
    private String content;

    public ProfileItem(Titles title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Titles getTitle() {

        return title;
    }

    public void setTitle(Titles title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ProfileItem{" +
                "title=" + title +
                ", content='" + content + '\'' +
                '}';
    }
}
