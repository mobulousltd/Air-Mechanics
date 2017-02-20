package mobulous12.airmechanics.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mobulous12 on 20/2/17.
 */

public class EarningsBean implements Parcelable {

    private String customerName="";
    private String paidAmount="";
    private String date="";
    private String address="";

    public EarningsBean() {

    }

    protected EarningsBean(Parcel in) {
        customerName = in.readString();
        paidAmount = in.readString();
        date = in.readString();
        address = in.readString();
    }

    public static final Creator<EarningsBean> CREATOR = new Creator<EarningsBean>() {
        @Override
        public EarningsBean createFromParcel(Parcel in) {
            return new EarningsBean(in);
        }

        @Override
        public EarningsBean[] newArray(int size) {
            return new EarningsBean[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerName);
        dest.writeString(paidAmount);
        dest.writeString(date);
        dest.writeString(address);
    }
}
