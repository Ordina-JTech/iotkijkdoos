package nl.ordina.kijkdoos.view.control;

import butterknife.OnCheckedChanged;
import nl.ordina.kijkdoos.R;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public class ControlLightFragment extends AbstractControlFragment {

    @Override
    protected String getTitle() {
        return "Bedien lamp";
    }

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_ligt_component;
    }

    @OnCheckedChanged(R.id.switchLight)
    public void onSwitchChanged() {
        if (getComponentChangedListener() != null) {
            getComponentChangedListener().onComponentChanged(getComponent(), null);
        }
    }

}
