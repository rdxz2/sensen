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
        expandedListTextView.setTypeface(Typeface.create("casual", Typeface.NORMAL));
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
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group_data_absensi, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.tanggal_absensi_text);
        listTitleTextView.setTypeface(Typeface.create("casual", Typeface.BOLD));
        listTitleTextView.setText("Tanggal: " + listTitle);
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
