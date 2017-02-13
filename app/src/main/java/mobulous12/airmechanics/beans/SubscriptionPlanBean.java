package mobulous12.airmechanics.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mobulous12 on 10/2/17.
 */

public class SubscriptionPlanBean implements Parcelable {

    private String userType=""; //customer or Service provider

    private String id="";
    private String planName="";
    private String planPayAmountKES="";
    private String planPayAmountUSD="";
    private String transactionId="";
    private String validityDuration="";

    public String getPlanPayAmountKES() {
        return planPayAmountKES;
    }

    public void setPlanPayAmountKES(String planPayAmountKES) {
        this.planPayAmountKES = planPayAmountKES;
    }

    public String getPlanPayAmountUSD() {
        return planPayAmountUSD;
    }

    public void setPlanPayAmountUSD(String planPayAmountUSD) {
        this.planPayAmountUSD = planPayAmountUSD;
    }

    public SubscriptionPlanBean() {

    }

    protected SubscriptionPlanBean(Parcel in) {
        userType = in.readString();
        id = in.readString();
        planName = in.readString();
        planPayAmountKES = in.readString();
        planPayAmountUSD = in.readString();
        transactionId = in.readString();
        validityDuration = in.readString();
    }

    public static final Creator<SubscriptionPlanBean> CREATOR = new Creator<SubscriptionPlanBean>() {
        @Override
        public SubscriptionPlanBean createFromParcel(Parcel in) {
            return new SubscriptionPlanBean(in);
        }

        @Override
        public SubscriptionPlanBean[] newArray(int size) {
            return new SubscriptionPlanBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }



    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getValidityDuration() {
        return validityDuration;
    }

    public void setValidityDuration(String validityDuration) {
        this.validityDuration = validityDuration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userType);
        dest.writeString(id);
        dest.writeString(planName);
        dest.writeString(planPayAmountKES);
        dest.writeString(planPayAmountUSD);
        dest.writeString(transactionId);
        dest.writeString(validityDuration);
    }
}
