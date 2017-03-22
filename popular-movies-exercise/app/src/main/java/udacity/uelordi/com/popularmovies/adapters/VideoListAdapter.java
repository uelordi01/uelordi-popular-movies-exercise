package udacity.uelordi.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    final private ListItemClickListener m_listener;
    List<MovieContentDetails> m_movies_populate_array= new ArrayList<>();

    private static int viewHolderCount;

    public interface ListItemClickListener
    {
        void onListItemClick(MovieContentDetails movie);
    }


    public VideoListAdapter(ListItemClickListener listener,List<MovieContentDetails> data) {

        m_listener=listener;
        viewHolderCount=0;
        m_movies_populate_array=data;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int LayoutIndexForListItem= R.layout.movie_list_item;
        LayoutInflater li=LayoutInflater.from(context);
        boolean shouldAtattachtToTheParentNow=false;

        View view= li.inflate(LayoutIndexForListItem,parent,shouldAtattachtToTheParentNow);
        MovieViewHolder result_view=new MovieViewHolder(view);
        viewHolderCount++;
        return result_view;
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
        public MovieViewHolder(View itemView) {
            super(itemView);
            m_movie_poster=(ImageView)itemView.findViewById(R.id.iv_item_movie_poster);
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
                    //Log.v(TAG,"image_path: "+m_movies_populate_array.get(listIndex).getPoster_path());
                    Picasso.with(itemView.getContext()).load(m_movies_populate_array.get(listIndex).getPoster_path()).into(m_movie_poster);
                }
            }

        }
        @Override
        public void onClick(View v) {
            int clickedPosition=getAdapterPosition();
            m_listener.onListItemClick(m_movies_populate_array.get(clickedPosition));
        }
    }
}