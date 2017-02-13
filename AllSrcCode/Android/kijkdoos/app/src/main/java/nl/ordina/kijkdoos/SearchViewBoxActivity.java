package nl.ordina.kijkdoos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchViewBoxActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.viewBoxList)
    ListView viewBoxList;
    private ArrayAdapter<String> viewBoxListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view_box);
        ButterKnife.bind(this);

        viewBoxListAdapter = new ArrayAdapter<>(this, R.layout.listitem_device, R.id.device_name);
        viewBoxListAdapter.add("JTech Kijkdoos 1");
        viewBoxList.setAdapter(viewBoxListAdapter);
        viewBoxList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(this, ViewBoxActivity.class));
    }
}
