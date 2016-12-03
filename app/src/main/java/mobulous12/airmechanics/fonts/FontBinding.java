package mobulous12.airmechanics.fonts;

import android.databinding.BindingAdapter;
import android.widget.TextView;

public class FontBinding
{
    @BindingAdapter({"bind:font"})
    public static void setFont(TextView textView, String fontName)
    {
        textView.setTypeface(FontCache.getInstance().get(fontName));
    }

}
