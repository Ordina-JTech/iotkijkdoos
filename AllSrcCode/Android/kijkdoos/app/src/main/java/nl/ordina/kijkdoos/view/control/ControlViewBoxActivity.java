package nl.ordina.kijkdoos.view.control;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.annimon.stream.function.BiConsumer;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lombok.AccessLevel;
import lombok.Getter;
import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.bluetooth.BluetoothConnectionFragment;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.view.control.speaker.ControlSpeakerFragment;

import static android.bluetooth.BluetoothAdapter.STATE_ON;
import static nl.ordina.kijkdoos.view.control.ControlLightFragment.ARGUMENT_COMPONENT;

public class ControlViewBoxActivity extends AppCompatActivity implements AbstractControlFragment.OnComponentChangedListener, DrawerLayout.DrawerListener {

    enum Component {
        LAMP_LEFT(R.id.ivLeftLamp, R.string.controlLampTitle, ControlLightFragment.class, (controller, lightStatus) -> controller.switchLeftLamp((boolean)lightStatus)), //
        LAMP_RIGHT(R.id.ivRightLamp, R.string.controlLampTitle, ControlLightFragment.class, (controller, lightStatus) -> controller.toggleRightLamp((boolean)lightStatus)), //
        DISCO_BALL(R.id.ivDiscoBall, R.string.controlDiscoBallTitle, ControlDiscoBallFragment.class, (controller, color) -> {
            if (color == null) controller.switchOffDiscoBall();
            else controller.setDiscoBallColor((ControlDiscoBallFragment.DiscoBallColor) color);
        }),
        GUITAR(R.id.ivGuitar, R.string.controlSpeakerTitle, ControlSpeakerFragment.class, (controller, song) -> controller.playSong((ControlSpeakerFragment.Song) song)), //
        TELEVISION(R.id.ivTelevision, R.string.controlTelevisionTitle, ControlTelevisionFragment.class,
                (controller, degree) -> controller.rotateTelevision((int) degree));

        private final int viewReference;
        @Getter
        private final int titleString;
        private final Class<? extends AbstractControlFragment> fragmentClass;
        private final BiConsumer<ViewBoxRemoteController, Object> action;

        Component(@IdRes int viewReference, @StringRes int titleString, Class<? extends AbstractControlFragment> fragmentClass,
                  BiConsumer<ViewBoxRemoteController, Object> action) {
            this.viewReference = viewReference;
            this.titleString = titleString;
            this.fragmentClass = fragmentClass;
            this.action = action;
        }

        public void performAction(ViewBoxRemoteController viewBoxRemoteController, Object value) {
            action.accept(viewBoxRemoteController, value);
        }

        public AbstractControlFragment getFragment() throws IllegalAccessException, InstantiationException {
            return fragmentClass.newInstance();
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

    @Getter(AccessLevel.PACKAGE)
    @VisibleForTesting
    private ViewBoxRemoteController viewBoxRemoteController;

    private Map<Component, Fragment> fragmentCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_box);
        ButterKnife.bind(this);

        componentController.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        componentController.setFocusableInTouchMode(false);
        componentController.addDrawerListener(this);

        final Bundle bundledExtras = getIntent().getExtras();
        final Bundle actualExtras = bundledExtras.getBundle(EXTRA_KEY_BUNDLED_VIEW_BOX_REMOTE_CONTROLLER);

        final Parcelable parceledViewBoxRemoteController = actualExtras.getParcelable(EXTRA_KEY_VIEW_BOX_REMOTE_CONTROLLER);
        viewBoxRemoteController = Parcels.unwrap(parceledViewBoxRemoteController);
        viewBoxRemoteController.connect(this);

        fragmentCache = new HashMap<>(Component.values().length - 1);

        final BluetoothConnectionFragment bluetoothConnectionFragment = BluetoothConnectionFragment.add(this);

        bluetoothConnectionFragment.addConnectionEventHandler(state -> state != STATE_ON, state -> finish());
    }

    @Override
    protected void onPause() {
        super.onPause();

        viewBoxRemoteController.disconnect();
    }

    @Override
    public void onBackPressed() {
        if (componentController.isDrawerOpen(GravityCompat.START)) {
            componentController.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.ivLeftLamp, R.id.ivRightLamp, R.id.ivDiscoBall, R.id.ivGuitar, R.id.ivTelevision})
    public void onComponentClicked(View clickedView) {
        final Component component = Component.get(clickedView.getId());

        final Fragment cachedFragment = getCachedFragment(component);

        if (cachedFragment != null) {
            getSupportFragmentManager().beginTransaction().attach(cachedFragment).commit();
        } else {
            try {
                showNewComponentController(component);
            } catch (Exception e) {
                Log.w(ControlViewBoxActivity.class.getSimpleName(), "Unable to create controller");
            }
        }

        componentController.openDrawer(GravityCompat.START);
        componentController.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        componentController.setFocusableInTouchMode(true);
    }

    protected void showNewComponentController(Component component) throws Exception {
        final Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_COMPONENT, component);

        AbstractControlFragment fragment = component.getFragment();
        fragment.setArguments(args);

        fragmentCache.put(component, fragment);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.left_drawer, fragment).commit();
    }

    @Override
    public void onComponentChanged(Component component, Object value) {
        component.performAction(viewBoxRemoteController, value);
    }

    @Nullable
    private Fragment getCachedFragment(Component component) {
        if (fragmentCache.containsKey(component)) {
            return fragmentCache.get(component);
        }

        return null;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        final Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.left_drawer);
        getSupportFragmentManager().beginTransaction().detach(currentFragment).commit();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
