package chandra.sensen;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MenuActivity extends AppCompatActivity implements
        MenuUtamaFragment.OnFragmentInteractionListener,
        MenuAbsensiFragment.OnFragmentInteractionListener,
        MenuDataFragment.OnFragmentInteractionListener,
        MenuAdminFragment.OnFragmentInteractionListener{

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.info_button) {
            // do something here
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.menu_tab);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_black_24dp).setText("Utama"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_grid_on_black_24dp).setText("Absensi"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_person_black_24dp).setText("Data"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_settings_black_24dp).setText("Admin"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.menu_pager);
        final chandra.sensen.PagerAdapter adapter = new chandra.sensen.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_menu);
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.menu_frame, new MenuUtamaFragment()).commit();
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.menu_tab);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                switch (tab.getPosition()){
//                    case 0: fragmentTransaction.replace(R.id.menu_frame, new MenuUtamaFragment()); break;
//                    case 1: fragmentTransaction.replace(R.id.menu_frame, new MenuAbsensiFragment()); break;
//                    case 2: fragmentTransaction.replace(R.id.menu_frame, new MenuDataFragment()); break;
//                    case 3: fragmentTransaction.replace(R.id.menu_frame, new MenuAdminFragment()); break;
//                }
//                fragmentTransaction.commit();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }


}
