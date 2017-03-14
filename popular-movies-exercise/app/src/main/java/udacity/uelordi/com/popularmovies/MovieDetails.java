package udacity.uelordi.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetails extends AppCompatActivity {
    @BindView (R.id.tv_detail_title)TextView m_tv_title;
    @BindView (R.id.iv_poster_detail_path)ImageView m_iv_poster;
    @BindView (R.id.tv_detail_user_rating)TextView m_tv_user_rating;
    @BindView (R.id.tv_detail_synopsys)TextView m_tv_synopsys;
    @BindView (R.id.tv_detail_release_date)TextView m_tv_release_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
    }
}
