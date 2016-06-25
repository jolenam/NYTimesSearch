package com.example.jolenam.nytimessearch.Activities;

import android.app.FragmentManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jolenam.nytimessearch.Article;
import com.example.jolenam.nytimessearch.ArticleArrayAdapter;
import com.example.jolenam.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.jolenam.nytimessearch.FilterDialogFragment;
import com.example.jolenam.nytimessearch.R;
import com.example.jolenam.nytimessearch.SpacesItemsDecoration;
import com.example.jolenam.nytimessearch.TopArticle;
import com.example.jolenam.nytimessearch.TopArticleAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements FilterDialogFragment.OnFilterSearchListener {

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    RecyclerView rvArticles;

    ArrayList<TopArticle> topStories;
    TopArticleAdapter topAdapter;

    SearchFilters mFilters;

    String savedQuery;

    String sortType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("New York Times Search");

        Typeface custom_font = Typeface.createFromAsset(this.getAssets(), "fonts/NOVABOLD.otf");
        mTitle.setTypeface(custom_font);

        setupViews();

        mFilters = new SearchFilters();

    }



    private void showFiltersDialog() {
        FragmentManager fm = getFragmentManager();
        FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance(mFilters);
        filterDialogFragment.show(fm, "fragment_filter");
    }


    public void setupViews() {
        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);
    }

    public void customLoadMoreDataFromApi(final int page, final SearchFilters filters) {


        // set parameters
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();

        params.put("api-key", "7075fa7943644766a780d669cacbd68b");
        params.put("page", page);
        params.put("q", savedQuery);

        if (filters != null ) {

            /*String month = searchFilter.getMonth();
            String day = searchFilter.getDay();
            String year = searchFilter.getYear();

            final Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, Integer.parseInt(year));
            cal.set(Calendar.MONTH, Integer.parseInt(month));
            cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String urlDate = format.format(cal.getTime());

            params.put("begin_date", urlDate);*/

            //done below now! in onUpdateFilter
            //String sortType = filters.getSortType();

            if (sortType.toLowerCase().equals("newest")) {
                params.put("sort", "newest");
            }
            else if (sortType.toLowerCase().equals("oldest")) {
                params.put("sort", "oldest");
            }

            // figure out newsdesk

        }


        Log.d("searchactivity", url);
        // make network request
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                if (page == 0) {
                    rvArticles.clearOnScrollListeners();
                    final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                    rvArticles.setLayoutManager(staggeredGridLayoutManager);
                    rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
                        @Override
                        public void onLoadMore(int page, int totalItemsCount) {
                            customLoadMoreDataFromApi(page, filters);
                        }
                    });
                    articles.clear();
                }

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    int curSize = adapter.getItemCount();
                    adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setQueryHint("Get news on...");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // allows for search results following first search to have endless scrolling
                rvArticles.clearOnScrollListeners();

                articles = new ArrayList<>();
                adapter = new ArticleArrayAdapter(articles);

                rvArticles.setAdapter(adapter);
                final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
                staggeredGridLayoutManager.setGapStrategy(
                        StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
                rvArticles.setLayoutManager(staggeredGridLayoutManager);

                SpacesItemsDecoration decoration = new SpacesItemsDecoration(10);
                rvArticles.addItemDecoration(decoration);

                rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        customLoadMoreDataFromApi(page, null);

                    }
                });

                savedQuery = query;

                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
                RequestParams params = new RequestParams();

                params.put("api-key", "7075fa7943644766a780d669cacbd68b");
                params.put("page", 0);
                params.put("q", query);

                Log.d("searchactivity", url);
                // make network request
                client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        JSONArray articleJsonResults = null;

                        try {
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            Log.d("DEBUG", articleJsonResults.toString());
                            articles.clear();
                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            adapter.notifyDataSetChanged();
                            Log.d("DEBUG", articles.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_Filter) {
            showFiltersDialog();
            return true;
        }

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    // FOR NEW ACTIVITY filters
    /*private final int REQUEST_CODE = 20;

    public void onFilter(MenuItem item) {
        Intent i = new Intent(this, FilterActivity.class);

        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            searchFilter = (SearchFilters) data.getSerializableExtra("filter");
        }
        customLoadMoreDataFromApi(0);
    }*/

    // 3. This method is invoked in the activity after dialog is dismissed
    // Access the filters result passed to the activity here
   @Override
    public void onUpdateFilters(SearchFilters filters) {
        // 1. Access the updated filters here and store them in member variable
       sortType =  filters.getSortType();
        // 2. Initiate a fresh search with these filters updated and same query value
        customLoadMoreDataFromApi(0, filters);

    }

}
