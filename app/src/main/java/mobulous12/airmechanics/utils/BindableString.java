package mobulous12.airmechanics.utils;

import android.annotation.TargetApi;

import org.parceler.Parcel;

import java.util.Objects;

@Parcel
public class BindableString extends BaseObservable {
    String value;

    public String get() {
        return value != null ? value : "";
    }

    @TargetApi(19)
    public void set(String value) {
        if (!Objects.equals(this.value, value)) {
            this.value = value;
            notifyChange();
        }
    }

    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }
}