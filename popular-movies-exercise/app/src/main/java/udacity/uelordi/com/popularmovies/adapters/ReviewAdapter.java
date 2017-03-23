package udacity.uelordi.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.content.ReviewContent;

/**
 * Created by uelordi on 22/03/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewContent> m_reviews_populate_array= new ArrayList<>();
    private static int viewHolderCount;
    private final static String TAG = "ReviewAdapter";

    public ReviewAdapter(List<ReviewContent> content) {
        viewHolderCount=0;
        m_reviews_populate_array=content;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int LayoutIndexForListItem= R.layout.review_list_item;
        LayoutInflater li=LayoutInflater.from(context);
        boolean shouldAtattachtToTheParentNow=false;

        View view= li.inflate(LayoutIndexForListItem,parent,shouldAtattachtToTheParentNow);
        ReviewAdapter.ReviewViewHolder result_view=new ReviewAdapter.ReviewViewHolder(view);
        viewHolderCount++;
        return result_view;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {

        return m_reviews_populate_array.size();
    }

     class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        TextView m_review_author;
        TextView m_review_content;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            m_review_author = (TextView)itemView.findViewById(R.id.tv_movie_review_author);
            m_review_content =(TextView)itemView.findViewById(R.id.tv_movie_review_content);
        }
        void bind(int listIndex)
        {
            Log.d(TAG,"Position #"+listIndex);
            if(m_reviews_populate_array.size()>0)
            {
                if(listIndex<m_reviews_populate_array.size()) {
                    m_review_author.setText(m_reviews_populate_array.get(listIndex).getAuthor());
                    m_review_content.setText(m_reviews_populate_array.get(listIndex).getContent());
                }
            }

        }

    }
}
