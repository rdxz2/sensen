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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Activity_Menu extends AppCompatActivity implements
        Fragment_MenuUtama.OnFragmentInteractionListener,
        Fragment_MenuAbsensi.OnFragmentInteractionListener,
        Fragment_MenuData.OnFragmentInteractionListener,
        Fragment_MenuAdmin.OnFragmentInteractionListener{

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
        if(id == R.id.logout_button){
            SharedPreferences.Editor prefEdit = Activity_Menu.this.getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE).edit();
            prefEdit.clear();
            prefEdit.commit();
            finish();
        }
        if(id == R.id.info_button){
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater.inflate(R.layout.dialog_info_aplikasi, null, false);
            final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(Activity_Menu.this);
            alertDialogBuilder.setView(view);
            final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

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

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.menu_tab);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_black_24dp).setText("Utama"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_grid_on_black_24dp).setText("Absensi"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_group_black_24dp).setText("Data"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_settings_black_24dp).setText("Admin"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(
                ContextCompat.getColor(this, android.R.color.black),
                ContextCompat.getColor(this, android.R.color.white)
        );
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getIcon().setColorFilter(ContextCompat.getColor(Activity_Menu.this, android.R.color.white), PorterDuff.Mode.SRC_IN);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.menu_pager);
        final Adapter_Menu adapter = new Adapter_Menu(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(Activity_Menu.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(Activity_Menu.this, android.R.color.black), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(ContextCompat.getColor(Activity_Menu.this, android.R.color.white), PorterDuff.Mode.SRC_IN);
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
