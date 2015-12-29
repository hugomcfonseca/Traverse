package feup.traverse;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapterTextPagerAdapter  extends FragmentStatePagerAdapter {
    public ViewChapterTextPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new ViewChapterTextFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }
}
