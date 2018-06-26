package chandra.sensen;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_TambahAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_admin);
        //INIT
        final TextView usernameText = findViewById(R.id.username_text);
        final TextView passwordText = findViewById(R.id.password_text);
        //BUTTON TAMBAH
        Button tambahButton = findViewById(R.id.tambah_button);
        tambahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //INIT
                usernameText.setVisibility(View.GONE);
                passwordText.setVisibility(View.GONE);
                EditText usernameEdit = findViewById(R.id.username_edit);
                EditText passwordEdit = findViewById(R.id.password_edit);
                boolean bener = true;
                //USERNAME SALAH
                if(!(usernameEdit.getText().toString().length()>=5 && usernameEdit.getText().toString().length()<20) || usernameEdit.getText().toString().length()==0){
                    usernameText.setVisibility(View.VISIBLE);
                    bener = false;
                }
                //PASSWORD SALAH
                if(passwordEdit.getText().toString().length()<=5 || passwordEdit.getText().toString().length()==0){
                    passwordText.setVisibility(View.VISIBLE);
                    bener = false;
                }
                //DATA BENER -> MASUKIN DATA KE DB
                if(bener){
                    //INIT
                    Contract_Admin.AdminDbHelper AdminDbHelper = new Contract_Admin.AdminDbHelper(Activity_TambahAdmin.this);
                    SQLiteDatabase db = AdminDbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //MASUKIN VALUE
                    values.put(Contract_Admin.AdminEntry.COLUMN_NAME_USERNAME, usernameEdit.getText().toString());
                    values.put(Contract_Admin.AdminEntry.COLUMN_NAME_PASSWORD, passwordEdit.getText().toString());
                    //EXECUTE
                    long newRowId = db.insert(Contract_Admin.AdminEntry.TABLE_NAME, null, values);
                    Toast.makeText(Activity_TambahAdmin.this, "Admin '" + usernameEdit.getText().toString() + "' telah ditambahkan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        //BUTTON BATAL
        Button batalButton = findViewById(R.id.batal_button);
        batalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
