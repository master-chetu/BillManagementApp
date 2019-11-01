package chetu.felixpat.letsply.model;

/**
 * Created by SHIVANI-SHALINI on 2/8/2018.
 */

public class UserDetails {
    String userName;
    String emailId;
    String password;

    public UserDetails(String userName, String emailId, String password) {
        this.userName = userName;
        this.emailId = emailId;
        this.password = password;
    }

    public UserDetails() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

