package tracker.Users;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class UserDetails {
    public static final String MALE = "m";
    public static final String FEMALE = "f";
    public static final String UNKNOWN = "u";

    public static final int WEIGHT_DEFAULT = 65;
    public static final int HEIGHT_DEFAULT = 170;
    public static final String SEX_DEFAULT = MALE;
    public static final String BIRTHDAY_DEFAULT = getDefaultBirthday();

    private String id;
    private int height;
    private double weight;
    private String birthday;
    private String sex;

    public UserDetails() {
    }

    public UserDetails(String id, int height, double weight, String birthday, String sex) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.birthday = birthday;
    }

    public static UserDetails getDefaultUserDetails(String id) {
        return new UserDetails(id, HEIGHT_DEFAULT, WEIGHT_DEFAULT, BIRTHDAY_DEFAULT, SEX_DEFAULT);
    }

    public static final String getDefaultBirthday() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.YEAR, 2000);

        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
