package nl.ordina.kijkdoos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LightFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LightFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LightFragment newInstance(String param1, String param2) {
        LightFragment fragment = new LightFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_light, container, false);
        registerAllBtnOnClickListeners(view);

        return view;
    }

    private void registerAllBtnOnClickListeners(View v) {
        ToggleButton idTBLampLeftOn = (ToggleButton)v.findViewById(R.id.idTBLampLeftOn);
        idTBLampLeftOn.setOnClickListener(this);

        ToggleButton idTBLampLeftBlinkOn = (ToggleButton)v.findViewById(R.id.idTBLampLeftBlinkOn);
        idTBLampLeftBlinkOn.setOnClickListener(this);


        ToggleButton idTBLampRightOn = (ToggleButton)v.findViewById(R.id.idTBLampRightOn);
        idTBLampRightOn.setOnClickListener(this);

        ToggleButton idTBLampRightBlinkOn = (ToggleButton)v.findViewById(R.id.idTBLampRightBlinkOn);
        idTBLampRightBlinkOn.setOnClickListener(this);


        ToggleButton idTBLampColorOn = (ToggleButton)v.findViewById(R.id.idTBLampColorOn);
        idTBLampColorOn.setOnClickListener(this);

        ToggleButton idTBLampColorBlinkOn = (ToggleButton)v.findViewById(R.id.idTBLampColorBlinkOn);
        idTBLampColorBlinkOn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ToggleButton tb = (ToggleButton)v.findViewById(v.getId());
        Toast.makeText(v.getContext(), tb.getText(), Toast.LENGTH_SHORT).show();

        communicateToArduino(tb);
    }

    private void communicateToArduino(ToggleButton tb) {
            switch (tb.getId()){
                case R.id.idTBLampLeftOn:
                case R.id.idTBLampLeftBlinkOn:
                case R.id.idTBLampRightOn:
                case R.id.idTBLampRightBlinkOn:
                case R.id.idTBLampColorOn:
                case R.id.idTBLampColorBlinkOn:
//                    tb.setChecked( tb.isChecked() ?  false : true);
                break;
        }
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
