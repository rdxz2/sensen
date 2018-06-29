package chandra.sensen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //INIT
        final EditText usernameEdit = (EditText) findViewById(R.id.username_edit);
        final EditText passwordEdit = (EditText) findViewById(R.id.password_edit);
        final CheckBox remembermeCheckBox = (CheckBox) findViewById(R.id.rememberme_checkbox);
        //SHARED PREFERENCES
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        usernameEdit.setText(pref.getString("USERNAME", ""));
        passwordEdit.setText(pref.getString("PASSWORD", ""));
        remembermeCheckBox.setChecked(pref.getBoolean("REMEMBER_ME", false));
        //BUTTON LOGIN
        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //INIT
                Contract_Admin.AdminDbHelper AdminDbHelper = new Contract_Admin.AdminDbHelper(Activity_Login.this);
                SQLiteDatabase db = AdminDbHelper.getReadableDatabase();
                Cursor cursor = db.query(
                        Contract_Admin.AdminEntry.TABLE_NAME,
                        new String[] {Contract_Admin.AdminEntry.COLUMN_NAME_USERNAME, Contract_Admin.AdminEntry.COLUMN_NAME_PASSWORD},
                        String.format("%s = ? AND %s = ?", Contract_Admin.AdminEntry.COLUMN_NAME_USERNAME, Contract_Admin.AdminEntry.COLUMN_NAME_PASSWORD),
                        new String[] {usernameEdit.getText().toString(), passwordEdit.getText().toString()},
                        null,
                        null,
                        null
                );
                //KALO ADA DATA
                if(cursor.getCount() > 0){
                    cursor.moveToFirst();
                    SharedPreferences.Editor prefEdit = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE).edit();
                    prefEdit.putString("ACCESS_LEVEL", "ADMIN");
                    //KALO REMEMBER ME DICENTANG -> SIMPAN SHARED PREFERENCES
                    if(remembermeCheckBox.isChecked()){
                        prefEdit.putString("USERNAME", usernameEdit.getText().toString());
                        prefEdit.putString("PASSWORD", passwordEdit.getText().toString());
                        prefEdit.putBoolean("REMEMBER_ME", remembermeCheckBox.isChecked());
                    }
                    //KALO REMEMBER ME GAK DICENTANG
                    else{
                        prefEdit.clear();
                    }
                    prefEdit.commit();
                    cursor.close();
                    startActivity(new Intent(Activity_Login.this, Activity_Menu.class));
                }
                //KALO GAADA DATA
                else{
                    Toast.makeText(Activity_Login.this, "Masukkan nama admin dan kata sandi yang benar", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //KALO REMEMBER ME DICENTANG -> LANGSUNG SKIP MENU LOGIN
        SharedPreferences pref = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE);
        if(pref.getString("ACCESS_LEVEL", "").equals("ADMIN")) startActivity(new Intent(Activity_Login.this, Activity_Menu.class));
    }
}
