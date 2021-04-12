package aptech.fatima.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

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


public class YTPlayerActivity extends AppCompatActivity {

    private YouTubePlayerView ytPlayer;
    private TextView videoTitle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ytplayer);
        ytPlayer = findViewById(R.id.yt_player);
        videoTitle = findViewById(R.id.video_title);

        Intent data = getIntent();
        final String videoId = data.getStringExtra("video_id");
        String video_title = data.getStringExtra("video_title");

        ytPlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId,0);
            }
        });

        videoTitle.setText(video_title);


    }

}
