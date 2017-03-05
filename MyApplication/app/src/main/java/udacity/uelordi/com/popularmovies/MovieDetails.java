package udacity.uelordi.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MovieDetails extends AppCompatActivity {
    TextView m_tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent parent_activity=getIntent();
        if(parent_activity.hasExtra("title"))
        {
            m_tv_title=(TextView)findViewById(R.id.tv_detail_title);
            m_tv_title.setText(parent_activity.getStringExtra("title"));
        }

    }
}
