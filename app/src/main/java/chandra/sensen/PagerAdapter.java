package chandra.sensen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by rd on 25/04/2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter{

    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs){
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                MenuUtamaFragment menuUtamaFragment = new MenuUtamaFragment();
                return menuUtamaFragment;
            case 1:
                MenuAbsensiFragment menuAbsensiFragment = new MenuAbsensiFragment();
                return menuAbsensiFragment;
            case 2:
                MenuDataFragment menuDataFragment = new MenuDataFragment();
                return menuDataFragment;
            case 3:
                MenuAdminFragment menuAdminFragment = new MenuAdminFragment();
                return menuAdminFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
