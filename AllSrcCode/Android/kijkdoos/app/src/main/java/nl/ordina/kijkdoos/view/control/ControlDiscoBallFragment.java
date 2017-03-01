package nl.ordina.kijkdoos.view.control;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import nl.ordina.kijkdoos.R;
import uz.shift.colorpicker.LineColorPicker;
import uz.shift.colorpicker.OnColorChangedListener;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public class ControlDiscoBallFragment extends AbstractControlFragment implements OnColorChangedListener {

    @BindView(R.id.colorSlider)
    public LineColorPicker colorSlider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        colorSlider.setColors(new int[] {Color.RED, Color.GREEN, Color.BLUE});
        colorSlider.setOnColorChangedListener(this);
        return view;
    }

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_disco_ball_component;
    }

    @Override
    public void onColorChanged(int position) {
        getComponentChangedListener().onComponentChanged(getComponent(), Color.RED);
    }
}
