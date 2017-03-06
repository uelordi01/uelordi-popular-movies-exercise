package udacity.uelordi.com.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.List;

import udacity.uelordi.com.popularmovies.content.MovieContent;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.onFetchResults;

public class VideoListActivity extends AppCompatActivity implements onFetchResults,VideoListAdapter.ListItemClickListener {
    private static final String TAG = VideoListActivity.class.getSimpleName();

    private FetchVideoList m_video_list_task;
    private TextView m_error_view;
    private ProgressBar m_video_list_progress_bar;

    private RecyclerView mMovie_list;
    private VideoListAdapter m_movie_list_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        String filter="popularity.desc";

        m_video_list_progress_bar=(ProgressBar)findViewById(R.id.pg_movie_list);

        mMovie_list=(RecyclerView)findViewById(R.id.rv_movie_list);

        hideErrorMessage();

        if(NetworkUtils.isOnline(getApplicationContext()))
        {
            m_video_list_task=new FetchVideoList();
            m_video_list_task.setListener(this);
            showLoadingBar();
            m_video_list_task.execute(filter);
        }
        else
        {
            showErrorMessage();
        }


    }

    @Override
    public void OnListAvailable(List<MovieContent> result) {
        hideLoadingBar();
        //Populate the recycler view
        GridLayoutManager gridManager=new GridLayoutManager(VideoListActivity.this,3);
        mMovie_list.setLayoutManager(gridManager);
       // mMovie_list.setHasFixedSize(true);
        m_movie_list_adapter=new VideoListAdapter(VideoListActivity.this,result);
        mMovie_list.setAdapter(m_movie_list_adapter);


        //m_test.setText(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }
    private void showErrorMessage()
    {
        if(m_error_view==null)
        {
            m_error_view=(TextView) findViewById(R.id.connectivity_error);
        }
        m_error_view.setVisibility(View.VISIBLE);
    }
    private void hideErrorMessage()
    {
        if(m_error_view==null)
        {
            m_error_view=(TextView) findViewById(R.id.connectivity_error);
        }
        m_error_view.setVisibility(View.INVISIBLE);
    }
    private void showLoadingBar()
    {
        if(m_video_list_progress_bar==null)
        {
            m_video_list_progress_bar=(ProgressBar)findViewById(R.id.pg_movie_list);
        }
        m_video_list_progress_bar.setVisibility(View.VISIBLE);

    }
    private void hideLoadingBar()
    {
        if(m_video_list_progress_bar==null)
        {
            m_video_list_progress_bar=(ProgressBar)findViewById(R.id.pg_movie_list);
        }
        m_video_list_progress_bar.setVisibility(View.INVISIBLE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemWasClicked=item.getItemId();
        switch (itemWasClicked)
        {
            case R.id.action_filter_popular:
            {
                Log.v(TAG,"filter_popular");
                if(NetworkUtils.isOnline(getApplicationContext())) {
                    m_video_list_task = new FetchVideoList();
                    m_video_list_task.setListener(this);
                    showLoadingBar();
                    m_video_list_task.execute("popularity.desc");
                }
                break;
            }
            case R.id.action_filter_rated:
            {
                Log.v(TAG,"filter_rated");
                if(NetworkUtils.isOnline(getApplicationContext())) {
                    m_video_list_task = new FetchVideoList();
                    m_video_list_task.setListener(this);
                    showLoadingBar();
                    m_video_list_task.execute("vote_average.desc");
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemClick(MovieContent movie) {
        //TODO make your intent for display the detail of the activity.
        Intent my_intent = new Intent(this,MovieDetails.class);
        my_intent.putExtra("poster_path",movie.getPoster_path());
        my_intent.putExtra("title",movie.getTitle());
        my_intent.putExtra("synopsys",movie.getSynopsis());
        my_intent.putExtra("user_rating",movie.getUser_rating());
        my_intent.putExtra("release_date",movie.getRelease_date());
        startActivity(my_intent);
    }
}
