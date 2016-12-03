package mobulous12.airmechanics.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mobulous2 on 2/11/16.
 */
public class ServiceProviderBean implements Parcelable
{
    String lat, lng, id, name, address, email, contact_no, profile, profile_thumb="", category, workhours, descritption, distance, workingdays="",
            start, end, reviews, rating, min_charge;

    public ServiceProviderBean() {
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMin_charge() {
        return min_charge;
    }

    public void setMin_charge(String min_charge) {
        this.min_charge = min_charge;
    }

    protected ServiceProviderBean(Parcel in) {
        lat = in.readString();
        lng = in.readString();
        id = in.readString();
        name = in.readString();
        address = in.readString();
        email = in.readString();
        contact_no = in.readString();
        profile = in.readString();
        profile_thumb = in.readString();
        category = in.readString();
        workhours = in.readString();
        descritption = in.readString();
        distance = in.readString();
        workingdays = in.readString();
        start = in.readString();
        end = in.readString();
        reviews = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(contact_no);
        dest.writeString(profile);
        dest.writeString(profile_thumb);
        dest.writeString(category);
        dest.writeString(workhours);
        dest.writeString(descritption);
        dest.writeString(distance);
        dest.writeString(workingdays);
        dest.writeString(start);
        dest.writeString(end);
        dest.writeString(reviews);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ServiceProviderBean> CREATOR = new Creator<ServiceProviderBean>() {
        @Override
        public ServiceProviderBean createFromParcel(Parcel in) {
            return new ServiceProviderBean(in);
        }

        @Override
        public ServiceProviderBean[] newArray(int size) {
            return new ServiceProviderBean[size];
        }
    };

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWorkhours() {
        return workhours;
    }

    public void setWorkhours(String workhours) {
        this.workhours = workhours;
    }

    public String getDescritption() {
        return descritption;
    }

    public void setDescritption(String descritption) {
        this.descritption = descritption;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getWorkingdays() {
        return workingdays;
    }

    public void setWorkingdays(String workingdays) {
        this.workingdays = workingdays;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfile_thumb() {
        return profile_thumb;
    }

    public void setProfile_thumb(String profile_thumb) {
        this.profile_thumb = profile_thumb;
    }

}
