package feup.traverse;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author hugof
 * @date 30/12/2015.
 */
public class CustomTextBoldF extends TextView {

    private static Typeface BoldF;

    public CustomTextBoldF(final Context context) {
        this(context, null);
    }

    public CustomTextBoldF(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextBoldF(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (BoldF == null) {

            BoldF = Typeface.createFromAsset(context.getAssets(), "fonts/qsB.otf");
        }
        setTypeface(BoldF);
    }
}
