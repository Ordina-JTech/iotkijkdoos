package nl.ordina.kijkdoos.view.control;

import butterknife.OnCheckedChanged;
import nl.ordina.kijkdoos.R;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public class ControlLightFragment extends AbstractControlFragment {

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_light_component;
    }

    @OnCheckedChanged(R.id.switchLight)
    public void onSwitchChanged() {
        if (getComponentChangedListener() != null) {
            getComponentChangedListener().onComponentChanged(getComponent(), null);
        }
    }

}
