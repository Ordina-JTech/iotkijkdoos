package nl.ordina.kijkdoos.view.control;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import lombok.Getter;
import nl.ordina.kijkdoos.R;

/**
 * Created by coenhoutman on 28/02/2017.
 */

public abstract class AbstractControlFragment extends Fragment {

    public static final String ARGUMENT_COMPONENT = "COMPONENT";

    @Getter
    private OnComponentChangedListener componentChangedListener;

    @Getter
    private ControlViewBoxActivity.Component component;

    @BindView(R.id.tvTitle)
    public TextView tvTitle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        final Bundle arguments = getArguments();
        component = (ControlViewBoxActivity.Component) arguments.getSerializable(ARGUMENT_COMPONENT);

        try {
            componentChangedListener = (OnComponentChangedListener) getActivity();
        } catch (ClassCastException e) {
            Log.w(AbstractControlFragment.class.getSimpleName(), "Activity should implement OnComponentChangedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.control_fragment, container, false);

        final ViewStub stub = ButterKnife.findById(inflatedView, R.id.stubControl);
        stub.setLayoutResource(getControlLayoutId());
        stub.inflate();
        ButterKnife.bind(this, inflatedView);

        tvTitle.setText(getTitle());

        return inflatedView;
    }

    protected abstract String getTitle();

    protected abstract int getControlLayoutId();

    public interface OnComponentChangedListener {
        void onComponentChanged(ControlViewBoxActivity.Component component, Object value);
    }

}
