package nl.ordina.kijkdoos.view.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import nl.ordina.kijkdoos.R;

import static nl.ordina.kijkdoos.view.control.ControlViewBoxActivity.Component.LAMP_RIGHT;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public class ControlChallengesFragment extends AbstractControlFragment {

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_challenges_component;
    }

    @OnClick({R.id.btGradientChallenge, R.id.btSpecialEffect})
    public void onClick(Button button) {
        if (getComponentChangedListener() != null) {
            getComponentChangedListener().onComponentChanged(getComponent(), button.getId());
        }
    }

}
