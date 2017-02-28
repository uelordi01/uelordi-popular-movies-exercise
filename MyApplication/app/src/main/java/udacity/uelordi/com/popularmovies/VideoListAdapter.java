package udacity.uelordi.com.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import udacity.uelordi.com.popularmovies.content.MovieContent;

/**
 * Created by uelordi on 28/02/2017.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MovieViewHolder>  {

    final private ListItemClickListener m_listener;
    List<MovieContent> m_movies_populate_array=new ArrayList<MovieContent>();
    int mNumberOfItems;
    public interface ListItemClickListener
    {
        void onListItemClick(int clickItemIndex);
    }

    public VideoListAdapter(int numberOfItems, ListItemClickListener listener) {
        mNumberOfItems=numberOfItems;
        m_listener=listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
       //This is called per each view;
        // holder.bind(position)
    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public void setMovieList(List<MovieContent> movies_populate_array)
    {
        m_movies_populate_array=movies_populate_array;
    }
    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public MovieViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
