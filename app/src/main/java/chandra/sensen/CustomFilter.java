package chandra.sensen;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    //INIT
    Adapter_MenuUmat adapter;
    ArrayList<Contract_Umat> umat_filter;

    //CONSTRUCTOR
    public CustomFilter(ArrayList<Contract_Umat> umat_filter, Adapter_MenuUmat adapter){
        this.adapter = adapter;
        this.umat_filter = umat_filter;
    }

    //FILTERING DATA (SESUAI INPUT USER)
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        //INIT
        FilterResults results = new FilterResults();
        //CEK INPUTAN USER
        if(constraint != null && constraint.length() > 0){
            //UBAH KE UPPERCASE
            constraint = constraint.toString().toUpperCase();
            ArrayList<Contract_Umat> umat_filtered = new ArrayList<>();
            //SEARCH DI umat_filter
            for (int i = 0; i < umat_filter.size(); i++){
                //KALO INPUTAN USER ADA DI NAMA/ID
                if(umat_filter.get(i).getNama().toUpperCase().contains(constraint) || umat_filter.get(i).getIdUmat().toUpperCase().contains(constraint) || umat_filter.get(i).getTglLahir().toUpperCase().contains(constraint)){
                    //MASUKIN KE umat_filter
                    umat_filtered.add(umat_filter.get(i));
                }
            }
            //SET RESULT
            results.count = umat_filtered.size();
            results.values = umat_filtered;
        }
        else{
            //SET RESULT (KOSONG)
            results.count = umat_filter.size();
            results.values = umat_filter;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.umat_list = (ArrayList<Contract_Umat>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
