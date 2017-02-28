package nl.ordina.kijkdoos.view.control;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.annimon.stream.function.Consumer;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import dagger.Component;
import lombok.AccessLevel;
import lombok.Getter;
import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;

import static nl.ordina.kijkdoos.ViewBoxApplication.getViewBoxApplication;

public class ControlViewBoxActivity extends AppCompatActivity implements ControlLightFragment.OnSwitchChangedListener {
    
    enum Components {
        LAMP_LEFT(R.id.ivLeftLamp, ViewBoxRemoteController::toggleLed);

        private final int ivLeftLamp;
        private final Consumer<ViewBoxRemoteController> action;

        Components(int ivLeftLamp, Consumer<ViewBoxRemoteController> action) {

            this.ivLeftLamp = ivLeftLamp;
            this.action = action;
        }

        public void performAction(ViewBoxRemoteController viewBoxRemoteController) {
            action.accept(viewBoxRemoteController);
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

    @OnClick(R.id.ivLeftLamp)
    public void onLeftLampClicked() {
        final ControlLightFragment fragment = new ControlLightFragment();
        final Bundle args = new Bundle();
        args.putSerializable("bla", Components.LAMP_LEFT);
        fragment.setArguments(args);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.left_drawer, fragment).commit();
        componentController.openDrawer(GravityCompat.START);
    }

    @Override
    public void onSwitchChanged(Components component) {
        component.performAction(viewBoxRemoteController);
    }
}
