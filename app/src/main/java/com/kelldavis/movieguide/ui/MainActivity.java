package com.kelldavis.movieguide.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kelldavis.movieguide.R;
import com.kelldavis.movieguide.adapter.MovieAdapter;
import com.kelldavis.movieguide.listener.MovieApiClient;
import com.kelldavis.movieguide.listener.OnLoadMoreListener;
import com.kelldavis.movieguide.model.Movie;
import com.kelldavis.movieguide.model.MovieResults;
import com.kelldavis.movieguide.utilities.MovieApiService;
import com.kelldavis.movieguide.utilities.Utils;
import com.kelldavis.movieguide.viewmodel.MovieDetailsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kelldavis.movieguide.utilities.Constants.API_KEY;
import static com.kelldavis.movieguide.utilities.Constants.GRID_LAYOUT;
import static com.kelldavis.movieguide.utilities.Constants.MOST_POPULAR_OPTION_CHECKED;
import static com.kelldavis.movieguide.utilities.Constants.TOP_RATED_OPTION_CHECKED;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.errorLayout)
    ScrollView errorLayout;
    @BindView(R.id.errorImage)
    ImageView errorImage;
    @BindView(R.id.errorText)
    TextView errorText;
    @BindView(R.id.errorButton)
    Button errorButton;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    private List<Movie> movies;
    private MovieAdapter movieAdapter;

    private MovieApiClient client;
    private Call<MovieResults> call;

    private Toast toast;

    private int mostPopularMoviesStartPage = 1;
    private int topRatedMoviesStartPage = 1;
    private MenuItem mostPopularMenuItem;
    private MenuItem topRatedMenuItem;
    private MenuItem favoritesMenuItem;
    private boolean mostPopularOptionChecked = true;
    private boolean topRatedOptionChecked = false;
    private boolean fromErrorButton;

    private MovieDetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //resolve references to views
        ButterKnife.bind(this);

        //initialize movies array
        movies = new ArrayList<>();

        //set up RecyclerView - define caching properties and default animator
        Utils.setupRecyclerView(this, recyclerView, GRID_LAYOUT);

        //set up adapter
        movieAdapter = new MovieAdapter(this, movies, recyclerView);
        recyclerView.setAdapter(movieAdapter);
        //set up pagination listener
        movieAdapter.setOnLoadMoreListener(this);

        //initialize view model
        viewModel = ViewModelProviders.of(this)
                .get(MovieDetailsViewModel.class);
        viewModel.getFavoriteMovies().observe(this, favorites -> {
            if (favorites != null && !mostPopularOptionChecked && !topRatedOptionChecked) {
                if (movies == null) {
                    movies = new ArrayList<>();
                } else {
                    movies.clear();
                    movieAdapter.notifyDataSetChanged();
                }
                movies.addAll(favorites);
                movieAdapter.notifyDataSetChanged();

                if (movies.size() == 0) {
                    updateEmptyStateViews(R.drawable.no_search_results, R.string.no_favorites, R.drawable.ic_error_outline, R.string.browse_movies);
                }
            }
        });

        //register refresh layout listener
        refreshLayout.setOnRefreshListener(this);
        //customize refresh layout color scheme
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));

        //get reference to TMDB API client
        client = MovieApiService.getClient().create(MovieApiClient.class);

        //set up button click listener for error/empty state views
        errorButton.setOnClickListener(this);

        //restore any previously saved movies sort order on activity state changed
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOST_POPULAR_OPTION_CHECKED)) {
                mostPopularOptionChecked = savedInstanceState.getBoolean(MOST_POPULAR_OPTION_CHECKED);
                topRatedOptionChecked = savedInstanceState.getBoolean(TOP_RATED_OPTION_CHECKED);
            }
        }

        //call API based on the selected sort order - popular movies being default
        if (mostPopularOptionChecked) {
            getPopularMovies();
        } else if (topRatedOptionChecked){
            getTopRatedMovies();
        } else {
            //TODO add check to display favorites and enable favorites menu item
        }
    }

    /**
     * inflates the menu on the action bar
     *
     * @param menu reference to the menu in which to inflate the options
     * @return boolean flag indicating whether inflating was successfully handled
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        //get references to the movie sort order menu items
        mostPopularMenuItem = menu.findItem(R.id.sort_most_popular);
        topRatedMenuItem = menu.findItem(R.id.sort_top_rated);
        favoritesMenuItem = menu.findItem(R.id.sort_favorites);

        return true;
    }

    /**
     * handle selection of menu items
     *
     * @param item reference to the selected menu item
     * @return boolean flag indicating whether menu item selection action was successfully handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //verify the selected menu option
        switch (item.getItemId()) {
            case R.id.sort_most_popular:
                //if the item is already selected, exit early
                if (item.isChecked()) {
                    return false;
                } else {
                    errorLayout.setVisibility(View.GONE);
                    //display progress indicator
                    progressBar.setVisibility(View.VISIBLE);
                    //set the item as selected
                    item.setChecked(true);
                    //unregister pagination listener before invoking the API to avoid unexpected behaviours
                    movieAdapter.setOnLoadMoreListener(null);
                    //clear the list, notify the adapter
                    movies.clear();
                    movieAdapter.notifyDataSetChanged();

                    //reset the page number to load from the beginning
                    mostPopularMoviesStartPage = 1;
                    //display popular movies
                    getPopularMovies();
                }
                break;
            case R.id.sort_top_rated:
                //if the item is already selected, exit early
                if (item.isChecked()) {
                    return false;
                } else {
                    errorLayout.setVisibility(View.GONE);
                    //display progress indicator
                    progressBar.setVisibility(View.VISIBLE);
                    //set the item as selected
                    item.setChecked(true);
                    //unregister pagination listener before invoking the API to avoid unexpected behaviours
                    movieAdapter.setOnLoadMoreListener(null);
                    //clear the list, notify the adapter
                    movies.clear();
                    movieAdapter.notifyDataSetChanged();

                    //reset the page number to load from the beginning
                    topRatedMoviesStartPage = 1;
                    //display top rated movies
                    getTopRatedMovies();
                }
                break;
            case R.id.sort_favorites:
                //if the item is already selected, exit early
                if (item.isChecked()) {
                    return false;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    //set the item as selected
                    item.setChecked(true);
                    movies.clear();
                    movieAdapter.notifyDataSetChanged();
                    movieAdapter.setOnLoadMoreListener(null);
                    refreshLayout.setEnabled(false);

                    mostPopularOptionChecked = false;
                    topRatedOptionChecked = false;

                    progressBar.setVisibility(View.GONE);
                    if (viewModel.getFavoriteMovies().getValue().size() > 0) {
                        movies.addAll(viewModel.getFavoriteMovies().getValue());
                        movieAdapter.notifyDataSetChanged();
                    } else {
                        updateEmptyStateViews(R.drawable.no_search_results, R.string.no_favorites, R.drawable.ic_error_outline, R.string.browse_movies);
                    }
                }
                break;
        }
        return true;
    }

    /**
     * called before displaying the menu to the user
     *
     * @param menu reference to the menu item that can be modified
     * @return boolean flag indicating whether any required modifications were successfully handled
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //set the checked item based on the boolean flag saved along activity lifecycle
        if (mostPopularOptionChecked) {
            mostPopularMenuItem.setChecked(true);
        } else if (topRatedOptionChecked){
            topRatedMenuItem.setChecked(true);
        } else {
            favoritesMenuItem.setChecked(true);
        }
        return true;
    }

    /**
     * invoke TMDB API To fetch movies sorted by popularity
     */
    private void getPopularMovies() {

        //exit early if internet is not connected
        if (Utils.checkInternetConnection(this)) {
            updateEmptyStateViews(R.drawable.no_internet_connection, R.string.no_internet_connection, R.drawable.ic_cloud_off, R.string.error_try_again);
            return;
        }

        //set boolean flag to indicate the sort order chosen
        mostPopularOptionChecked = true;
        topRatedOptionChecked = false;

        //define the call object that wraps the API response
        call = client.getPopularMovies(API_KEY, mostPopularMoviesStartPage);
        //invoke the call asynchronously
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(@NonNull Call<MovieResults> call, @NonNull Response<MovieResults> response) {

                //remove pagination loading indicator
                movieAdapter.removeLoader(null);
                //hide refresh layout progress indicator
                refreshLayout.setRefreshing(false);
                //enable refresh action
                refreshLayout.setEnabled(true);

                //verify if the response body or the fetched results are empty/null
                if (response.body() == null || response.body().getResults() == null || response.body().getResults().size() == 0) {
                    updateEmptyStateViews(R.drawable.no_search_results, R.string.no_search_results, R.drawable.ic_movie, R.string.error_no_results);
                    return;
                }

                //update data set, notify the adapter
                movies.addAll(response.body().getResults());
                movieAdapter.notifyDataSetChanged();

                //hide progress indicator and empty state views
                errorLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                //display recycler view
                recyclerView.setVisibility(View.VISIBLE);

                //notify the adapter that a new page data load is complete
                movieAdapter.setLoading(false);
                //increment the page number
                mostPopularMoviesStartPage++;

                //enable pagination in case user wants to load the next page
                movieAdapter.setOnLoadMoreListener(MainActivity.this);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResults> call, @NonNull Throwable t) {
                //display error messages on failure
                updateEmptyStateViews(R.drawable.no_search_results, R.string.no_search_results, R.drawable.ic_error_outline, R.string.browse_movies);
            }
        });
    }


    /**
     * invoke TMDB API To fetch movies sorted by ratings
     */
    private void getTopRatedMovies() {

        //exit early if internet is not connected
        if (Utils.checkInternetConnection(this)) {
            updateEmptyStateViews(R.drawable.no_internet_connection, R.string.no_internet_connection, R.drawable.ic_cloud_off, R.string.error_try_again);
            return;
        }

        //set boolean flag to indicate the sort order chosen
        mostPopularOptionChecked = false;
        topRatedOptionChecked = true;

        //define the call object that wraps the API response
        call = client.getTopRatedMovies(API_KEY, topRatedMoviesStartPage);
        //invoke the call asynchronously
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(@NonNull Call<MovieResults> call, @NonNull Response<MovieResults> response) {

                //remove pagination loading indicator
                movieAdapter.removeLoader(null);
                //hide refresh layout progress indicator
                refreshLayout.setRefreshing(false);
                //enable refresh action
                refreshLayout.setEnabled(true);

                //verify if the response body or the fetched results are empty/null
                if (response.body() == null || response.body().getResults() == null || response.body().getResults().size() == 0) {
                    updateEmptyStateViews(R.drawable.no_search_results, R.string.no_search_results, R.drawable.ic_movie, R.string.error_no_results);
                    return;
                }

                //update data set, notify the adapter
                movies.addAll(response.body().getResults());
                movieAdapter.notifyDataSetChanged();

                //hide progress indicator and empty state views
                errorLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                //display recycler view
                recyclerView.setVisibility(View.VISIBLE);

                //notify the adapter that a new page data load is complete
                movieAdapter.setLoading(false);
                //increment the page number
                topRatedMoviesStartPage++;

                //enable pagination in case user wants to load the next page
                movieAdapter.setOnLoadMoreListener(MainActivity.this);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResults> call, @NonNull Throwable t) {
                //display error messages on failure
                updateEmptyStateViews(R.drawable.no_search_results, R.string.no_search_results, R.drawable.ic_error_outline, R.string.error_no_results);
            }
        });
    }

    /**
     * called on API call failure or internet connection failure
     *
     * @param errorImage        resource ID of the image to be displayed indicating the error
     * @param errorText         user understandable error message
     * @param errorTextDrawable icon indicating the error
     * @param errorButtonText   text for the error button, prompting the user for an action
     */
    private void updateEmptyStateViews(int errorImage, int errorText, int errorTextDrawable, int errorButtonText) {
        //disable refresh progress indicator
        refreshLayout.setRefreshing(false);

        //display the error layout
        errorLayout.setVisibility(View.VISIBLE);
        //hide progress indicator and RecyclerView
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        //update error images/text according to the error
        this.errorImage.setImageResource(errorImage);
        this.errorText.setText(errorText);
        this.errorText.setCompoundDrawablesWithIntrinsicBounds(0, errorTextDrawable, 0, 0);
        errorButton.setText(errorButtonText);
    }

    /**
     * called on swipe to refresh
     */
    @Override
    public void onRefresh() {

        //hide all the views
        recyclerView.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);

        //hide progress indicator when refresh indicator is displayed on swipe
        //rather than user clicking on "Try Again" when the internet connection is down
        if (!fromErrorButton) {
            progressBar.setVisibility(View.GONE);
        } else {
            displayToast(getString(R.string.trying_again_alert));
            fromErrorButton = false;
        }

        //disable pagination to avoid unexpected results
        movieAdapter.setOnLoadMoreListener(null);
        movieAdapter.setLoading(false);

        //reset page counters
        mostPopularMoviesStartPage = 1;
        topRatedMoviesStartPage = 1;

        //clear the list, notify the adapter
        movies.clear();
        movieAdapter.notifyDataSetChanged();

        //invoke API call based on selected sort order
        if (mostPopularMenuItem.isChecked()) {
            getPopularMovies();
        } else if (topRatedMenuItem.isChecked()) {
            getTopRatedMovies();
        }
    }

    /**
     * invoked on loading a new page from the API results for pagination
     */
    @Override
    public void onLoadMore() {

        //disable swipe to refresh to avoid unexpected results
        refreshLayout.setEnabled(false);

        //add a progress loading indicator at the bottom of the RecyclerView
        movieAdapter.addLoader(null);

        //invoke API call based on selected sort order
        if (mostPopularMenuItem.isChecked()) {
            getPopularMovies();
        } else if (topRatedMenuItem.isChecked()) {
            getTopRatedMovies();
        }
    }

    /**
     * handle view clicks
     *
     * @param view reference to the view that receives the click event
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.errorButton:
                if (((Button) view).getText().toString().trim().equalsIgnoreCase(getString(R.string.error_try_again))) {
                    //call refresh action on clicking "Try Again"
                    progressBar.setVisibility(View.VISIBLE);
                    fromErrorButton = true;
                    onRefresh();
                } else if (((Button) view).getText().toString().trim().equalsIgnoreCase(getString(R.string.browse_movies))){
                    errorLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    mostPopularMoviesStartPage = 1;
                    topRatedMoviesStartPage = 1;
                    mostPopularMenuItem.setChecked(true);
                    getPopularMovies();
                } else {
                    //close the app on server error
                    finish();
                }
                break;
        }
    }

    /**
     * displays the specified message as a toast to alert the user
     *
     * @param message message to be displayed
     */
    private void displayToast(String message) {
        if (toast != null) {
            //cancel any outstanding toasts
            toast.cancel();
        }
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * called before activity gets destroyed
     *
     * @param outState bundle object that can hold any state that we want to save
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //save a flag indicating if the default movie sort order (sort by most popular) is checked
        outState.putBoolean(MOST_POPULAR_OPTION_CHECKED, mostPopularOptionChecked);
        outState.putBoolean(TOP_RATED_OPTION_CHECKED, topRatedOptionChecked);
        super.onSaveInstanceState(outState);
    }
}

