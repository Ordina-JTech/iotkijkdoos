package nl.ordina.kijkdoos.view.control;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import butterknife.BindView;
import nl.ordina.kijkdoos.R;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public class ControlTelevisionFragment extends AbstractControlFragment implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.rotationSlider)
    public SeekBar rotationSlider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        rotationSlider.setMax(179);
        rotationSlider.setOnSeekBarChangeListener(this);

        return view;
    }

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_television_component;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        getComponentChangedListener().onComponentChanged(getComponent(), progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
