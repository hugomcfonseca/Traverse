package feup.traverse;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapterTextPagerAdapter  extends FragmentStatePagerAdapter {

    ViewChapter view = new ViewChapter();
    public int fragment_number;

    public ViewChapterTextPagerAdapter(FragmentManager fm, int fragment_number) {
        super(fm);
        this.fragment_number = fragment_number;
    }

    @Override
    public Fragment getItem(int position) {

        if (fragment_number == 1){
            return new ViewChapterMenuFragment();
        } else if (fragment_number == 2) {
            return new ViewChapterMapsFragment();
        } else if (fragment_number == 3) {
            return new ViewChapterTextFragment();
        } else return null;
    }

    @Override
    public int getCount() {
        if (fragment_number != 3) {
            return 1;
        } else
            return 3;
    }
}
