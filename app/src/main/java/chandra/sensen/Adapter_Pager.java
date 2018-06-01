package chandra.sensen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by rd on 25/04/2018.
 */

public class Adapter_Pager extends FragmentStatePagerAdapter{

    int mNoOfTabs;

    public Adapter_Pager(FragmentManager fm, int NumberOfTabs){
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Fragment_MenuUtama framgent_menuUtama = new Fragment_MenuUtama();
                return framgent_menuUtama;
            case 1:
                Fragment_MenuAbsensi fragment_menuAbsensi = new Fragment_MenuAbsensi();
                return fragment_menuAbsensi;
            case 2:
                Fragment_MenuData fragment_menuData = new Fragment_MenuData();
                return fragment_menuData;
            case 3:
                Fragment_MenuAdmin fragment_menuAdmin = new Fragment_MenuAdmin();
                return fragment_menuAdmin;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
