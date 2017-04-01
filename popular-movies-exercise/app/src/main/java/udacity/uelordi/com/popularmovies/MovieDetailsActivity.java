package udacity.uelordi.com.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
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

    @BindView (R.id.tv_detail_title)TextView mTvTitle;
    @BindView (R.id.iv_poster_detail_path)ImageView mIvPoster;
    @BindView (R.id.tv_detail_user_rating)TextView mTvUserRating;
    @BindView (R.id.tv_detail_synopsys)TextView mTvSynopsys;
    @BindView (R.id.tv_detail_release_date)TextView mTvReleaseDate;
    @BindView (R.id.rv_movie_reviews) RecyclerView rvReviews;

    @BindView(R.id.bt_favorite_button) ImageButton btFavorite;
    //@BindView (R.id.rv_movie_trailers) RecyclerView mRvTrailers;


    ReviewAdapter mReviewtAdapter;
    private MovieContentDetails mCurrentMovieObject;
    private static String MOVIE_ID_KEY="movieid";

    private static final int MOVIE_DETAIL_TASK_ID=6;
    private static final String TAG = "MoveDetailsActivity";
    public static final String VIDEO_OBJECT_KEY = "video_list_key";

    public static final String[] MOVIE_DETAILS_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_SYNOPSYS,
            MovieContract.MovieEntry.COLUMN_IMAGE_URL,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_USER_RATING,

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Intent parent_activity=getIntent();
        if(savedInstanceState != null) {
            //mCurrentMovieObject =
        }
        mCurrentMovieObject = parent_activity.getParcelableExtra(VIDEO_OBJECT_KEY);
        initInterface();
        // TODO make the query of the id to get the image.
        // TODO Make cursor sql consult and get everything very easy
        // TODO See if it is favorite. so see if there is in the favorite folder.
        // TODO change the icon of the favorite, with the styles.
        // TODO get the trailers and the intents.
        // TODO use the query bundle of the onsave state to maintain the id in the memory.
        // TODO write the favorite bitmaps in the physical disk.
        // TODO improve the review part.
        // TODO HAY QUE HACER OTRO CURSOR LOADER PARA QUE ME DEVUELVA TODO Y HAGA TODO LA MOVIDA DE RELLENO.
        // TODO tienes que definir las proyecciones.
        // TODO tienes que definir bien el uri que vas a usar.
        Bundle queryBundle = new Bundle();

        queryBundle.putLong(MOVIE_ID_KEY,mCurrentMovieObject.getMovieID());
        getSupportLoaderManager().initLoader(MOVIE_DETAIL_TASK_ID, queryBundle, this);
    }

    @Override
    public Loader<List> onCreateLoader(int id, Bundle args) {
        Long movieid=args.getLong(MOVIE_ID_KEY);
        return (Loader<List>) new MovieDetailTaskLoader(this,movieid);
    }

    @Override
    public void onLoadFinished(Loader<List> loader, List data) {
            Log.v("text"," test");
        List<MovieContentDetails> result= data;
        LinearLayoutManager lmanager=new LinearLayoutManager(this);
        rvReviews.setLayoutManager(lmanager);
        if(result != null) {
            mCurrentMovieObject=result.get(0);
            mReviewtAdapter = new ReviewAdapter(mCurrentMovieObject.getReviewContent());
            rvReviews.setAdapter(mReviewtAdapter);
        }

    }
    @Override
    public void onLoaderReset(Loader<List> loader) {

    }

    @OnClick(R.id.bt_favorite_button)
    public void submit() {
        Log.v(TAG,"favorite button pressed:");
        if(mCurrentMovieObject != null) {
            if(FavoriteService.getInstance().
                            isFavorite(mCurrentMovieObject)){
                FavoriteService.getInstance().removeFromFavorites(mCurrentMovieObject);

            }
            else {
                FavoriteService.getInstance().addToFavorites(mCurrentMovieObject);
            }
        }
        else
        {
            Toast.makeText(this,getResources().
                            getString(R.string.error_movie_class_null),
                            Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public void initInterface()
    {
        FavoriteService.getInstance().setContext(this);
        if(mCurrentMovieObject != null) {
            mTvTitle.setText(mCurrentMovieObject.getTitle());
            mTvSynopsys.setText(mCurrentMovieObject.getSynopsis());
            mTvUserRating.setText(mCurrentMovieObject.getUser_rating());
            mTvReleaseDate.setText(mCurrentMovieObject.getRelease_date());
            String cursorPath = mCurrentMovieObject.getPoster_path();
            Log.v(TAG,"image_path: "+cursorPath);
            Picasso.with(getApplicationContext())
                    .load(cursorPath)
                    .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.no_image_available)
                    .into(mIvPoster);

        }
    }
}
