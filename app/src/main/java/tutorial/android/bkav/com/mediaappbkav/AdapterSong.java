package tutorial.android.bkav.com.mediaappbkav;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class AdapterSong extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Object> mSongItems;
    private int mPlayingPosition = -1;
    private Boolean mStateMusic = false;

    public static final int ALPHABEL = 0;
    public static final int SONG_ITEM = 1;

    private IOnSongClickListener mOnSongClickedLister;

    public AdapterSong(Context context, List<Object> itemList, IOnSongClickListener listener) {
        this.mContext = context;
        this.mSongItems = itemList;
        mOnSongClickedLister = listener;
    }


    public boolean getMusicState() {
        return mStateMusic;
    }
    public void setMusicState(boolean value) {
        this.mStateMusic = value;
    }

    @Override
    public int getItemViewType(int position) {
        if (mSongItems.get(position) instanceof CharSequence) {
            return ALPHABEL;
        } else if (mSongItems.get(position) instanceof SongItem) {
            return SONG_ITEM;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case ALPHABEL: {
                View itemAlphabel = inflater.inflate(R.layout.row_alphabet, parent, false);
                return new AlphabetAdapter(itemAlphabel);
            }
            case SONG_ITEM: {
                View itemSong = inflater.inflate(R.layout.row_song_item, parent, false);
                return new SongItemAdapter(itemSong);
            }
            default:
                break;
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case ALPHABEL: {
                final AlphabetAdapter alphabetAdapter = (AlphabetAdapter) holder;
                alphabetAdapter.mAlphabet.setText(mSongItems.get(position).toString());
                break;
            }
            case SONG_ITEM: {
                final SongItemAdapter songItemAdapter = (SongItemAdapter) holder;
                songItemRecyclerview(songItemAdapter, position);
                catchEventClick(songItemAdapter, position);
                break;
            }
        }
    }

    private void catchEventClick(final SongItemAdapter songItemAdapter, final int position) {
        songItemAdapter.mImformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext, songItemAdapter.mImformationButton);
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete_menu:
                                Toast.makeText(mContext, menuItem.getTitle(),
                                        Toast.LENGTH_LONG).show();
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        songItemAdapter.mLinearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongItem item = (SongItem) mSongItems.get(position);
                mStateMusic = true;
                if (mOnSongClickedLister != null) {
                    mOnSongClickedLister.onSongClicked(item);
                    if (mPlayingPosition != -1)
                        notifyItemChanged(mPlayingPosition);
                }
                SongItem songItem = (SongItem) mSongItems.get(position);
                if (songItemAdapter.mSttTextview.getText().toString().trim().equals("")) {
                    songItemAdapter.mSttTextview.setText(String.valueOf(songItem.getmPositionItem()));
                    songItemAdapter.mSttTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    mPlayingPosition = -1;
                    mOnSongClickedLister.hideInfo();
                } else {
                    songItemAdapter.mSttTextview.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_music, 0, 0, 0);
                    songItemAdapter.mSttTextview.setText("");
                    mPlayingPosition = position;

                }
            }
        });
    }


    private void songItemRecyclerview(final SongItemAdapter songItemAdapter, final int position) {

        SongItem songItem = (SongItem) mSongItems.get(position);
        songItemAdapter.mSttTextview.setText(String.valueOf(songItem.getmPositionItem()));
        songItemAdapter.mSttTextview.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        songItemAdapter.mNameSongTextView.setText(songItem.getmName());
        String duration = songItem.getmDuration();
        songItemAdapter.mTimerSongTextView.setText(duration == null ? "0" : songItem.setCorrectDuration(songItem.getmDuration()
        ));

    }


    @Override
    public int getItemCount() {
        return mSongItems.size();
    }

    public class SongItemAdapter extends RecyclerView.ViewHolder {

        ImageButton mImformationButton;
        TextView mSttTextview;
        TextView mNameSongTextView;
        TextView mTimerSongTextView;
        LinearLayout mLinearlayout;

        public SongItemAdapter(View itemView) {
            super(itemView);
            mImformationButton = (ImageButton) itemView.findViewById(R.id.imformation_button);
            mSttTextview = (TextView) itemView.findViewById(R.id.stt_textview);
            mNameSongTextView = (TextView) itemView.findViewById(R.id.name_song_textview);
            mTimerSongTextView = (TextView) itemView.findViewById(R.id.timer_song_textview);
            mLinearlayout = (LinearLayout) itemView.findViewById(R.id.linear_song);
        }
    }

    public class AlphabetAdapter extends RecyclerView.ViewHolder {
        private TextView mAlphabet;

        public AlphabetAdapter(View itemView) {
            super(itemView);
            mAlphabet = (TextView) itemView.findViewById(R.id.title_song_alphabet);
        }
    }
}