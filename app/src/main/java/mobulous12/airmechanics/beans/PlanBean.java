package mobulous12.airmechanics.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mobulous12 on 5/12/16.
 */

public class PlanBean implements Parcelable {

    String planId,planName,expiryDate,planAmount,credits,remainingPoints,description;

    protected PlanBean(Parcel in) {
        planId = in.readString();
        planName = in.readString();
        expiryDate = in.readString();
        planAmount = in.readString();
        credits = in.readString();
        remainingPoints = in.readString();
        description = in.readString();
    }
    public PlanBean()
    {

    }

    public static final Creator<PlanBean> CREATOR = new Creator<PlanBean>() {
        @Override
        public PlanBean createFromParcel(Parcel in) {
            return new PlanBean(in);
        }

        @Override
        public PlanBean[] newArray(int size) {
            return new PlanBean[size];
        }
    };

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(String planAmount) {
        this.planAmount = planAmount;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getRemainingPoints() {
        return remainingPoints;
    }

    public void setRemainingPoints(String remainingPoints) {
        this.remainingPoints = remainingPoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(planId);
        dest.writeString(planName);
        dest.writeString(expiryDate);
        dest.writeString(planAmount);
        dest.writeString(credits);
        dest.writeString(remainingPoints);
        dest.writeString(description);
    }
}
