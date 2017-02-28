package nl.ordina.kijkdoos.view.control;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import nl.ordina.kijkdoos.R;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public class ControlLightFragment extends Fragment {

    public static final String ARGUMENT_COMPONENT = "COMPONENT";

    private OnSwitchChangedListener activity;
    private ControlViewBoxActivity.Component component;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final Bundle arguments = getArguments();
        component = (ControlViewBoxActivity.Component) arguments.getSerializable(ARGUMENT_COMPONENT);

        try {
            activity = (OnSwitchChangedListener) getActivity();
        } catch (ClassCastException e) {
            Log.w(ControlLightFragment.class.getSimpleName(), "Activity should implement OnSwitchChangedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.control_fragment, container, false);
        ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @OnCheckedChanged(R.id.switchLight)
    public void onSwitchChanged() {
        if (activity != null) {
            activity.onSwitchChanged(component);
        }
    }

    public interface OnSwitchChangedListener {
        void onSwitchChanged(ControlViewBoxActivity.Component component);
    }

}
