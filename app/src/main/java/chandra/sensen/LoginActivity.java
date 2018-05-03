package chandra.sensen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEdit = (EditText) findViewById(R.id.username_edit);
        final EditText passwordEdit = (EditText) findViewById(R.id.password_edit);
        final CheckBox remembermeCheckBox = (CheckBox) findViewById(R.id.rememberme_checkbox);

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        usernameEdit.setText(pref.getString("USERNAME", ""));
        passwordEdit.setText(pref.getString("PASSWORD", ""));
        remembermeCheckBox.setChecked(pref.getBoolean("REMEMBER_ME", false));

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                if(usernameEdit.getText().toString().equals("admin") && passwordEdit.getText().toString().equals("admin")){
                    SharedPreferences.Editor prefEdit = getSharedPreferences("LOGIN_PREFERENCES", MODE_PRIVATE).edit();
                    prefEdit.putString("ACCESS_LEVEL", "admin");
                    prefEdit.commit();
                    prefEdit = getPreferences(MODE_PRIVATE).edit();
                    if(remembermeCheckBox.isChecked()){
                        prefEdit.putString("USERNAME", usernameEdit.getText().toString());
                        prefEdit.putString("PASSWORD", passwordEdit.getText().toString());
                    }
                    else{
                        prefEdit.clear();
                    }
                    prefEdit.putBoolean("REMEMBER_ME", remembermeCheckBox.isChecked());
                    prefEdit.commit();

                    startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                }*/
                AdminContract.AdminDbHelper AdminDbHelper = new AdminContract.AdminDbHelper(LoginActivity.this);
                SQLiteDatabase db = AdminDbHelper.getReadableDatabase();

                Cursor cursor = db.query(
                        AdminContract.AdminEntry.TABLE_NAME,
                        new String[] {AdminContract.AdminEntry._ID},
                        String.format("%s = ? AND %s = ?", AdminContract.AdminEntry.COLUMN_NAME_USERNAME, AdminContract.AdminEntry.COLUMN_NAME_PASSWORD),
                        new String[] {usernameEdit.getText().toString(), passwordEdit.getText().toString()},
                        null,
                        null,
                        null
                );
                if(cursor.getCount() > 0){
                    cursor.moveToFirst();
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    intent.putExtra("ID", cursor.getString(cursor.getColumnIndex(AdminContract.AdminEntry._ID)));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Masukkan nama pengguna dan kata sandi yang benar", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
