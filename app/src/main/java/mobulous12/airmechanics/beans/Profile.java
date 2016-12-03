package mobulous12.airmechanics.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mobulous2 on 2/11/16.
 */
public class Profile implements Parcelable
{
    private String fullname="";
    private String companyname="";
    private String email="";
    private String password="";
    private String contactno="";
    private String address="";
    private String imagePath="";
    private String country_code="";
    private String category="";
    private String city="";
    private String lat="";
    private String lng="";

    public Profile() {
    }

    protected Profile(Parcel in) {
        fullname = in.readString();
        companyname = in.readString();
        email = in.readString();
        password = in.readString();
        contactno = in.readString();
        address = in.readString();
        imagePath = in.readString();
        country_code = in.readString();
        category = in.readString();
        city = in.readString();
        lat = in.readString();
        lng = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullname);
        dest.writeString(companyname);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(contactno);
        dest.writeString(address);
        dest.writeString(imagePath);
        dest.writeString(country_code);
        dest.writeString(category);
        dest.writeString(city);
        dest.writeString(lat);
        dest.writeString(lng);
    }
}
