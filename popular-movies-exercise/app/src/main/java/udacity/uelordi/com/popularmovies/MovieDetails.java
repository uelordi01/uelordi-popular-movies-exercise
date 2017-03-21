package udacity.uelordi.com.popularmovies;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
//import udacity.uelordi.com.popularmovies.background.ReviewListTaskLoader;


public class MovieDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List> {
    @BindView (R.id.tv_detail_title)TextView m_tv_title;
    @BindView (R.id.iv_poster_detail_path)ImageView m_iv_poster;
    @BindView (R.id.tv_detail_user_rating)TextView m_tv_user_rating;
    @BindView (R.id.tv_detail_synopsys)TextView m_tv_synopsys;
    @BindView (R.id.tv_detail_release_date)TextView m_tv_release_date;

    private static String MOVIE_ID_KEY="movieid";

    private static final int REVIEW_TASK_ID=6;
    private static final int TRAILER_TASK_ID=7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO CREATE THE DBHELPER
        //TODO CREATE THE ONCREATE AND ONUPGRADE
        //TODO CREATE THE DATABASE NAME AND DATABASE VERSION ATTRIBUTES
        //TODO CREATE THE SQL STATEMENT TO CREATE AND DROP THE TABLE (WITH THE REQUIRED PARAMETERS).
        //TODO DEFINE THE INSERTIONS OF THE TABLE OF FAVORITES ).
        //TODO CREATE THE CONTRACT CLASS
        //TODO CREATE THE BASE COLUMNS EXTENDED CLASS.
        //TODO CREATE THE INTENTS OF THE TABLE

        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);
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
            Picasso.with(m_iv_poster.getContext()).load(parent_activity.getStringExtra("poster_path")).into(m_iv_poster);
        }
        if( parent_activity.hasExtra("synopsys") )
        {
            m_tv_synopsys.setText(parent_activity.getStringExtra("synopsys"));
        }
        if( parent_activity.hasExtra("release_date") )
        {
            m_tv_release_date.setText(getResources().getString(R.string.release_date)+parent_activity.getStringExtra("release_date"));
        }
        Bundle queryBundle = new Bundle();
        queryBundle.putInt(MOVIE_ID_KEY,parent_activity.getIntExtra("movieid",0));
       // getSupportLoaderManager().initLoader(REVIEW_TASK_ID, queryBundle, this);
    }

    @Override
    public Loader<List> onCreateLoader(int id, Bundle args) {
        int movieid=args.getInt(MOVIE_ID_KEY);
        //return new ReviewListTaskLoader(this,movieid);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List> loader, List data) {

    }

    @Override
    public void onLoaderReset(Loader<List> loader) {

    }
}
