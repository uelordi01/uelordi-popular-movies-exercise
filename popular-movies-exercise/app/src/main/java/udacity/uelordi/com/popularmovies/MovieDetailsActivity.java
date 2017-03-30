package udacity.uelordi.com.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import udacity.uelordi.com.popularmovies.adapters.ReviewAdapter;
import udacity.uelordi.com.popularmovies.background.MovieDetailTaskLoader;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.content.ReviewContent;
import udacity.uelordi.com.popularmovies.database.MovieContract;
import udacity.uelordi.com.popularmovies.services.FavoriteService;


public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List> {
    @BindView (R.id.tv_detail_title)TextView m_tv_title;
    @BindView (R.id.iv_poster_detail_path)ImageView m_iv_poster;
    @BindView (R.id.tv_detail_user_rating)TextView m_tv_user_rating;
    @BindView (R.id.tv_detail_synopsys)TextView m_tv_synopsys;
    @BindView (R.id.tv_detail_release_date)TextView m_tv_release_date;
    @BindView (R.id.rv_movie_reviews) RecyclerView rvReviews;

   @BindView(R.id.bt_favorite_button) ImageButton btFavorite;
    //@BindView (R.id.rv_movie_trailers) RecyclerView mRvTrailers;


     ReviewAdapter mReviewtAdapter;

    private static String MOVIE_ID_KEY="movieid";

    private static final int MOVIE_DETAIL_TASK_ID=6;
    private static final String TAG = "MoveDetailsActivity";
    MovieContentDetails m_current_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
        initInterface();
        Intent parent_activity=getIntent();
        if( parent_activity.hasExtra("title") )
        {
            m_tv_title.setText(parent_activity.getStringExtra("title"));
        }
        if( parent_activity.hasExtra("user_rating") )
        {
            m_tv_user_rating.setText(parent_activity.getStringExtra("user_rating"));
        }
        if( parent_activity.hasExtra("poster_path") )
        {
            Picasso.with(m_iv_poster.getContext())
                    .load(parent_activity.getStringExtra("poster_path"))
                    .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.no_image_available)
                    .into(m_iv_poster);
        }
        if( parent_activity.hasExtra("synopsys") )
        {
            m_tv_synopsys.setText(parent_activity.getStringExtra("synopsys"));
        }
        if( parent_activity.hasExtra("release_date") )
        {
            m_tv_release_date.setText(getResources().getString(R.string.release_date)
                                                    +parent_activity.getStringExtra("release_date"));
        }
        Bundle queryBundle = new Bundle();
        queryBundle.putLong(MOVIE_ID_KEY,parent_activity.getLongExtra("movieid",0));
        getSupportLoaderManager().initLoader(MOVIE_DETAIL_TASK_ID, queryBundle, this);
    }

    @Override
    public Loader<List> onCreateLoader(int id, Bundle args) {
        Long movieid=args.getLong(MOVIE_ID_KEY);
        return new MovieDetailTaskLoader(this,movieid);
    }

    @Override
    public void onLoadFinished(Loader<List> loader, List data) {
            Log.v("text"," test");
        List<MovieContentDetails> result= data;
        LinearLayoutManager lmanager=new LinearLayoutManager(this);
        rvReviews.setLayoutManager(lmanager);
        m_current_content=result.get(0);
        mReviewtAdapter = new ReviewAdapter(m_current_content.getReviewContent());
        rvReviews.setAdapter(mReviewtAdapter);
    }
    @Override
    public void onLoaderReset(Loader<List> loader) {

    }

    @OnClick(R.id.bt_favorite_button)
    public void submit() {
        Log.v(TAG,"favorite button pressed:");
        if(m_current_content != null) {
            if(FavoriteService.getInstance().
                            isFavorite(m_current_content)){
                FavoriteService.getInstance().removeFromFavorites(m_current_content);

            }
            else {
                FavoriteService.getInstance().addToFavorites(m_current_content);
            }
        }
        else
        {
            Toast.makeText(this,getResources().
                            getString(R.string.error_movie_class_null),
                            Toast.LENGTH_SHORT).show();
        }
    }
    public void initInterface()
    {
        FavoriteService.getInstance().setContext(getApplicationContext());
    }
}
