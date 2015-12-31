package feup.traverse;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapterMenuFragment extends Fragment {

    private Button btn_viewchapterAudio, btn_viewchapterMaps, btn_viewchapterText, btn_viewchapterTakePicture;
    private TextView tv_viewchapterAudioname, tv_viewchapterTextName, tv_viewchapterLocalName;
    private TextView tv_viewchapterMusicTimer;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    DataBaseAdapter dataBaseAdapter;

    private Session session;

    boolean firstStart = true;
    boolean pontuationOnMusic = false;
    Handler handler_music = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_viewchapter_menu, container, false);

        dataBaseAdapter = new DataBaseAdapter(getActivity());
        dataBaseAdapter.open();
        session = new Session(getActivity()); //in oncreate

        btn_viewchapterAudio = (Button) rootView.findViewById(R.id.btn_viewchapter_listen);
        btn_viewchapterText = (Button) rootView.findViewById(R.id.btn_viewchapter_text);
        btn_viewchapterMaps = (Button) rootView.findViewById(R.id.btn_viewchapter_maps);
        btn_viewchapterTakePicture = (Button) rootView.findViewById(R.id.btn_viewchapter_takephoto);
        tv_viewchapterAudioname = (TextView)rootView.findViewById(R.id.tv_view_chapter_audio);
        tv_viewchapterTextName = (TextView)rootView.findViewById(R.id.tv_viewchapter_chaptername);
        tv_viewchapterLocalName = (TextView)rootView.findViewById(R.id.tv_viewchapter_localname);
        tv_viewchapterMusicTimer = (TextView)rootView.findViewById(R.id.tv_viewchapter_music_timer);

        Cursor cursor = dataBaseAdapter.getChapterInfo(session.getusername(), ((ViewChapter) getActivity()).value);
        tv_viewchapterLocalName.setText(cursor.getString(cursor.getColumnIndex("local")));

        if (((ViewChapter) getActivity()).finished == 1){
            btn_viewchapterMaps.setEnabled(false);
        }

        btn_viewchapterAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final boolean[] play_status = {false};

                if (!mediaPlayer.isPlaying()) {
                    btn_viewchapterAudio.setText("Listening To");
                    isStartPrepared();

                    if (firstStart){
                        handler_music.post(music_timer);
                    }

                    mediaPlayer.start();
                    firstStart = false;
                } else {
                    btn_viewchapterAudio.setText("Paused/Stopped");
                    mediaPlayer.pause();
                }

            }
        });

        btn_viewchapterMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ViewChapter) getActivity()).flag = 2;
                ((ViewChapter) getActivity()).mPagerAdapter = new ViewChapterTextPagerAdapter(getActivity().getSupportFragmentManager(),2);
                ((ViewChapter) getActivity()).mPager.setAdapter(((ViewChapter) getActivity()).mPagerAdapter);
            }
        });

        btn_viewchapterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ViewChapter) getActivity()).flag = 3;
                ((ViewChapter) getActivity()).mPagerAdapter = new ViewChapterTextPagerAdapter(getActivity().getSupportFragmentManager(),3);
                ((ViewChapter) getActivity()).mPager.setAdapter(((ViewChapter) getActivity()).mPagerAdapter);
            }
        });

        btn_viewchapterTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 2500);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler_music.removeCallbacks(music_timer);
                mediaPlayer.stop();
                mediaPlayer.reset();

                if (pontuationOnMusic)
                    Toast.makeText(getActivity(),"Congratulations, you have earned X points.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(),"Ops, you did not listened to music on time required.", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    /* TODO: Implement sharing on Facebook */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2500 && data.getData() != null) {
            Bitmap image = (Bitmap) data.getExtras().get("data"); // Bitmap that is used to share on FB
        } else
            Toast.makeText(getActivity(),"You didn't get any photo. :(", Toast.LENGTH_SHORT).show();
    }

    public void onDestroyView(){
        super.onDestroyView();
        handler_music.removeCallbacks(music_timer);
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    private void isStartPrepared() {
        try {
            AssetFileDescriptor afd = getActivity().getAssets().openFd("audio/"+"Victoria_ch1.mp3");
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Runnable music_timer = new Runnable() {
        @Override
        public void run() {
            new CountDownTimer(mediaPlayer.getDuration(), 1000) {

                public void onTick(long millisUntilFinished) {
                    String time = String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    tv_viewchapterMusicTimer.setText(time);
                    Log.d("DEBUG", "current/total:" + mediaPlayer.getCurrentPosition()/1000 + "/" + mediaPlayer.getDuration()/1000);
                }

                public void onFinish() {
                    handler_music.removeCallbacks(music_timer);
                    tv_viewchapterMusicTimer.setText("00:00:00");
                    Toast.makeText(getActivity(),"Time to listening to music finished!", Toast.LENGTH_SHORT).show();
                    Log.d("DEBUG2","current/total:"+mediaPlayer.getCurrentPosition()/1000+"/"+mediaPlayer.getDuration()/1000);
                    if ((mediaPlayer.getCurrentPosition()/1000 == mediaPlayer.getDuration()/1000) && !mediaPlayer.isPlaying()){
                        pontuationOnMusic = true;
                    }
                }
            }.start();
        }
    };

}
