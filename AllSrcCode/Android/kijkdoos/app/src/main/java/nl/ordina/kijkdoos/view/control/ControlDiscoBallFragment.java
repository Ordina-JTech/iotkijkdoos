package nl.ordina.kijkdoos.view.control;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.annimon.stream.Stream;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import lombok.Getter;
import nl.ordina.kijkdoos.R;
import uz.shift.colorpicker.LineColorPicker;
import uz.shift.colorpicker.OnColorChangedListener;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public class ControlDiscoBallFragment extends AbstractControlFragment implements OnColorChangedListener {

    @BindView(R.id.colorSlider)
    public LineColorPicker colorSlider;

    public enum DiscoBallColor {
        OFF(Color.WHITE, "c0"),
        RED(Color.RED, "c1"),
        YELLOW(Color.YELLOW, "c2"),
        GREEN(Color.GREEN, "c3"),
        AQUA(Color.CYAN, "c4"),
        BLUE(Color.BLUE, "c5"),
        PURPLE(Color.MAGENTA, "c6");

        @Getter
        private final int color;

        @Getter
        private final String message;

        DiscoBallColor(int color, String message) {
            this.color = color;
            this.message = message;
        }

        public static int[] getColors() {
            return Stream.of(values()).mapToInt(DiscoBallColor::getColor).toArray();
        }

        public static DiscoBallColor from(int color) {
            return Stream.of(values()).filter(value ->  value.getColor() == color).single();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        colorSlider.setColors(DiscoBallColor.getColors());
        colorSlider.setOnColorChangedListener(this);
        return view;
    }

    @Override
    protected String getTitle() {
        return "Bedien disco bal";
    }

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_disco_ball_component;
    }

    @Override
    public void onColorChanged(int color) {
        getComponentChangedListener().onComponentChanged(getComponent(), DiscoBallColor.from(color));
    }
}
