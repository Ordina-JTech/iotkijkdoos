package nl.ordina.kijkdoos.view.control;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Consumer;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.AccessLevel;
import lombok.Getter;
import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;

import static nl.ordina.kijkdoos.ViewBoxApplication.getViewBoxApplication;
import static nl.ordina.kijkdoos.view.control.ControlLightFragment.ARGUMENT_COMPONENT;

public class ControlViewBoxActivity extends AppCompatActivity implements ControlLightFragment.OnSwitchChangedListener {
    
    enum Component {
        LAMP_LEFT(R.id.ivLeftLamp, ViewBoxRemoteController::toggleLeftLamp), //
        LAMP_RIGHT(R.id.ivRightLamp, ViewBoxRemoteController::toggleRightLamp);

        private final int viewReference;
        private final Consumer<ViewBoxRemoteController> action;

        Component(@IdRes int ivLeftLamp, Consumer<ViewBoxRemoteController> action) {
            this.viewReference = ivLeftLamp;
            this.action = action;
        }

        public void performAction(ViewBoxRemoteController viewBoxRemoteController) {
            action.accept(viewBoxRemoteController);
        }

        public static Component get(@IdRes int viewReference) {
            final Optional<Component> optional = Stream.of(values()).filter(value -> value.viewReference == viewReference).findSingle();
            return optional.get();
        }
    }

    public static final String EXTRA_KEY_BUNDLED_VIEW_BOX_REMOTE_CONTROLLER = "BUNDLED_VIEW_BOX_REMOTE_CONTROLLER";
    public static final String EXTRA_KEY_VIEW_BOX_REMOTE_CONTROLLER = "VIEW_BOX_REMOTE_CONTROLLER";

    @BindView(R.id.ivTelevision)
    public ImageView television;

    @BindView(R.id.component_controller)
    public DrawerLayout componentController;

    @BindView(R.id.ivLeftLamp)
    public ImageView ivLeftLamp;

    @Getter(AccessLevel.PACKAGE) @VisibleForTesting
    private ViewBoxRemoteController viewBoxRemoteController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_box);
        ButterKnife.bind(this);
        getViewBoxApplication(this).getApplicationComponent().inject(this);

        componentController.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        componentController.setFocusableInTouchMode(false);

        final Bundle bundledExtras = getIntent().getExtras();
        final Bundle actualExtras = bundledExtras.getBundle(EXTRA_KEY_BUNDLED_VIEW_BOX_REMOTE_CONTROLLER);

        final Parcelable parceledViewBoxRemoteController = actualExtras.getParcelable(EXTRA_KEY_VIEW_BOX_REMOTE_CONTROLLER);
        viewBoxRemoteController = Parcels.unwrap(parceledViewBoxRemoteController);
        viewBoxRemoteController.connect(this);
    }

    @Override
    protected void onPause() {
        viewBoxRemoteController.disconnect();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (componentController.isDrawerOpen(GravityCompat.START)) {
            componentController.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.ivTelevision)
    public void onTelevisionClicked() {

    }

    @OnClick({R.id.ivLeftLamp, R.id.ivRightLamp})
    public void onLampClicked(View clickedView) {
        final Component component = Component.get(clickedView.getId());

        final Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_COMPONENT, component);

        final ControlLightFragment fragment = new ControlLightFragment();
        fragment.setArguments(args);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.left_drawer, fragment).commit();

        componentController.openDrawer(GravityCompat.START);
        componentController.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        componentController.setFocusableInTouchMode(true);
    }

    @Override
    public void onSwitchChanged(Component component) {
        component.performAction(viewBoxRemoteController);
    }
}
