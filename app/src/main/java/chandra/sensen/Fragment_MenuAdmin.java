package chandra.sensen;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_MenuAdmin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_MenuAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_MenuAdmin extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_MenuAdmin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_MenuAdmin.
     */
    public static Fragment_MenuAdmin newInstance(String param1, String param2) {
        Fragment_MenuAdmin fragment = new Fragment_MenuAdmin();
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
                startActivity(new Intent(getActivity(), Activity_TambahAdmin.class));
            }
        });

//        cursor.close();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        //SAMBUNG KE DB
        Contract_Admin.AdminDbHelper AdminDbHelper = new Contract_Admin.AdminDbHelper(getActivity());
        final SQLiteDatabase db = AdminDbHelper.getReadableDatabase();

        //SET CURSOR
        String[] projection = {
                Contract_Admin.AdminEntry._ID,
                Contract_Admin.AdminEntry.COLUMN_NAME_USERNAME,
                Contract_Admin.AdminEntry.COLUMN_NAME_PASSWORD
        };
        final Cursor cursor = db.query(Contract_Admin.AdminEntry.TABLE_NAME, projection, null, null, null, null, null);

        //SET KONTEN LISTVIEW
        ArrayList<String> str = new ArrayList();
        cursor.moveToFirst();
        do {
            str.add(String.format("%d - %s",
                    cursor.getInt(cursor.getColumnIndex(Contract_Admin.AdminEntry._ID)),
                    cursor.getString(cursor.getColumnIndex(Contract_Admin.AdminEntry.COLUMN_NAME_USERNAME))
            ));
        } while (cursor.moveToNext());

        //ARRAY ADAPTER
        ArrayAdapter<String> strList = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1);
        for(int x=0; x<str.size(); x++) strList.add(str.get(x));
        ListView adminList = (ListView) getActivity().findViewById(R.id.admin_list);

        //SAAT ITEM DIKLIK
        adminList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //DIALOG BOX
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                final View view2 = layoutInflater.inflate(R.layout.dialog_edit_admin, null, false);
                final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(view2);
                final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                //SET USERNAME DI EDITTEXT
                cursor.moveToPosition(position);
                EditText usernameEdit = (EditText) view2.findViewById(R.id.username_edit);
                usernameEdit.setText(cursor.getString(cursor.getColumnIndex(Contract_Admin.AdminEntry.COLUMN_NAME_USERNAME)));

                //BUTTON UBAH
                Button ubahButton = (Button) view2.findViewById(R.id.ubah_button);
                ubahButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText passwordlamaEdit = (EditText) view2.findViewById(R.id.password_lama_edit);
                        EditText passwordbaruEdit = (EditText) view2.findViewById(R.id.password_baru_edit);
                        //PASSWORD BENER
                        if(passwordlamaEdit.getText().toString().equals(cursor.getString(cursor.getColumnIndex(Contract_Admin.AdminEntry.COLUMN_NAME_PASSWORD)))){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(Contract_Admin.AdminEntry.COLUMN_NAME_PASSWORD, passwordbaruEdit.getText().toString());
                            String selection = String.format("%s = ?", Contract_Admin.AdminEntry._ID);
                            String[] selectionArgs = {Integer.toString(cursor.getInt(cursor.getColumnIndex(Contract_Admin.AdminEntry._ID)))};
                            int count = db.update(Contract_Admin.AdminEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                            Toast.makeText(getActivity(), "Kata sandi telah berhasil diubah", Toast.LENGTH_SHORT).show();
                            onResume();
                            alertDialog.hide();
                        }
                        //PASSWORD SALAH
                        else{
                            Toast.makeText(getActivity(), "Masukkan kata sandi yang benar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //BUTTON BATAL
                Button batalButton = (Button) view2.findViewById(R.id.batal_button);
                batalButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.hide();
                    }
                });
            }
        });
        //SAAT ITEM DIKLIK PANJANG
        adminList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //DIALOG BOX
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                final View view2 = layoutInflater.inflate(R.layout.dialog_hapus_admin, null, false);
                final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(view2);
                final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                //SET USERNAME DI EDITTEXT
                cursor.moveToPosition(i);
                TextView passwordText = (TextView) view2.findViewById(R.id.confirm_text);
                passwordText.setText("Apakah Anda yakin ingin menghapus Admin '" + cursor.getString(cursor.getColumnIndex(Contract_Admin.AdminEntry.COLUMN_NAME_USERNAME)) + "'?");

                //BUTTON HAPUS
                Button hapusButton = (Button) view2.findViewById(R.id.hapus_button);
                hapusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText passwordEdit = (EditText) view2.findViewById(R.id.password_edit);
                        //PASSWORD BENER
                        if(passwordEdit.getText().toString().equals(cursor.getString(cursor.getColumnIndex(Contract_Admin.AdminEntry.COLUMN_NAME_PASSWORD)))){
                            //TODO: hapus dari database
                            Toast.makeText(getActivity(), "Admin '" + cursor.getString(cursor.getColumnIndex(Contract_Admin.AdminEntry.COLUMN_NAME_USERNAME)) + "' telah dihapus", Toast.LENGTH_SHORT).show();
                            alertDialog.hide();
                            onResume();
                        }
                        //PASSWORD SALAH
                        else{
                            Toast.makeText(getActivity(), "Masukkan kata sandi yang benar", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //BUTTON BATAL
                Button batalButton = (Button) view2.findViewById(R.id.batal_button);
                batalButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.hide();
                    }
                });
                return false;
            }
        });

        //TAMPILIN LISTVIEW
        adminList.setAdapter(strList);
    }

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
        void onFragmentInteraction(Uri uri);
    }
}
