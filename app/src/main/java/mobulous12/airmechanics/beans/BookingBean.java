package mobulous12.airmechanics.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mobulous2 on 20/10/16.
 */
public class BookingBean implements Parcelable
{
    private String status="",userName = "",userImage="",profile_thumb="",minCharge="",requestDate="",requestTime="",
            usernumber="", useraddress="", bookingid="", requestdesc="", requestname="", requestcategory="",requestImage = "";
    private String latitude = "", longitude = "", serviceType = "";
    private String openTime = "", closeTime = "";
    private String createdOn = "", categoryId = "", category = "", serviceproviderid="", walletAmount="";
    private String payAmount = "";
    private String usedWalletAmount="", rating="";

    protected BookingBean(Parcel in) {
        status = in.readString();
        userName = in.readString();
        userImage = in.readString();
        profile_thumb = in.readString();
        minCharge = in.readString();
        requestDate = in.readString();
        requestTime = in.readString();
        usernumber = in.readString();
        useraddress = in.readString();
        bookingid = in.readString();
        requestdesc = in.readString();
        requestname = in.readString();
        requestcategory = in.readString();
        requestImage = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        serviceType = in.readString();
        openTime = in.readString();
        closeTime = in.readString();
        createdOn = in.readString();
        categoryId = in.readString();
        category = in.readString();
        serviceproviderid = in.readString();
        walletAmount = in.readString();
        payAmount = in.readString();
        usedWalletAmount = in.readString();
        rating = in.readString();
        paymentId = in.readString();
        requestImgArr = in.createStringArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(userName);
        dest.writeString(userImage);
        dest.writeString(profile_thumb);
        dest.writeString(minCharge);
        dest.writeString(requestDate);
        dest.writeString(requestTime);
        dest.writeString(usernumber);
        dest.writeString(useraddress);
        dest.writeString(bookingid);
        dest.writeString(requestdesc);
        dest.writeString(requestname);
        dest.writeString(requestcategory);
        dest.writeString(requestImage);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(serviceType);
        dest.writeString(openTime);
        dest.writeString(closeTime);
        dest.writeString(createdOn);
        dest.writeString(categoryId);
        dest.writeString(category);
        dest.writeString(serviceproviderid);
        dest.writeString(walletAmount);
        dest.writeString(payAmount);
        dest.writeString(usedWalletAmount);
        dest.writeString(rating);
        dest.writeString(paymentId);
        dest.writeStringArray(requestImgArr);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BookingBean> CREATOR = new Creator<BookingBean>() {
        @Override
        public BookingBean createFromParcel(Parcel in) {
            return new BookingBean(in);
        }

        @Override
        public BookingBean[] newArray(int size) {
            return new BookingBean[size];
        }
    };

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    private String paymentId="";

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getUsedWalletAmount() {
        return usedWalletAmount;
    }

    public void setUsedWalletAmount(String usedWalletAmount) {
        this.usedWalletAmount = usedWalletAmount;
    }

    private String[] requestImgArr={};

    public String getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(String walletAmount) {
        this.walletAmount = walletAmount;
    }


    public String getRequestImage() {
        return requestImage;
    }

    public void setRequestImage(String requestImage) {
        this.requestImage = requestImage;
    }


    public String[] getRequestImgArr() {
        return requestImgArr;
    }

    public void setRequestImgArr(String[] requestImgArr) {
        this.requestImgArr = requestImgArr;
    }

    public BookingBean() {
    }

    public String getServiceproviderid() {
        return serviceproviderid;
    }

    public void setServiceproviderid(String serviceproviderid) {
        this.serviceproviderid = serviceproviderid;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public String getMinCharge() {
        return minCharge;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public void setMinCharge(String minCharge) {
        this.minCharge = minCharge;
    }

    public String getProfile_thumb() {
        return profile_thumb;
    }

    public void setProfile_thumb(String profile_thumb) {
        this.profile_thumb = profile_thumb;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getBookingid() {
        return bookingid;
    }

    public void setBookingid(String bookingid) {
        this.bookingid = bookingid;
    }

    public String getRequestdesc() {
        return requestdesc;
    }

    public String getRequestname() {
        return requestname;
    }

    public void setRequestname(String requestname) {
        this.requestname = requestname;
    }

    public void setRequestdesc(String requestdesc) {
        this.requestdesc = requestdesc;

    }

    public String getRequestcategory() {
        return requestcategory;
    }

    public void setRequestcategory(String requestcategory) {
        this.requestcategory = requestcategory;
    }


}
