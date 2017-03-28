package udacity.uelordi.com.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by uelordi on 28/03/17.
 */
// TODO, FINISH YOUR ADAPTER:
public class TrailerAdapter extends RecyclerView.Adapter<TrailerViewHolder> {
    @Nullable
    private OnItemClickListener onItemClickListener;
    private final Context context;

    public TrailerAdapter(Context context,OnItemClickListener callback) {
        this.context = context;
        onItemClickListener = callback;
    }


    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TrailerViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
