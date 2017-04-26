package nl.ordina.kijkdoos.view.search;

import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.bluetooth.ViewBoxRemoteController;

import static java.lang.Integer.valueOf;
import static java.util.Collections.sort;

public class ViewBoxListAdapter extends BaseAdapter {
    private ArrayList<ViewBoxRemoteController> viewBoxRemoteControllers;
    private LayoutInflater inflater;
    private Set<Integer> disabledItems;

    public ViewBoxListAdapter(SearchViewBoxActivity activity) {
        super();
        viewBoxRemoteControllers = new ArrayList<>();
        inflater = activity.getLayoutInflater();
        disabledItems = new HashSet<>();
    }

    @UiThread
    public void addViewBoxRemoteController(ViewBoxRemoteController device) {
        if (device == null) return;

        if (!viewBoxRemoteControllers.contains(device)) {
            viewBoxRemoteControllers.add(device);
            sort(viewBoxRemoteControllers, (o1, o2) -> valueOf(o1.getSignalStrength()).compareTo(o2.getSignalStrength()));

            notifyDataSetChanged();
        }
    }

    @UiThread
    public void removeViewBoxRemoteController(ViewBoxRemoteController device) {
        viewBoxRemoteControllers.remove(device);
        notifyDataSetChanged();
    }

    public ViewBoxRemoteController getViewBoxRemoteController(int position) {
        return viewBoxRemoteControllers.get(position);
    }

    public void clear() {
        viewBoxRemoteControllers.clear();
        notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position) {
        return !disabledItems.contains(position);
    }

    public void enableItems() {
        disabledItems.clear();
    }

    public void disableItem(int position) {
        disabledItems.add(position);
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