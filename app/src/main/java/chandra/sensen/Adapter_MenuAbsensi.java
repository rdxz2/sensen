package chandra.sensen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter_MenuAbsensi extends BaseExpandableListAdapter {

    private Context context;
    private List<String> tanggal_list;
    private HashMap<String, List<String>> absensi_list;

    //CONSTRUCTOR
    public Adapter_MenuAbsensi(Context context, List<String> tanggal_list, HashMap<String, List<String>> absensi_list) {
        this.context = context;
        this.tanggal_list = tanggal_list;
        this.absensi_list = absensi_list;
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

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,View convertView, ViewGroup parent) {
        final String tanggal_absen = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group_data_absensi, null);
        }
        TextView tanggal_absenText = (TextView) convertView.findViewById(R.id.tanggal_absensi_text);
        tanggal_absenText.setTypeface(Typeface.create("casual", Typeface.BOLD));
        tanggal_absenText.setText("Tanggal: " + tanggal_absen);
        Button exportButton1 = (Button) convertView.findViewById(R.id.export_button1);
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
                        for(int a = 0; a < absensi_list.size(); a++){
                            if(absensi_list.containsKey(tanggal_absen)){
                                Toast.makeText(context, absensi_list.get(tanggal_absen).toString(), Toast.LENGTH_SHORT).show();
                                nama_file = "ABSENSI TANGGAL " + tanggal_absen;
                                break;
                            }
                        }
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

    String nama_file = "";

    //CLASS LISTING UMAT -> BUAT AMBIL SELURUH DATA UMAT
    class listingUmat extends AsyncTask<String, Void, Boolean> {
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
            Service_WebService service = new Service_WebService("http://absenpadum.top/TampilData.php","GET","");
            String jsonString = service.responseBody;
            try {
                JSONArray umatArray = new JSONArray(jsonString);
                for (int i = 0; i<umatArray.length(); i++){
                    JSONObject umatObject = umatArray.getJSONObject(i);

                }
            }
            catch (JSONException e){e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();

            try (
                    Writer writer = new BufferedWriter(new FileWriter("./" + nama_file + ".csv"));
                    CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            ) {
                String[] headerRecord = {"IDAbsen", "IDUmat", "Nama"};
                csvWriter.writeNext(headerRecord);
                for(int a=0; a<)
                    csvWriter.writeNext(new String[]{"Sundar Pichai ♥", "sundar.pichai@gmail.com", "+1-1111111111", "India"});
                    csvWriter.writeNext(new String[]{"Satya Nadella", "satya.nadella@outlook.com", "+1-1111111112", "India"});
            }
            catch (IOException e){e.printStackTrace();}
        }
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
