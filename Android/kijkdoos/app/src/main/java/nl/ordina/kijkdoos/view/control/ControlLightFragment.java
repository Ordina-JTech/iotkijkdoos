package nl.ordina.kijkdoos.view.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import nl.ordina.kijkdoos.R;

import static nl.ordina.kijkdoos.view.control.ControlViewBoxActivity.Component.LAMP_RIGHT;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public class ControlLightFragment extends AbstractControlFragment {

    @BindView(R.id.ivComponentControl)
    public ImageView componentControl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        if (getComponent() == LAMP_RIGHT) {
            componentControl.setImageResource(R.drawable.lamp_right);
        }
        return view;
    }

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_light_component;
    }

    @OnCheckedChanged(R.id.switchLight)
    public void onSwitchChanged(boolean switchStatus) {
        if (getComponentChangedListener() != null) {
            getComponentChangedListener().onComponentChanged(getComponent(), switchStatus);
        }
    }

}
