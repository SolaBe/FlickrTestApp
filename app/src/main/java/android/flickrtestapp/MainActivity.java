package android.flickrtestapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.etsy.android.grid.StaggeredGridView;

import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Bitmap>> {


    private SearchView searchView;
    private StaggeredGridView gridView;
    private ProgressBar bar;
    private AdapterGrid adapter;
    private ContentLoadListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (StaggeredGridView) findViewById(R.id.grid_view);
        bar = (ProgressBar) findViewById(R.id.progress_bar);
        listener = new ContentLoadListener(gridView,bar);
        if(Stored.bitmaps.size() != 0) {
            adapter = new AdapterGrid(this, R.layout.grid_item, Stored.bitmaps);
            gridView.setAdapter(adapter);
            listener.showContentOrLoadingIndicator(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem item  = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Bundle bundle = new Bundle();
                bundle.putString("query",query);
                MainActivity.this.getSupportLoaderManager().restartLoader(0,bundle,MainActivity.this);
                listener.setViewsToInitialState();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Bitmap>> onCreateLoader(int id, Bundle args) {
        Log.w("onLoadCreate", "success");
        Loader<List<Bitmap>> loader = new ImagesLoader(this,(String) args.get("query"));
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Bitmap>> loader, List<Bitmap> data) {
        Stored.bitmaps = data;
        adapter = new AdapterGrid(this,R.layout.grid_item,Stored.bitmaps);
        gridView.setAdapter(adapter);
        listener.showContentOrLoadingIndicator(true);
        Log.w("onLoadFinished", "success");
    }

    @Override
    public void onLoaderReset(Loader<List<Bitmap>> loader) {
        Log.w("onLoadReset","failed");
    }
}
