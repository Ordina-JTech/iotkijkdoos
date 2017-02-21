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
