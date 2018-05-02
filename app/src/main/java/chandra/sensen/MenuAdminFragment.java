package chandra.sensen;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuAdminFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuAdminFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MenuAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuAdminFragment newInstance(String param1, String param2) {
        MenuAdminFragment fragment = new MenuAdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu_admin, container, false);

        //FAB
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.tambah_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TambahAdminActivity.class));
            }
        });

        //SAMBUNG KE DB
        AdminContract.AdminDbHelper AdminDbHelper = new AdminContract.AdminDbHelper(getActivity());
        SQLiteDatabase db = AdminDbHelper.getReadableDatabase();

        String[] projection = {
                AdminContract.AdminEntry._ID,
                AdminContract.AdminEntry.COLUMN_NAME_USERNAME,
        };
        //ARRAY LIST
        ArrayList<String> str = new ArrayList();
        Cursor cursor = db.query(AdminContract.AdminEntry.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();
        do {
            str.add(String.format("%d: %s",
                    cursor.getInt(cursor.getColumnIndex(AdminContract.AdminEntry._ID)),
                    cursor.getInt(cursor.getColumnIndex(AdminContract.AdminEntry.COLUMN_NAME_USERNAME))
            ));
        } while (cursor.moveToNext());
        //ARRAY ADAPTER
        ArrayAdapter<String> strList = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        for(int x=0; x<str.size(); x++) strList.add(str.get(x));
        ListView adminList = (ListView) v.findViewById(R.id.admin_list);
        //TAMPILIN
        adminList.setAdapter(strList);
        cursor.close();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
