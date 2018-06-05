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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Fragment_MenuAbsensi extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_MenuAbsensi() {
        // Required empty public constructor
    }

    public static Fragment_MenuAbsensi newInstance(String param1, String param2) {
        Fragment_MenuAbsensi fragment = new Fragment_MenuAbsensi();
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
                String myFormat = "yyyy-MM-dd";
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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
