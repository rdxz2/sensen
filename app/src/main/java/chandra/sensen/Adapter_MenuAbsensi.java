package chandra.sensen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Adapter_MenuAbsensi extends BaseExpandableListAdapter {

    //INIT
    private Context context;
    private List<String> tanggal_list;
    private HashMap<String, List<String>> absensi_list;

    //CONSTRUCTOR
    public Adapter_MenuAbsensi(Context context, List<String> tanggal_list, HashMap<String, List<String>> absensi_list) {
        this.context = context;
        this.tanggal_list = tanggal_list;
        this.absensi_list = absensi_list;
        //SORT TANGGAL
        Collections.sort(tanggal_list, Collections.reverseOrder());
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.tanggal_list.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.tanggal_list.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    //INIT
    private String nama_file = "", tanggal_absen2 = "";
    private List<String> umat_list = new ArrayList<>();
    private List<Contract_Export> export_list = new ArrayList<>();

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,View convertView, ViewGroup parent) {
        //INIT
        final String tanggal_absen = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group_data_absensi, null);
        }
        //INIT
        TextView tanggal_absenText = convertView.findViewById(R.id.tanggal_absensi_text);
        tanggal_absenText.setTypeface(Typeface.create("casual", Typeface.BOLD));
        tanggal_absenText.setText("Tanggal: " + tanggal_absen);
        //TOMBOL EXPORT
        Button exportButton1 = convertView.findViewById(R.id.export_button1);
        exportButton1.setFocusable(false);
        exportButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DIALOG BOX
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                final View view2 = layoutInflater.inflate(R.layout.dialog_export_absensi, null, false);
                final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                alertDialogBuilder.setView(view2);
                final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                //INIT
                TextView exportText = view2.findViewById(R.id.export_text);
                exportText.setText(String.format("Apakah Anda yakin ingin meng-export data pada tanggal %s ?", tanggal_absen));
                //BUTTON EXPORT
                Button exportButton2 = view2.findViewById(R.id.export_button2);
                exportButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for(String key : absensi_list.keySet()){
                            if(key.equals(tanggal_absen)){
                                tanggal_absen2 = tanggal_absen;
                                nama_file = "ABSENSI TANGGAL " + tanggal_absen;
                                umat_list = absensi_list.get(tanggal_absen);
                                new export_absensi().execute();
                                break;
                            }
                        }
                        alertDialog.hide();
                    }
                });
                //BUTTON KEMBALI
                Button batalButton = view2.findViewById(R.id.batal_button);
                batalButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.hide();
                    }
                });
            }
        });
        return convertView;
    }

    //CLASS EXPORT ABSENSI -> BUAT EXPORT DATA ABSENSI TANGGAL YANG DIPILIH KE FORMAT CSV
    class export_absensi extends AsyncTask<String, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //TAMPILIN PROGRESS DIALOG
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Meng-export data absensi");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //WEB SERVICE
            Service_WebService service = new Service_WebService("http://absenpadum.top/DataAbsensi.php","GET","");
            String jsonString = service.responseBody;
            try {
                JSONArray exportArray = new JSONArray(jsonString);
                for (int i = 0; i<exportArray.length(); i++){
                    //DAPETIN DATA ABSENSI
                    JSONObject exportObject = exportArray.getJSONObject(i);
                    if(umat_list.contains(exportObject.getString("Nama")) && exportObject.getString("Tanggal_absen").equals(tanggal_absen2)) export_list.add(new Contract_Export(exportObject.getString("IDAbsen"), exportObject.getString("IDUmat"), exportObject.getString("Nama")));
                }
            }
            catch (JSONException e){e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            //REQUEST STORAGE PERMISSION
            final int REQUEST = 112;
            if (Build.VERSION.SDK_INT >= 23) {
                String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (!hasPermissions(context, PERMISSIONS)) {
                    ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, REQUEST );
                } else {
                    export_final();
                }
            } else {
                export_final();
            }
        }
    }

    private void export_final(){
        //WRITE DATA KE FORMAT CSV
        final String filename = "/" + nama_file + ".csv";
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {return;}
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
        try {
            if(file.exists()) file.delete();
            file.createNewFile();
            //INIT
            String COMMA_DELIMITER = ",";
            String NEW_LINE_SEPARATOR = "\n";
            String FILE_HEADER = "IDAbsen, IDmat, Nama";
            //MASUKIN DATA" OBJEK KE LIST BARU
//            for(int a = 0 ; a < export_list.size(); a++){
//                List export = new ArrayList();
//                export.add(export_list.get(a));
//            }
            FileWriter fileWriter = null;
            try {
                //MULAI TULIS ISI DARI FILE CSV
                fileWriter = new FileWriter(file);
                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
                //TULIS SETIAP OBJEK
                for (Contract_Export export_lists : export_list) {
                    fileWriter.append(String.valueOf(export_lists.getIdabsen()));
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(export_lists.getIdUmat());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(export_lists.getNama());
                    fileWriter.append(NEW_LINE_SEPARATOR);
                }
                Toast.makeText(context, "Data absensi tanggal " + tanggal_absen2 + " telah berhasil di-export.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Terjadi kesalahan saat meng-export data.", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e){e.printStackTrace();}
    }

    //CEK APAKAH SUDAH ADA PERMISSION
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.absensi_list.get(this.tanggal_list.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String nama_umat_absen = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_data_absensi, null);
        }
        TextView nama_umat_absenView = (TextView) convertView.findViewById(R.id.nama_text);
        nama_umat_absenView.setTypeface(Typeface.create("casual", Typeface.NORMAL));
        nama_umat_absenView.setText(nama_umat_absen);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.absensi_list.get(this.tanggal_list.get(listPosition)).size();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
