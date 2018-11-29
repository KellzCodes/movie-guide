package com.kelldavis.movieguide.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kelldavis.movieguide.R;
import com.kelldavis.movieguide.adapter.MovieCastAdapter;
import com.kelldavis.movieguide.listener.MovieApiClient;
import com.kelldavis.movieguide.model.Cast;
import com.kelldavis.movieguide.model.Credits;
import com.kelldavis.movieguide.model.Movie;
import com.kelldavis.movieguide.utilities.MovieApiService;
import com.kelldavis.movieguide.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kelldavis.movieguide.utilities.Constants.API_KEY;
import static com.kelldavis.movieguide.utilities.Constants.GRID_LAYOUT;
import static com.kelldavis.movieguide.utilities.Constants.MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieCastFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyTextView)
    TextView emptyTextView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private List<Cast> castList;


    public MovieCastFragment() {
        // Required empty public constructor
    }


    /**
     * inflates the view for the fragment
     *
     * @param inflater           reference to inflater service
     * @param container          parent for the fragment
     * @param savedInstanceState reference to bundle object that can be used to save activity states
     * @return inflated view for fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_cast, container, false);
    }

    /**
     * called after onCreateView returns - resolve references to child views here
     *
     * @param view               reference to created view that can be modified
     * @param savedInstanceState reference to bundle object that can be used to save activity states
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        //set up RecyclerView - define caching properties and default animator
        Utils.setupRecyclerView(getContext(), recyclerView, GRID_LAYOUT);

        //initialize data set and set up the adapter
        castList = new ArrayList<>();
        final MovieCastAdapter adapter = new MovieCastAdapter(getContext(), castList);
        recyclerView.setAdapter(adapter);

        //initialize retrofit client and call object that wraps the response
        MovieApiClient client = MovieApiService.getClient().create(MovieApiClient.class);
        //invoke movie credits call passing the movie id and API KEY
        Call<Credits> call = client.getMovieCredits(((Movie) getArguments().getParcelable(MOVIE)).getMovieId(), API_KEY);
        //invoke API call asynchronously
        call.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(@NonNull Call<Credits> call, @NonNull Response<Credits> response) {
                progressBar.setVisibility(View.GONE);
                //verify if the response body or the fetched results are empty/null
                if (response.body() == null || response.body().getCast() == null) {
                    return;
                }

                //update data set, notify the adapter
                //update view visibility accordingly
                if (response.body().getCast().size() > 0) {
                    castList.addAll(response.body().getCast());
                    adapter.notifyDataSetChanged();
                    emptyTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    emptyTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Credits> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), R.string.error_movie_cast, Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * return new instance of fragment with movie data passed in as arguments
     *
     * @param movie reference to movie object set as one of fragment's arguments
     * @return instance of fragment
     */
    public static Fragment newInstance(Movie movie) {
        MovieCastFragment fragment = new MovieCastFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }
}

