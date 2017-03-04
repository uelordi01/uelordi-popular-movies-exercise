package udacity.uelordi.com.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import udacity.uelordi.com.popularmovies.utils.onFetchResults;

public class VideoListActivity extends AppCompatActivity implements onFetchResults {
    private static final String TAG = VideoListActivity.class.getSimpleName();
    private FetchVideoList m_video_list_task;
    private TextView m_test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        String filter=getResources().getString(R.string.filter_popular);
        m_test=(TextView) findViewById(R.id.test);
        m_video_list_task=new FetchVideoList();
        m_video_list_task.setListener(this);
        m_video_list_task.execute(filter);
    }

    @Override
    public void OnListAvailable(String result) {
        m_test.setText(result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemWasClicked=item.getItemId();
        switch (itemWasClicked)
        {
            case R.id.action_filter_popular:
            {
                Log.v(TAG,"filter_popular");
                m_video_list_task.execute("popularity.desc");
                break;
            }
            case R.id.action_filter_rated:
            {
                Log.v(TAG,"filter_rated");
                m_video_list_task.execute("vote_average.desc");
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
/*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
