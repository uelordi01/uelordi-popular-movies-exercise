package udacity.uelordi.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView m_tv_title;
        ImageView m_iv_poster;
        TextView m_tv_user_rating;
        TextView m_tv_synopsys;
        TextView m_tv_release_date;
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_movie_details);
        Intent parent_activity=getIntent();
        if( parent_activity.hasExtra("title") )
        {
            m_tv_title = (TextView)findViewById(R.id.tv_detail_title);
            m_tv_title.setText(parent_activity.getStringExtra("title"));
        }
        if( parent_activity.hasExtra("user_rating") )
        {
            m_tv_user_rating = (TextView)findViewById(R.id.tv_detail_user_rating);
            m_tv_user_rating.setText(parent_activity.getStringExtra("user_rating"));
        }
        if( parent_activity.hasExtra("poster_path") )
        {
            m_iv_poster = (ImageView)findViewById(R.id.iv_poster_detail_path);
            Picasso.with(m_iv_poster.getContext()).load(parent_activity.getStringExtra("poster_path")).into(m_iv_poster);
        }
        if( parent_activity.hasExtra("synopsys") )
        {
            m_tv_synopsys = (TextView) findViewById(R.id.tv_detail_synopsys);
            m_tv_synopsys.setText(parent_activity.getStringExtra("synopsys"));
        }
        if( parent_activity.hasExtra("release_date") )
        {
            m_tv_release_date= (TextView) findViewById(R.id.tv_detail_release_date);
            m_tv_release_date.setText(getResources().getString(R.string.release_date)+parent_activity.getStringExtra("release_date"));
        }

       // my_intent.putExtra("poster_path",movie.getPoster_path());
        //my_intent.putExtra("title",movie.getTitle());
        //my_intent.putExtra("synopsys",movie.getSynopsis());
        //my_intent.putExtra("user_rating",movie.getUser_rating());
        //my_intent.putExtra("release_date",movie.getRelease_date());

    }
}
