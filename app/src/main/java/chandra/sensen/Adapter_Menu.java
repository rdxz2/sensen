package chandra.sensen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by rd on 25/04/2018.
 */

public class Adapter_Menu extends FragmentStatePagerAdapter{

    //INIT
    int mNoOfTabs;

    //CONSTRUCTOR
    public Adapter_Menu(FragmentManager fm, int NumberOfTabs){
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }

    //BUAT FRAGMENT BARU (SESUAI MENU 4 BIJI YANG LAGI DIPILIH ITU)
    @Override
    public Fragment getItem(int position) {
        switch (position){
            //MENU UTAMA
            case 0:
                Fragment_MenuUtama framgent_menuUtama = new Fragment_MenuUtama();
                return framgent_menuUtama;
            //MENU ABSENSI
            case 1:
                Fragment_MenuAbsensi fragment_menuAbsensi = new Fragment_MenuAbsensi();
                return fragment_menuAbsensi;
            //MENU UMAT
            case 2:
                Fragment_MenuUmat fragment_menuUmat = new Fragment_MenuUmat();
                return fragment_menuUmat;
            //MENU ADMIN
            case 3:
                Fragment_MenuAdmin fragment_menuAdmin = new Fragment_MenuAdmin();
                return fragment_menuAdmin;
            //DEFAULT
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
