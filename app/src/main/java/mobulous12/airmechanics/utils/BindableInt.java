package mobulous12.airmechanics.utils;

import org.parceler.Parcel;

/**
 * Created by mobulous1 on 1/8/16.
 */
@Parcel
public class BindableInt extends BaseObservable {
    Integer value;

    public Integer get() {
        return value;
    }

    public void set(Integer value) {
        this.value = value;
        notifyChange();
    }

}
