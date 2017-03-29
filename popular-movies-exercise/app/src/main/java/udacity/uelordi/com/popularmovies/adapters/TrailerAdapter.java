package udacity.uelordi.com.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.content.TrailerContent;


/**
 * Created by uelordi on 28/03/17.
 */
// TODO, FINISH YOUR ADAPTER:
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    @Nullable
    private OnItemClickListener onItemClickListener;
    private final Context context;
    List <TrailerContent> mTrailerList =  new ArrayList<>();
    List<MovieContentDetails> m_movies_populate_array= new ArrayList<>();

    public TrailerAdapter(Context context,OnItemClickListener callback) {
        mTrailerList = new ArrayList<>();
        this.context = context;
        onItemClickListener = callback;
    }
    public void setTrailerList(List<TrailerContent> tcontent) {
        mTrailerList = tcontent;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int LayoutIndexForListItem =  R.layout.trailer_list_item;
        View view = LayoutInflater.from(context)
                .inflate(LayoutIndexForListItem, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mTrailerList == null) {
            return 0;
        }
        return mTrailerList.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_video_thumbnail)
        ImageView movieVideoThumbnail;

        public TrailerViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        void bind(int position) {
            mTrailerList.get(position).getVideoKey();
            Picasso.with(context).load(mTrailerList.
                    get(position).getPosterPath()).
                    into(movieVideoThumbnail);
        }
        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(mTrailerList.get(getAdapterPosition()));
        }
    }
}
