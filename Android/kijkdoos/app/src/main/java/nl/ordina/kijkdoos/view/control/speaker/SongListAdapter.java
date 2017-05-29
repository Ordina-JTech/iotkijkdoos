package nl.ordina.kijkdoos.view.control.speaker;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.annimon.stream.function.BiFunction;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Getter;
import nl.ordina.kijkdoos.R;

/**
 * Created by coenhoutman on 03/03/2017.
 */

public class SongListAdapter<T, U extends RecyclerView.ViewHolder> extends BaseAdapter {
    private final List<T> items;
    private final Class<U> viewHolderClass;
    private final int rowView;
    private final BiFunction<T, U, View> getViewFunction;
    private final LayoutInflater layoutInflater;

    public SongListAdapter(@NonNull List<T> items, Class<U> viewHolderClass, @LayoutRes int rowView, BiFunction<T, U, View> getViewFunction,
                           LayoutInflater layoutInflater) {
        this.items = items;
        this.viewHolderClass = viewHolderClass;
        this.rowView = rowView;
        this.getViewFunction = getViewFunction;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            final U viewHolder;
            if (convertView != null) {
                viewHolder = (U) convertView.getTag();
            } else {
                convertView = layoutInflater.inflate(rowView, null, false);
                viewHolder = viewHolderClass.getDeclaredConstructor(View.class).newInstance(convertView);
                convertView.setTag(viewHolder);
            }

            return getViewFunction.apply(items.get(position), viewHolder);
        } catch (InstantiationException e) {
            Log.w(SongListAdapter.class.getSimpleName(), "Unable to create viewholder");
        } catch (IllegalAccessException e) {
            Log.w(SongListAdapter.class.getSimpleName(), "Unable to create viewholder");
        } catch (NoSuchMethodException e) {
            Log.w(SongListAdapter.class.getSimpleName(), "Unable to create viewholder");
        } catch (InvocationTargetException e) {
            Log.w(SongListAdapter.class.getSimpleName(), "Unable to create viewholder");
        }

        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Getter
        @BindView(R.id.device_name)
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
