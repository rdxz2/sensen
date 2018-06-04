package chandra.sensen;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Adapter_MenuData extends RecyclerView.Adapter<Adapter_MenuData.MenuDataViewHolder>{

    private ArrayList<Contract_Umat> umat_list;

    Adapter_MenuData(ArrayList<Contract_Umat> umat_list){
        this.umat_list = umat_list;
    }

    @Override
    public MenuDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_data_umat, parent, false);
        return new MenuDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuDataViewHolder holder, int position) {
        holder.nama_text.setText(umat_list.get(position).getNama());
        holder.id_text.setText(umat_list.get(position).getIdUmat());
    }

    @Override
    public int getItemCount() {
        return umat_list.size();
    }

    class MenuDataViewHolder extends RecyclerView.ViewHolder{
        TextView id_text;
        TextView nama_text;
        MenuDataViewHolder(final View umat_view){
            super(umat_view);
            nama_text = umat_view.findViewById(R.id.nama_text);
            id_text = umat_view.findViewById(R.id.id_text);
            umat_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //INIT
                    int pos = getAdapterPosition();
                    TextView idumat_text = view.findViewById(R.id.idumat_text);
                    TextView nama_text = view.findViewById(R.id.nama_text);
                    TextView alamat_text = view.findViewById(R.id.alamat_text);
                    TextView tgl_lahir_text = view.findViewById(R.id.tgl_lahir_text);

                    //DIALOG BOX
                    LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                    @SuppressLint("InflateParams") final View view2 = layoutInflater.inflate(R.layout.dialog_info_umat, null, false);
                    final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setView(view2);
                    final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    //SET SEMUA TEXTVIEW
                    idumat_text.setText(umat_list.get(pos).getIdUmat());
                    nama_text.setText(umat_list.get(pos).getNama());
                    alamat_text.setText(umat_list.get(pos).getAlamat());
                    tgl_lahir_text.setText(umat_list.get(pos).getTglLahir());

                    //BUTTON KEMBALI
                    Button kembaliButton = view.findViewById(R.id.kembali_button);
                    kembaliButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.hide();
                        }
                    });
                }
            });
        }
    }
}
