package udacity.uelordi.com.popularmovies;


import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import udacity.uelordi.com.popularmovies.adapters.OnReviewItemListener;
import udacity.uelordi.com.popularmovies.adapters.OnTrailerItemListener;
import udacity.uelordi.com.popularmovies.adapters.ReviewAdapter;
import udacity.uelordi.com.popularmovies.adapters.TrailerAdapter;
import udacity.uelordi.com.popularmovies.background.MovieDetailTaskLoader;
import udacity.uelordi.com.popularmovies.content.MovieContentDetails;
import udacity.uelordi.com.popularmovies.content.ReviewContent;
import udacity.uelordi.com.popularmovies.content.TrailerContent;
import udacity.uelordi.com.popularmovies.services.FavoriteService;
import udacity.uelordi.com.popularmovies.services.NetworkModule;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.OnReviewListener;
import udacity.uelordi.com.popularmovies.utils.OnTrailerListener;


public class MovieDetailsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List> ,
        OnTrailerItemListener,
        OnReviewItemListener,
        OnReviewListener,
        OnTrailerListener,
        FavoriteService.FavoriteListener

{

    @BindView (R.id.tv_detail_title)TextView mTvTitle;
    @BindView (R.id.iv_poster_detail_path)ImageView mIvPoster;
    @BindView (R.id.tv_detail_user_rating)TextView mTvUserRating;
    @BindView (R.id.tv_detail_synopsys)TextView mTvSynopsys;
    @BindView (R.id.tv_detail_release_date)TextView mTvReleaseDate;
    @BindView (R.id.rv_movie_reviews) RecyclerView rvReviews;
    @BindView (R.id.rv_movie_trailer) RecyclerView rvTrailers;
    @BindView(R.id.bt_favorite_button) ImageButton btFavorite;


    ReviewAdapter mReviewtAdapter;
    TrailerAdapter mTrailerAdatper;
    private MovieContentDetails mCurrentMovieObject;

    private static String MOVIE_ID_KEY = "movieid";

    private static final int MOVIE_DETAIL_TASK_ID = 6;
    private static final String TAG = "MoveDetailsActivity";
    public static final String VIDEO_OBJECT_KEY = "video_list_key";

    public static final String EXTRA_TRAILERS = "EXTRA_TRAILERS";
    public static final String EXTRA_REVIEWS = "EXTRA_REVIEWS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        initInterface();
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_TRAILERS)) {

                List<TrailerContent> trailers = savedInstanceState.
                                        getParcelableArrayList(EXTRA_TRAILERS);
                mTrailerAdatper.setTrailerList(trailers);
                Log.v(TAG," oncreate trailers size -> "+trailers.size());
            }
            if (savedInstanceState.containsKey(EXTRA_REVIEWS)) {
                List<ReviewContent> reviews = savedInstanceState.
                                            getParcelableArrayList(EXTRA_REVIEWS);
                Log.v(TAG,"oncreate reviews size -> "+reviews.size());
                mReviewtAdapter.setReviewList(reviews);
            }
        }
        else {
            if(NetworkModule.isOnline(getApplicationContext())) {
                loadTrailersAndReviews();
            }
            else {
                showNetworkingErrors();
            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<TrailerContent> trailers = mTrailerAdatper.getTrailerArrayList();
        Log.v(TAG,"onsavedinstance trailers size -> "+trailers.size());
        if (!trailers.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_TRAILERS, trailers);
        }

        ArrayList<ReviewContent> reviews = mReviewtAdapter.getReviewArrayList();
        if (!reviews.isEmpty()) {
            Log.v(TAG,"onsavedinstance reviews size -> "+reviews.size());
            outState.putParcelableArrayList(EXTRA_REVIEWS, reviews);
        }
    }
    public void loadTrailersAndReviews() {
        NetworkModule.getInstance().configureCallback((OnReviewListener)this);
        NetworkModule.getInstance().configureCallback((OnTrailerListener) this);

        NetworkModule.getInstance().getReviewList(mCurrentMovieObject.getId());
        NetworkModule.getInstance().getTrailerList(mCurrentMovieObject.getId());
    }
    @Override
    public Loader<List> onCreateLoader(int id, Bundle args) {
        Long movieid=args.getLong(MOVIE_ID_KEY);
        return new MovieDetailTaskLoader(this,movieid);
    }

    @Override
    public void onLoadFinished(Loader<List> loader, List data) {
            Log.v("text"," test");
        List<MovieContentDetails> result = data;

        if(result != null) {
            mCurrentMovieObject=result.get(0);
//            mReviewtAdapter.setReviewList(mCurrentMovieObject.getReviewContent());
//            mTrailerAdatper.setTrailerList(mCurrentMovieObject.getTrailerContent());
        }

    }
    @Override
    public void onLoaderReset(Loader<List> loader) {

    }

    @OnClick(R.id.bt_favorite_button)
    public void submit()  {
        Log.v(TAG,"favorite button pressed:");
        FavoriteService.getInstance().checkFavorite(mCurrentMovieObject,true);
    }



    public void initInterface()
    {
        Intent parent_activity=getIntent();
        mCurrentMovieObject = parent_activity.getParcelableExtra(VIDEO_OBJECT_KEY);
        LinearLayoutManager lmanager=new LinearLayoutManager(this);
        GridLayoutManager trmanager=new GridLayoutManager(MovieDetailsActivity.this,3);
        rvReviews.setLayoutManager(lmanager);
        rvTrailers.setLayoutManager(trmanager);
        mReviewtAdapter = new ReviewAdapter(this);
        mTrailerAdatper = new TrailerAdapter(this,this);
        rvReviews.setAdapter(mReviewtAdapter);
        rvTrailers.setAdapter(mTrailerAdatper);


        //FavoriteService.getInstance().setContext(this);
        FavoriteService.getInstance().registerContentResolver(getContentResolver(),this);
        FavoriteService.getInstance().checkFavorite(mCurrentMovieObject, false);
        if(mCurrentMovieObject != null) {
            mTvTitle.setText(mCurrentMovieObject.getOriginalTitle());
            mTvSynopsys.setText(mCurrentMovieObject.getOverview());
            mTvUserRating.setText(String.valueOf(mCurrentMovieObject.getVoteAverage()));
            mTvReleaseDate.setText(mCurrentMovieObject.getReleaseDate());
            String movie_path = NetworkModule.getInstance().getImageUrlPah()
                                + mCurrentMovieObject.getPosterPath();
            Log.v(TAG,"image_path: "+movie_path);
            Glide.with(getApplicationContext()).load(
                    movie_path)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.no_image_available)
                    .into(mIvPoster);
        }
    }
    @Override
    public void onTrailerItemClick(TrailerContent content) {
        Intent trailerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(content.getTrailerURL()));
        if (trailerIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(trailerIntent);
        }
    }

    @Override
    public void onReviewItemClick(ReviewContent content) {
        Intent reviewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(content.getReviewUrl()));
        if (reviewIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(reviewIntent);
        }
    }
    public void showNetworkingErrors() {
        Toast.makeText(getApplicationContext(),
                        getString(R.string.connectivity_warning_reviews_trailers).toString(),
                                                                    Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnReviewListAvailable(List<ReviewContent> result) {
        mReviewtAdapter.setReviewList(result);
    }

    @Override
    public void OnTrailerListAvailable(List<TrailerContent> result) {
       mTrailerAdatper.setTrailerList(result);
    }
    @Override
    public void onIsFavorite(boolean isFavorite, boolean performAction) {
        if(performAction) {
            if(mCurrentMovieObject != null) {
                if (isFavorite) {
                    FavoriteService.getInstance().removeFromFavorites(mCurrentMovieObject);
                } else {
                    FavoriteService.getInstance().addToFavorites(mCurrentMovieObject);
                }
            }
            else {
                Toast.makeText(this,getResources().
                                getString(R.string.error_movie_class_null),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            if(isFavorite) {
                btFavorite.setImageResource(R.drawable.favorite_pressed_button);
            }
            else {
                btFavorite.setImageResource(R.drawable.no_favorite);
            }
        }
    }
    @Override
    public void onAddFavoriteCompleted(boolean result) {
        if(result) {
            btFavorite.setImageResource(R.drawable.favorite_pressed_button);
        }
    }

    @Override
    public void onRemoveFavoriteCompleted(boolean result) {
        if(result) {
            btFavorite.setImageResource(R.drawable.no_favorite);
        }
    }

    @Override
    public void onFavoriteError(String error) {

    }
}
