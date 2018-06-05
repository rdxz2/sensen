package chandra.sensen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter_MenuAbsensi extends BaseExpandableListAdapter {

    private Context context;
    private List<String> tanggal_list;
    private HashMap<String, List<String>> absensi_list;

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
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_data_absensi, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.nama_text);
        expandedListTextView.setText(expandedListText);
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
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group_data_absensi, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.nama_text);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
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

//public class Adapter_MenuAbsensi extends RecyclerView.Adapter<Adapter_MenuAbsensi.MenuAbsensiViewHolder>{
//
//
//    private ArrayList<Contract_Absensi> absensi_list;
//    private Context context;
//
//    Adapter_MenuAbsensi(ArrayList<Contract_Absensi> absensi_list, Context context){
//        this.absensi_list = absensi_list;
//        this.context = context;
//    }
//
//    @Override
//    public Adapter_MenuAbsensi.MenuAbsensiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        View view = layoutInflater.inflate(R.layout.card_data_absensi, parent, false);
//        return new Adapter_MenuAbsensi.MenuAbsensiViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(Adapter_MenuAbsensi.MenuAbsensiViewHolder holder, int position) {
//        holder.tanggalabsen_text.setText("Daftar absensi tanggal : " + absensi_list.get(position).getTanggalAbsen());
//    }
//
//    @Override
//    public int getItemCount() {
//        return absensi_list.size();
//    }
//
//    void tampil_group(){
//
//    }
//
//    class MenuAbsensiViewHolder extends RecyclerView.ViewHolder{
//        TextView tanggalabsen_text;
//        MenuAbsensiViewHolder(final View absensi_view){
//            super(absensi_view);
//            tanggalabsen_text = absensi_view.findViewById(R.id.tanggalabsen_text);
//            //SAAT CARD DIPILIH
//            absensi_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //DIALOG BOX
//                    LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
//                    final View view2 = layoutInflater.inflate(R.layout.dialog_info_absensi, null, false);
//                    final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(view.getContext());
//                    alertDialogBuilder.setView(view2);
//                    final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//
//                    //INIT
//                    final int pos = getAdapterPosition();
//                    GridLayout absensiGrid = view2.findViewById(R.id.absensi_grid);
//                    TextView tanggal_absensi_text = view2.findViewById(R.id.tanggal_absensi_text);
//
//                    //SET SEMUA TEXTVIEW
//                    tanggal_absensi_text.setText(absensi_list.get(pos).getTanggalAbsen());
//                    for(int a=0; a<absensi_list.size(); a++){
//                        if(absensi_list.get(a).getTanggalAbsen().equals(absensi_list.get(pos).getTanggalAbsen())){
//                            TextView nama_text = new TextView(context);
//                            nama_text.setText(absensi_list.get(a).getNama());
//                            nama_text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                            absensiGrid.addView(nama_text);
//                        }
//                    }
//
//                    //BUTTON KEMBALI
//                    Button kembaliButton = view2.findViewById(R.id.kembali_button);
//                    kembaliButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            alertDialog.hide();
//                        }
//                    });
//                }
//            });
//        }
//    }
//}
