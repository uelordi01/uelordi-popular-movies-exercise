package udacity.uelordi.com.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.database.MovieContract;

/**
 * Created by uelordi on 28/02/2017.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MovieViewHolder>  {
    private final String TAG = VideoListAdapter.class.getSimpleName();
    final private OnVideoItemClickListener m_listener;
    List<MovieContentDetails> m_movies_populate_array;
    Context mContext;
    private static int viewHolderCount;
    private Cursor mCursor;
    private String mOptionType;


    public VideoListAdapter(OnVideoItemClickListener listener) {

        m_listener=listener;
        viewHolderCount=0;
        m_movies_populate_array= new ArrayList<>();

    }
    public void swapCursor(Cursor newCursor)
    {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public void setOptionType(String option) {
        mOptionType = option;
    }
    public void addData(Cursor cursor) {
        m_movies_populate_array.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(MovieContract.MovieEntry.COL_MOVIE_ID);
                String title = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_TITLE);
                String posterPath = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_POSTER_PATH);
                String overview = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_OVERVIEW);
                String rating = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_VOTE_AVERAGE);
                String releaseDate = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_RELEASE_DATE);
                String backdropPath = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_BACKDROP_PATH);
                MovieContentDetails movie = new MovieContentDetails(id ,
                                                                    title,
                                                                    overview,
                                                                    rating,
                                                                    posterPath,
                                                                    releaseDate,
                                                                    backdropPath);
                m_movies_populate_array.add(movie);
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }
    public void addData( List<MovieContentDetails> movie_list) {
        m_movies_populate_array.clear();
        m_movies_populate_array.addAll(movie_list);
        notifyDataSetChanged();
    }
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int LayoutIndexForListItem =  R.layout.movie_list_item;
        viewHolderCount++;
        View view = LayoutInflater.from(context)
                .inflate(LayoutIndexForListItem, parent, false);
        view.setFocusable(true);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
       //This is called per each view;
         //mCursor.moveToPosition(position);
         holder.bind(position);
    }

    @Override
    public int getItemCount() {

        return m_movies_populate_array.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.iv_item_movie_poster)
        ImageView m_movie_poster;
        @BindView(R.id.tv_aux_title)
         TextView m_auxiliar_text = null;
        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        void bind(int listIndex)
        {
            Log.d(TAG,"Position #"+listIndex);
            String movie_path = null;
                    //m_movie_title.setText(m_movies_populate_array.get(listIndex).getTitle());
//                  if(mOptionType.equals(itemView.getResources().getString(R.string.pref_sort_favorites_value))) {
//                        movie_path =ImageHandler.getInstance().getImagePath(itemView.getContext(),
//                                                    m_movies_populate_array
//                                                    .get(listIndex).getPoster_path());
//                        //File f= new File(movie_path);
//                        Picasso.with(itemView.getContext())
//                                .load(movie_path)
//                                .config(Bitmap.Config.RGB_565)
//                                .placeholder(R.drawable.no_image_available)
//                                .error(R.drawable.no_image_available)
//                                .into(m_movie_poster);
//                    }
//                    else {
                        movie_path=m_movies_populate_array.
                                get(listIndex).getBaseIMAGE_URL_PATH() +
                                m_movies_populate_array
                                        .get(listIndex).getPoster_path();
//                    }

                    Log.v(TAG,"image_path: "+movie_path);
                    Glide.with(itemView.getContext()).load(
                            movie_path)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                            .into(m_movie_poster);

//                    Picasso.with(itemView.getContext())
//                            .load(movie_path)
//                            .placeholder(R.drawable.no_image_available)
//                            .error(R.drawable.no_image_available)
//                            .into(m_movie_poster);


        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            m_listener.onItemClick(m_movies_populate_array.get(clickedPosition));
        }
    }
}
