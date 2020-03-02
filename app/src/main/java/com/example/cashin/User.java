package com.example.cashin;

public class User{
    private String email;
    private String username;
    private String profilePic;
    private String phone;
    private String userUid;

    public User() {

    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public User(String email, String username, String profilePic,String userUid) {
        this.email = email;
        this.username = username;
        this.profilePic = profilePic;
        this.phone = phone;
        this.userUid=userUid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getPhone() {
        return phone;
    }

    public User(String email, String uid, String url) {
    }
}
