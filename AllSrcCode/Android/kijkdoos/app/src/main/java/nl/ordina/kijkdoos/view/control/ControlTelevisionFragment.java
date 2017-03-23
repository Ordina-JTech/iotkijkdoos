package nl.ordina.kijkdoos.view.control;

import android.content.SharedPreferences;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final int sliderValue = getControlValueStore().getInt(ControlTelevisionFragment.class.getSimpleName(), 0);
        rotationSlider.setProgress(sliderValue);
    }

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_television_component;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (progress % 5 == 0 || progress % 8 == 0) {
            getComponentChangedListener().onComponentChanged(getComponent(), progress);
        }

        final SharedPreferences.Editor editor = getControlValueStore().edit();
        editor.putInt(ControlTelevisionFragment.class.getSimpleName(), progress);
        editor.apply();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
