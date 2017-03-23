package nl.ordina.kijkdoos.view.control;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.triggertrap.seekarc.SeekArc;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.ordina.kijkdoos.R;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public class ControlTelevisionFragment extends AbstractControlFragment implements SeekArc.OnSeekArcChangeListener {

    @BindView(R.id.rotationSlider)
    public SeekArc rotationSlider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        rotationSlider.setOnSeekArcChangeListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final int sliderValue = getControlValueStore().getInt(ControlTelevisionFragment.class.getSimpleName(), 0);
        rotationSlider.setProgress(sliderValue);
    }

    @Override
    public void onDestroyView() {
        final SharedPreferences.Editor editor = getControlValueStore().edit();
        editor.putInt(ControlTelevisionFragment.class.getSimpleName(), rotationSlider.getProgress());
        editor.apply();

        super.onDestroyView();
    }

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_television_component;
    }


    @Override
    public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
        if (progress % 5 == 0 || progress % 8 == 0) {
            getComponentChangedListener().onComponentChanged(getComponent(), progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekArc seekArc) {

    }

    @Override
    public void onStopTrackingTouch(SeekArc seekArc) {

    }
}
