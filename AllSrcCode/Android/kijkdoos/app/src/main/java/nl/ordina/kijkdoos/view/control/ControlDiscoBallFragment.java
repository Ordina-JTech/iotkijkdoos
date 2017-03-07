package nl.ordina.kijkdoos.view.control;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.annimon.stream.Collectors;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.larswerkman.holocolorpicker.ColorPicker;

import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import lombok.Getter;
import nl.ordina.kijkdoos.R;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public class ControlDiscoBallFragment extends AbstractControlFragment implements ColorPicker.OnColorChangedListener {

    public enum DiscoBallColor {
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

        public static DiscoBallColor getNearest(@ColorInt int color) {
            final Set<Map.Entry<DiscoBallColor, Integer>> mapOfDistancesToKnownColors = Stream.of(values()).collect(Collectors.
                    toMap(discoBallColor -> discoBallColor, discoBallColor -> getDistance(discoBallColor.getColor(), color))).entrySet();
            final Optional<DiscoBallColor> discoBallColorOptional = Stream.of(mapOfDistancesToKnownColors).min((o1, o2) -> o1.getValue().compareTo(o2.getValue())).map(Map.Entry::getKey);

            return discoBallColorOptional.get();
        }

        private static int getDistance(int color1, int color2) {
            final int color1Red = (color1 >> 16) & 0xFF;
            final int color1Green = (color1 >> 8) & 0xFF;
            final int color1Blue = (color1) & 0xFF;

            final int color2Red = (color2 >> 16) & 0xFF;
            final int color2Green = (color2 >> 8) & 0xFF;
            final int color2Blue = (color2) & 0xFF;

            return (int) sqrt(pow(color2Red - color1Red, 2) + pow(color2Green - color1Green, 2) + pow(color2Blue - color1Blue, 2));
        }
    }

    @BindView(R.id.colorSlider)
    public ColorPicker colorSlider;

    @BindView(R.id.discoBallSwitch)
    public Switch discoBallSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);

        final int initialColor = DiscoBallColor.RED.getColor();
        colorSlider.setColor(initialColor);
        onColorChanged(initialColor);

        colorSlider.setShowOldCenterColor(false);
        colorSlider.setOnColorChangedListener(this);

        return view;
    }

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_disco_ball_component;
    }

    @Override
    public void onColorChanged(int color) {
        final DiscoBallColor nearestColor = DiscoBallColor.getNearest(color);

        getComponentChangedListener().onComponentChanged(getComponent(), nearestColor);
        discoBallSwitch.setChecked(true);

        changeSwitchColor(nearestColor.getColor());
    }

    @OnCheckedChanged(R.id.discoBallSwitch)
    public void onDiscoBallSwitched(boolean isChecked) {
        if (isChecked) {
            onColorChanged(colorSlider.getColor());
        } else {
            getComponentChangedListener().onComponentChanged(getComponent(), null);
            changeSwitchColor(Color.GRAY);
        }
    }

    protected void changeSwitchColor(int color) {
        final ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorSlider.getColor(), color);
        colorAnimator.setDuration(50);
        colorAnimator.addUpdateListener(animator -> {
            discoBallSwitch.getThumbDrawable().setColorFilter((int) animator.getAnimatedValue(), PorterDuff.Mode.MULTIPLY);
            discoBallSwitch.getTrackDrawable().setColorFilter((int) animator.getAnimatedValue(), PorterDuff.Mode.MULTIPLY);
        });

        colorAnimator.start();
    }
}
