package chandra.sensen;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuAbsensiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuAbsensiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuAbsensiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MenuAbsensiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuAbsensiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuAbsensiFragment newInstance(String param1, String param2) {
        MenuAbsensiFragment fragment = new MenuAbsensiFragment();
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
        View v = inflater.inflate(R.layout.fragment_menu_absensi, container, false);

        final Calendar calendarAwal = Calendar.getInstance();
        final Calendar calendarAkhir = Calendar.getInstance();

        //TANGGAL AWAL
        final EditText tanggalawalEdit= (EditText) v.findViewById(R.id.tanggal_awal_edit);
        final DatePickerDialog.OnDateSetListener dateAwal = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarAwal.set(Calendar.YEAR, year);
                calendarAwal.set(Calendar.MONTH, monthOfYear);
                calendarAwal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                tanggalawalEdit.setText(sdf.format(calendarAwal.getTime()));
            }
        };
        tanggalawalEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateAwal, calendarAwal.get(Calendar.YEAR), calendarAwal.get(Calendar.MONTH), calendarAwal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //TANGGAL AKHIR
        final EditText tanggalakhirEdit= (EditText) v.findViewById(R.id.tanggal_akhir_edit);
        final DatePickerDialog.OnDateSetListener dateAkhir = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarAkhir.set(Calendar.YEAR, year);
                calendarAkhir.set(Calendar.MONTH, monthOfYear);
                calendarAkhir.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                if(!calendarAwal.after(calendarAkhir)){
                    tanggalakhirEdit.setText(sdf.format(calendarAkhir.getTime()));
                }
                else {
                    Toast.makeText(getActivity(), "Tanggal awal harus sebelum tanggal akhir", Toast.LENGTH_SHORT).show();
                    tanggalakhirEdit.setText("");
                }
            }
        };
        tanggalakhirEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateAkhir, calendarAkhir.get(Calendar.YEAR), calendarAkhir.get(Calendar.MONTH), calendarAkhir.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //BUTTON CARI
        final EditText cariEdit = (EditText) v.findViewById(R.id.cari_edit);
        final Button cariButton = (Button) v.findViewById(R.id.cari_button);
        cariButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(cariEdit.getText().toString().equals("") && tanggalawalEdit.getText().toString().equals("") && tanggalakhirEdit.getText().toString().equals(""))){
                    //CARI

                }
                else{
                    Toast.makeText(getActivity(), "Isi terlebih dahulu data-data di atas", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
