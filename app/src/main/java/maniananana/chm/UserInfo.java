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

    public UserInfo() {}

    public List<UserLocation> getLocations() {
        return Locations;
    }


    public String getFullName() {
        return FullName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getUserEmail() {
        return UserEmail;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setLocations(List<UserLocation> locations) {
        Locations = locations;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
