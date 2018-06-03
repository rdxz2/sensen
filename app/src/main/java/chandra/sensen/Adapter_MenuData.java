package chandra.sensen;

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

    public Adapter_MenuData(ArrayList<Contract_Umat> umat_list){
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
            nama_text = (TextView) umat_view.findViewById(R.id.nama_text);
            id_text = (TextView) umat_view.findViewById(R.id.id_text);
            umat_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
