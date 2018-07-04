package chandra.sensen;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Activity_Menu extends AppCompatActivity implements
        Fragment_MenuUtama.OnFragmentInteractionListener,
        Fragment_MenuAbsensi.OnFragmentInteractionListener,
        Fragment_MenuUmat.OnFragmentInteractionListener,
        Fragment_MenuAdmin.OnFragmentInteractionListener{

    //INFLATE BUTTON DI ACTION BAR (ADA 2 BIJI)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //SAAT BUTTON" DI ACTION BAR DIKLIK
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //INIT
        int id = item.getItemId();
        //BUTTON LOGOUT
        if(id == R.id.logout_button){
            SharedPreferences.Editor prefEdit = Activity_Menu.this.getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE).edit();
            prefEdit.clear();
            prefEdit.commit();
            finish();
        }
        //BUTTON INFO APLIKASI
        if(id == R.id.info_button){
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater.inflate(R.layout.dialog_info_aplikasi, null, false);
            final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(Activity_Menu.this);
            alertDialogBuilder.setView(view);
            final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            //BUTTON KEMBALI
            Button kembaliButton = (Button) view.findViewById(R.id.kembali_button);
            kembaliButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.hide();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //TAB LAYOUT (TAB YANG ADA DI ATAS ITU YANG ISINYA BUTTON 4 BIJI)
        final TabLayout tabLayout = findViewById(R.id.menu_tab);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_black_24dp).setText("Utama"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_grid_on_black_24dp).setText("Absensi"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_group_black_24dp).setText("Umat"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_settings_black_24dp).setText("Admin"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, android.R.color.black),
                ContextCompat.getColor(this, android.R.color.white)
        );
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getIcon().setColorFilter(ContextCompat.getColor(Activity_Menu.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
        //VIEWPAGER (HALAMAN YANG DITAMPILIN SAAT 4 BUTTON MASING" DIKLIK)
        final ViewPager viewPager = findViewById(R.id.menu_pager);
        final Adapter_Menu adapter = new Adapter_Menu(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //SAAT TAB DIPILIH
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(Activity_Menu.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
                viewPager.setCurrentItem(tab.getPosition());
            }
            //SAAT PINDAH TAB
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(Activity_Menu.this, android.R.color.black), PorterDuff.Mode.SRC_IN);
            }
            //SAAT TAB DIPILIH LAGI
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(Activity_Menu.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
            }
        });
    }

    //MATIIN TOMBOL BACK
    @Override
    public void onBackPressed() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
