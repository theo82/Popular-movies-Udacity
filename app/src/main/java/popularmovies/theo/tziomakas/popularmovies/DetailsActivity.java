package popularmovies.theo.tziomakas.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.moview_container,new DetailsFragment()).commit();
        }
    }
}
