package maniananana.chm;

import java.util.List;

public class UserInfo {
    private String FullName;
    private List<UserLocation> Locations;
    private String PhoneNumber;
    private String UserEmail;
    private String isAdmin;

    public UserInfo(String fullName, List<UserLocation> locations, String phoneNumber, String email, String isAdmin) {
        this.FullName = fullName;
        this.Locations = locations;
        this.PhoneNumber = phoneNumber;
        this.UserEmail = email;
        this.isAdmin = isAdmin;
    }

    public UserInfo() {
    }

    public List<UserLocation> getLocations() {
        return Locations;
    }

    public void setLocations(List<UserLocation> locations) {
        Locations = locations;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
