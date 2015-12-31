package feup.traverse;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author hugof
 * @date 30/12/2015.
 */
public class CustomTextRegularF extends TextView {

    private static Typeface RegularF;

    public CustomTextRegularF(final Context context) {
        this(context, null);
    }

    public CustomTextRegularF(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextRegularF(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (RegularF == null) {

            RegularF = Typeface.createFromAsset(context.getAssets(), "fonts/qsR.otf");
        }
        setTypeface(RegularF);
    }
}
