package udacity.uelordi.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;

/**
 * Created by uelordi on 28/02/2017.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MovieViewHolder>  {
    private final String TAG = VideoListAdapter.class.getSimpleName();
    final private OnItemClickListener m_listener;
    List<MovieContentDetails> m_movies_populate_array;
    Context mContext;
    private static int viewHolderCount;



    public VideoListAdapter(OnItemClickListener listener) {

        m_listener=listener;
        viewHolderCount=0;
        m_movies_populate_array= new ArrayList<>();

    }
    public void setMovieList(List<MovieContentDetails> data)
    {
        m_movies_populate_array = data;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int LayoutIndexForListItem =  R.layout.movie_list_item;
        viewHolderCount++;
        View view = LayoutInflater.from(context)
                .inflate(LayoutIndexForListItem, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
       //This is called per each view;

         holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return m_movies_populate_array.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        final ImageView m_movie_poster;
         TextView m_auxiliar_text = null;
        public MovieViewHolder(View itemView) {
            super(itemView);
            m_movie_poster=(ImageView)itemView.findViewById(R.id.iv_item_movie_poster);
            m_auxiliar_text = (TextView)itemView.findViewById(R.id.tv_aux_title);
            itemView.setOnClickListener(this);
        }
        void bind(int listIndex)
        {
            Log.d(TAG,"Position #"+listIndex);
            if(m_movies_populate_array.size()>0)
            {
                if(listIndex<m_movies_populate_array.size())
                {
                    //m_movie_title.setText(m_movies_populate_array.get(listIndex).getTitle());
                    Log.v(TAG,"image_path: "+m_movies_populate_array.get(listIndex).
                                                                                getPoster_path());

                    Picasso.with(itemView.getContext())
                            .load(m_movies_populate_array.get(listIndex).getPoster_path())
                            .placeholder(R.drawable.no_image_available)
                            .error(R.drawable.no_image_available)
                            .into(m_movie_poster);
                   /* m_auxiliar_text.setText(m_movies_populate_array.
                            get(listIndex).getTitle());*/
                }
            }

        }
        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            m_listener.onItemClick(m_movies_populate_array.get(clickedPosition));
        }
    }
}
