package udacity.uelordi.com.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import udacity.uelordi.com.popularmovies.content.MovieContent;
import udacity.uelordi.com.popularmovies.utils.NetworkUtils;
import udacity.uelordi.com.popularmovies.utils.onFetchResults;

public class VideoListActivity extends AppCompatActivity implements onFetchResults {
    private static final String TAG = VideoListActivity.class.getSimpleName();
    private FetchVideoList m_video_list_task;
    private TextView m_test;
    private ProgressBar m_video_list_progress_bar;
    RecyclerView mMovie_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        String filter="popularity.desc";//getResources().getString(R.string.filter_popular);

       // m_test=(TextView) findViewById(R.id.test);
        m_video_list_progress_bar=(ProgressBar)findViewById(R.id.pg_movie_list);

        mMovie_list=(RecyclerView)findViewById(R.id.rv_movie_list);
        GridLayoutManager gridManager=new GridLayoutManager(VideoListActivity.this,100);






        m_video_list_task=new FetchVideoList();
        m_video_list_task.setListener(this);
        showLoadingBar();
        m_video_list_task.execute(filter);
    }

    @Override
    public void OnListAvailable(List<MovieContent> result) {
        hideLoadingBar();
        //m_test.setText(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }
    public void showLoadingBar()
    {
        if(m_video_list_progress_bar==null)
        {
            m_video_list_progress_bar=(ProgressBar)findViewById(R.id.pg_movie_list);
        }
        m_video_list_progress_bar.setVisibility(View.VISIBLE);

    }
    public void hideLoadingBar()
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
                m_video_list_task=new FetchVideoList();
                m_video_list_task.setListener(this);
                showLoadingBar();
                m_video_list_task.execute("popularity.desc");
                break;
            }
            case R.id.action_filter_rated:
            {
                Log.v(TAG,"filter_rated");
                m_video_list_task=new FetchVideoList();
                m_video_list_task.setListener(this);
                showLoadingBar();
                m_video_list_task.execute("vote_average.desc");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
