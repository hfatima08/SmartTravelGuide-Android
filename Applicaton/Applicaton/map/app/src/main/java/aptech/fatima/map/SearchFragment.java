package aptech.fatima.map;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import aptech.fatima.map.adapter.AdapterHome;
import aptech.fatima.map.models.ModelHome;
import aptech.fatima.map.models.VideoYT;
import aptech.fatima.map.network.YoutubeAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */

public class SearchFragment extends Fragment {

    private EditText input_query;
    private SearchView btn_search;
    private AdapterHome adapter;
    private LinearLayoutManager manager;
    private List<VideoYT> videoList = new ArrayList<>();
    String url;
    RecyclerView rv;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        btn_search = view.findViewById(R.id.search);
        rv= view.findViewById(R.id.recycler_search);

        adapter = new AdapterHome(getContext(), videoList);
        manager = new LinearLayoutManager(getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);

        String input1=getArguments().getString("data");
        if(input1!=null) {
            getJson(input1);
        }

        btn_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String input=btn_search.getQuery().toString();
                if (!TextUtils.isEmpty(input)){
                    getJson(input);
                } else {
                    Toast.makeText(getContext(), "You need to enter some text", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return view;
    }

    private void getJson(String query) {
        List<Address> addressList = null;
        Geocoder geocoder = new Geocoder(getContext());
        try {
            addressList = geocoder.getFromLocationName(query, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!addressList.isEmpty()) {
            url = YoutubeAPI.BASE_URL + YoutubeAPI.sch + YoutubeAPI.KEY + YoutubeAPI.mx + YoutubeAPI.ord
                    + YoutubeAPI.part + YoutubeAPI.query + query + " beautiful tourism places" + YoutubeAPI.type;
            Call<ModelHome> data = YoutubeAPI.getVideo().getHomeVideo(url);
            data.enqueue(new Callback<ModelHome>() {
                @Override
                public void onResponse(Call<ModelHome> call, Response<ModelHome> response) {
                    if (response.errorBody() != null) {
                        Log.w(TAG, "onResponse search : " + response.errorBody().toString());
                    } else {
                        ModelHome mh = response.body();
                        if (mh.getItems().size() != 0) {
                            videoList.clear();
                            videoList.addAll(mh.getItems());
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getContext(), "No Content Available", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ModelHome> call, Throwable t) {
                    Log.e(TAG, "onFailure search: ", t);
                }
            });
        }
    else{
            Toast.makeText(getContext(), "Enter a Country/City Name", Toast.LENGTH_SHORT).show();
        }
    }

    }



