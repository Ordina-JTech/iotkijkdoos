package nl.ordina.kijkdoos.view.control.speaker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;

import com.annimon.stream.Stream;
import com.annimon.stream.function.BiFunction;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import lombok.Getter;
import nl.ordina.kijkdoos.R;
import nl.ordina.kijkdoos.view.control.AbstractControlFragment;

import static java.util.Arrays.asList;
import static nl.ordina.kijkdoos.R.string.songTitleAlarm;
import static nl.ordina.kijkdoos.R.string.songTitleCustom;
import static nl.ordina.kijkdoos.R.string.songTitleFatherJacob;

/**
 * Created by coenhoutman on 02/03/2017.
 */
public class ControlSpeakerFragment extends AbstractControlFragment implements AdapterView.OnItemClickListener {

    private SongListAdapter<Song, SongListAdapter.ViewHolder> songListAdapter;

    public enum Song {
        ALARM(songTitleAlarm, "d"), VADER_JACOB(songTitleFatherJacob, "e"), CUSTOM(songTitleCustom, "f");

        @StringRes
        private final int title;

        @Getter
        private final String message;

        Song(@StringRes int title, String message) {
            this.title = title;
            this.message = message;
        }
    }

    @BindView(R.id.songList)
    public ListView songList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final BiFunction<Song, SongListAdapter.ViewHolder, View> viewSetterFunction = (song, viewHolder) -> {
            viewHolder.getTitle().setText(song.title);

            return viewHolder.itemView;
        };
        songListAdapter = new SongListAdapter<>(asList(Song.values()), SongListAdapter.ViewHolder.class,
                R.layout.listitem_device, viewSetterFunction, getActivity().getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        songList.setAdapter(songListAdapter);

        return view;
    }

    @Override
    protected int getControlLayoutId() {
        return R.layout.control_speaker_component;
    }

    @Override
    protected ViewStub getViewStub(View inflatedView) {
        return ButterKnife.findById(inflatedView, R.id.fullScreenControlStub);
    }

    @OnItemClick(R.id.songList)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getComponentChangedListener().onComponentChanged(getComponent(), songListAdapter.getItem(position));
    }
}
