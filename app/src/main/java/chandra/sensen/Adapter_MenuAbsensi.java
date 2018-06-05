package chandra.sensen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter_MenuAbsensi extends RecyclerView.Adapter<Adapter_MenuAbsensi.MenuAbsensiViewHolder>{


    private ArrayList<Contract_Umat> absensi_list;
    private Context context;

    Adapter_MenuAbsensi(ArrayList<Contract_Umat> absensi_list, Context context){
        this.absensi_list = absensi_list;
        this.context = context;
    }

    @Override
    public Adapter_MenuAbsensi.MenuAbsensiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_data_umat, parent, false);
        return new Adapter_MenuAbsensi.MenuAbsensiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_MenuAbsensi.MenuAbsensiViewHolder holder, int position) {
        holder.nama_text.setText("ID Umat\t: " + absensi_list.get(position).getNama());
        holder.id_text.setText("Nama\t: " + absensi_list.get(position).getIdUmat());
    }

    @Override
    public int getItemCount() {
        return absensi_list.size();
    }

    class MenuAbsensiViewHolder extends RecyclerView.ViewHolder{
        TextView id_text;
        TextView nama_text;
        MenuAbsensiViewHolder(final View absensi_view){
            super(absensi_view);
            nama_text = absensi_view.findViewById(R.id.nama_text);
            id_text = absensi_view.findViewById(R.id.id_text);
            //SAAT CARD DIPILIH
            absensi_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //DIALOG BOX
                    LayoutInflater layoutInflater = LayoutInflater.from(view.getContext());
                    final View view2 = layoutInflater.inflate(R.layout.dialog_info_absensi, null, false);
                    final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setView(view2);
                    final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    //INIT
                    final int pos = getAdapterPosition();
                    TextView idumat_text = view2.findViewById(R.id.idumat_text);
                    TextView nama_text = view2.findViewById(R.id.nama_text);
                    TextView alamat_text = view2.findViewById(R.id.alamat_text);
                    TextView tgl_lahir_text = view2.findViewById(R.id.tgl_lahir_text);
                    ImageView foto_image = view2.findViewById(R.id.foto_image);

                    //SET SEMUA TEXTVIEW & IMAGEVIEW
                    idumat_text.setText(absensi_list.get(pos).getIdUmat());
                    nama_text.setText(absensi_list.get(pos).getNama());
                    alamat_text.setText(absensi_list.get(pos).getAlamat());
                    tgl_lahir_text.setText(absensi_list.get(pos).getTglLahir());
                    //KALO ADA FOTO
                    if(!absensi_list.get(pos).getFoto().equals("")){
                        //TODO: TAMBAH FOTO
                    }

                    //BUTTON UBAH
                    Button ubahButton = view2.findViewById(R.id.ubah_button);
                    ubahButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(view.getContext(), Activity_UbahUmat.class);
                            i.putExtra("IDUMAT", absensi_list.get(pos).getIdUmat());
                            i.putExtra("NAMA", absensi_list.get(pos).getNama());
                            i.putExtra("TGL_LAHIR", absensi_list.get(pos).getTglLahir());
                            i.putExtra("ALAMAT", absensi_list.get(pos).getAlamat());
                            context.startActivity(i);
                        }
                    });

                    //BUTTON KEMBALI
                    Button kembaliButton = view2.findViewById(R.id.kembali_button);
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
