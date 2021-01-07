package maniananana.chm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String FullName;
    private List<UserLocation> Locations;
    private String PhoneNumber;
    private String UserEmail;
    private String isAdmin;

}
