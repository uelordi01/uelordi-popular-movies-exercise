package udacity.uelordi.com.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.uelordi.com.popularmovies.R;
import udacity.uelordi.com.popularmovies.content.TrailerContent;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    @Nullable
    private OnTrailerItemListener onItemClickListener;
    private final Context context;
    private List <TrailerContent> mTrailerList =  new ArrayList<>();

    public TrailerAdapter(Context context,OnTrailerItemListener callback) {
        mTrailerList = new ArrayList<>();
        this.context = context;
        onItemClickListener = callback;
    }
    public void setTrailerList(List<TrailerContent> tcontent) {
        mTrailerList = tcontent;
        notifyDataSetChanged();
    }
    public  ArrayList<TrailerContent> getTrailerArrayList(){
        ArrayList<TrailerContent> listoftrailers = new ArrayList<>(mTrailerList.size());
        listoftrailers.addAll( mTrailerList);
        return listoftrailers;
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
        @BindView(R.id.tv_trailer_title)
        TextView mTrailerTitle;

        public TrailerViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }
        void bind(int position) {
            String title = mTrailerList.get(position).getVideoName();
            mTrailerTitle.setText(title);
        }
        @Override
        public void onClick(View v) {
            if(onItemClickListener != null) {
                onItemClickListener.onTrailerItemClick(mTrailerList.get(getAdapterPosition()));
            }
        }
    }
}
