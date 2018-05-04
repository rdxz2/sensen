package chandra.sensen;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TambahAdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_admin);

        final TextView usernameText = (TextView) findViewById(R.id.username_text);
        final TextView passwordText = (TextView) findViewById(R.id.password_text);

        Button tambahButton = (Button) findViewById(R.id.tambah_button);
        tambahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameText.setVisibility(View.GONE);
                passwordText.setVisibility(View.GONE);

                EditText usernameEdit = (EditText) findViewById(R.id.username_edit);
                EditText passwordEdit = (EditText) findViewById(R.id.password_edit);

                //DATA BENER
                if((usernameEdit.getText().toString().length()>=5 && usernameEdit.getText().toString().length()<20) && (passwordEdit.getText().toString().length()>=5)){
                    //MASUKIN DATA KE DB
                    AdminContract.AdminDbHelper AdminDbHelper = new AdminContract.AdminDbHelper(TambahAdminActivity.this);
                    SQLiteDatabase db = AdminDbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(AdminContract.AdminEntry.COLUMN_NAME_USERNAME, usernameEdit.getText().toString());
                    values.put(AdminContract.AdminEntry.COLUMN_NAME_PASSWORD, passwordEdit.getText().toString());
                    long newRowId = db.insert(AdminContract.AdminEntry.TABLE_NAME, null, values);
                    Toast.makeText(TambahAdminActivity.this, "Admin " + usernameEdit.getText().toString() + " telah ditambahkan", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    //USERNAME SALAH
                    if(!(usernameEdit.getText().toString().length()>=5 && usernameEdit.getText().toString().length()<20) || usernameEdit.getText().toString().length()==0){
                        usernameText.setVisibility(View.VISIBLE);
                    }
                    //PASSWORD SALAH
                    if(passwordEdit.getText().toString().length()<=5 || passwordEdit.getText().toString().length()==0){
                        passwordText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        Button batalButton = (Button) findViewById(R.id.batal_button);
        batalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
