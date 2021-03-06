package udacity.uelordi.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.content.ReviewContent;

/**
 * Created by uelordi on 22/03/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewContent> mReviewsPopulateArray= new ArrayList<>();
    private static int viewHolderCount;
    private final static String TAG = "ReviewAdapter";
    private OnReviewItemListener mListener;

    public ReviewAdapter(OnReviewItemListener listener) {
        viewHolderCount=0;
        mListener = listener;
    }
    public void setReviewList(List<ReviewContent> data){
        mReviewsPopulateArray = data;
        notifyDataSetChanged();
    }
    public ArrayList<ReviewContent> getReviewArrayList() {
        ArrayList<ReviewContent> listoftrailers = new ArrayList<>(mReviewsPopulateArray.size());
        listoftrailers.addAll( mReviewsPopulateArray);
        return listoftrailers;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context=parent.getContext();
        int LayoutIndexForListItem= R.layout.review_list_item;
        LayoutInflater li=LayoutInflater.from(context);
        View view= li.inflate(LayoutIndexForListItem,parent,false);
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

        return mReviewsPopulateArray.size();
    }
     class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tv_movie_review_author)
        TextView mReviewAuthor;
        @BindView(R.id.tv_movie_review_content)
        TextView mReviewContent;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        void bind(int listIndex)
        {
            Log.d(TAG,"Position #"+listIndex);
            if(mReviewsPopulateArray.size()>0) {
                if(listIndex<mReviewsPopulateArray.size()) {
                    mReviewAuthor.setText(mReviewsPopulateArray.get(listIndex).getAuthor());
                    mReviewContent.setText(mReviewsPopulateArray.get(listIndex).getContent());
                }
            }
        }
        @Override
        public void onClick(View v) {
            mListener.onReviewItemClick(mReviewsPopulateArray.get(getAdapterPosition()));
        }
    }
}
