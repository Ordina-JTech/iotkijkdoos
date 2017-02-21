package nl.ordina.kijkdoos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;
import nl.ordina.kijkdoos.search.SearchViewBoxActivity;

public class ViewBoxListAdapter extends BaseAdapter {
    private ArrayList<ViewBoxRemoteController> viewBoxRemoteControllers;
    private LayoutInflater inflater;

    public ViewBoxListAdapter(SearchViewBoxActivity activity) {
        super();
        viewBoxRemoteControllers = new ArrayList<>();
        inflater = activity.getLayoutInflater();
    }

    public void addViewBoxRemoteController(ViewBoxRemoteController device) {
        if (!viewBoxRemoteControllers.contains(device)) {
            viewBoxRemoteControllers.add(device);
            notifyDataSetChanged();
        }
    }

    public ViewBoxRemoteController getViewBoxRemoteController(int position) {
        return viewBoxRemoteControllers.get(position);
    }

    public void clear() {
        viewBoxRemoteControllers.clear();
    }

    @Override
    public int getCount() {
        return viewBoxRemoteControllers.size();
    }

    @Override
    public Object getItem(int i) {
        return viewBoxRemoteControllers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.listitem_device, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder.title.setText(viewBoxRemoteControllers.get(position).getName());

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.device_name)
        TextView title;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}