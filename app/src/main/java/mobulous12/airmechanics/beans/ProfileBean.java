package mobulous12.airmechanics.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mobulous12 on 26/10/16.
 */

public class ProfileBean implements Parcelable {
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
    private String from="";
    private String to="";
    private String radius="";
    private String working_days="";
    private String employees="";
    private String speciality="";
    private String mnCharg="";

    public ProfileBean() {
    }


    protected ProfileBean(Parcel in) {
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
        from = in.readString();
        to = in.readString();
        radius = in.readString();
        working_days = in.readString();
        employees = in.readString();
        speciality = in.readString();
        mnCharg = in.readString();
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
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(radius);
        dest.writeString(working_days);
        dest.writeString(employees);
        dest.writeString(speciality);
        dest.writeString(mnCharg);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfileBean> CREATOR = new Creator<ProfileBean>() {
        @Override
        public ProfileBean createFromParcel(Parcel in) {
            return new ProfileBean(in);
        }

        @Override
        public ProfileBean[] newArray(int size) {
            return new ProfileBean[size];
        }
    };

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getMnCharg() {
        return mnCharg;
    }

    public void setMnCharg(String mnCharg) {
        this.mnCharg = mnCharg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getWorking_days() {
        return working_days;
    }

    public void setWorking_days(String working_days) {
        this.working_days = working_days;
    }

    public String getEmployees() {
        return employees;
    }

    public void setEmployees(String employees) {
        this.employees = employees;
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

}
