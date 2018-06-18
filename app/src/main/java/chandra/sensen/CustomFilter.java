package chandra.sensen;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter {

    Adapter_MenuUmat adapter;
    ArrayList<Contract_Umat> filterList;

    public CustomFilter(ArrayList<Contract_Umat> filterList,Adapter_MenuUmat adapter){
        this.adapter=adapter;
        this.filterList=filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<Contract_Umat> filteredPlayers=new ArrayList<>();

            for (int i=0;i<filterList.size();i++){
                //CHECK
                if(filterList.get(i).getNama().toUpperCase().contains(constraint) || filterList.get(i).getIdUmat().toUpperCase().contains(constraint)){
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }
            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }
        else{
            results.count=filterList.size();
            results.values=filterList;
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
