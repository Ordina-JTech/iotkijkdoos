package nl.ordina.kijkdoos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewBoxActivity extends AppCompatActivity {

    public static final String EXTRA_KEY_BUNDLED_VIEW_BOX_REMOTE_CONTROLLER = "BUNDLED_VIEW_BOX_REMOTE_CONTROLLER";
    public static final String EXTRA_KEY_VIEW_BOX_REMOTE_CONTROLLER = "VIEW_BOX_REMOTE_CONTROLLER";
    @BindView(R.id.IVTelevision)
    public ImageView television;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_box);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.IVTelevision)
    public void onTelevisionClicked() {
        Bundle animationOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, television, "ViewBoxComponent")
                .toBundle();
        startActivity(new Intent(this, EditComponentActivity.class), animationOptions);
    }
}
