package feup.traverse;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.jar.Attributes;

/**
 * Created by Filipe on 29/12/2015.
 */
public class BoldText extends TextView {
    public BoldText(Context context, AttributeSet attrs ){
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/asB.otf"));
    }
}
