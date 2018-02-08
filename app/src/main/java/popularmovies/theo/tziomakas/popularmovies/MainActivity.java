package popularmovies.theo.tziomakas.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import popularmovies.theo.tziomakas.popularmovies.data.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("MainActivity","MainActivity onCreate");
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container,new MainActivityFragment()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_settings){

            Context context = MainActivity.this;
            Class activityToBeLauched = SettingsActivity.class;

            Intent intent = new Intent(context,activityToBeLauched);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
