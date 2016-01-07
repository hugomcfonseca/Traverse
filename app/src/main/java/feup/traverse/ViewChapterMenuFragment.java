package feup.traverse;

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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapterMenuFragment extends Fragment {

    private Button btn_viewchapterAudio, btn_viewchapterMaps, btn_viewchapterTextP1, btn_viewchapterTakePicture, btn_viewchapterTextP2;
    private TextView tv_viewchapterAudioname, tv_viewchapterLocalName;
    private TextView tv_viewchapterMusicTimer;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    DataBaseAdapter dataBaseAdapter;

    private Session session;

    boolean firstStart = true;
    boolean pontuationOnMusic = false;
    boolean isPaused = false;
    Handler handler_music = new Handler();
    CountDownTimer music_countdown;
    AssetFileDescriptor afd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_viewchapter_menu, container, false);

        dataBaseAdapter = new DataBaseAdapter(getActivity());
        dataBaseAdapter.open();
        session = new Session(getActivity()); //in oncreate

        btn_viewchapterAudio = (Button) rootView.findViewById(R.id.btn_viewchapter_listen);
        btn_viewchapterTextP1 = (Button) rootView.findViewById(R.id.btn_viewchapter_text_part_i);
        btn_viewchapterTextP2 = (Button) rootView.findViewById(R.id.btn_viewchapter_text_part_ii);
        btn_viewchapterMaps = (Button) rootView.findViewById(R.id.btn_viewchapter_maps);
        btn_viewchapterTakePicture = (Button) rootView.findViewById(R.id.btn_viewchapter_takephoto);
        tv_viewchapterAudioname = (TextView)rootView.findViewById(R.id.tv_view_chapter_audio);
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

                if (!mediaPlayer.isPlaying()) {
                    btn_viewchapterAudio.setText("Listening To");

                    if (firstStart || !isPaused){       //TEST IT
                        try {
                            isStartPrepared(((ViewChapter) getActivity()).value);              //TEST IT
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        handler_music.post(music_timer);
                    }

                    mediaPlayer.start();
                    firstStart = false;
                } else {
                    btn_viewchapterAudio.setText("Paused/Stopped");
                    isPaused = true;
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

        btn_viewchapterTextP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ViewChapter) getActivity()).part = 1;
                ((ViewChapter) getActivity()).flag = 3;
                ((ViewChapter) getActivity()).mPagerAdapter = new ViewChapterTextPagerAdapter(getActivity().getSupportFragmentManager(),3);
                ((ViewChapter) getActivity()).mPager.setAdapter(((ViewChapter) getActivity()).mPagerAdapter);
            }
        });

        btn_viewchapterTextP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ViewChapter) getActivity()).part = 2;
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
                    Toast.makeText(getActivity(), "Congratulations, you have earned X points.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), "Ops, you did not listened to music on time required.", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    /* TODO: Implement sharing on Facebook */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2500) {
            Bitmap image = (Bitmap) data.getExtras().get("data"); // Bitmap that is used to share on FB
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            ShareDialog.show(getActivity(), content);
        } else
            Toast.makeText(getActivity(),"You didn't get any photo. :(", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        handler_music.removeCallbacks(music_timer);

        if (!firstStart)
            music_countdown.cancel();

        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();

        dataBaseAdapter.close();
    }

    private Runnable music_timer = new Runnable() {
        @Override
        public void run() {
            music_countdown = new CountDownTimer(mediaPlayer.getDuration(), 1000) {

                public void onTick(long millisUntilFinished) {

                    String time = String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    tv_viewchapterMusicTimer.setText("( "+ time+" )");
//                    Log.d("DEBUG", "current/total:" + mediaPlayer.getCurrentPosition()/1000 + "/" + mediaPlayer.getDuration()/1000);
                }

                public void onFinish() {
                    handler_music.removeCallbacks(music_timer);
                    tv_viewchapterMusicTimer.setText("( 00:00:00 )");
                    Toast.makeText(getActivity(),"Time to listening to music finished!", Toast.LENGTH_SHORT).show();
            //        Log.d("DEBUG2","current/total:"+mediaPlayer.getCurrentPosition()/1000+"/"+mediaPlayer.getDuration()/1000);
                    if ((mediaPlayer.getCurrentPosition()/1000 == mediaPlayer.getDuration()/1000) && !mediaPlayer.isPlaying()){
                        pontuationOnMusic = true;
                    }
                }
            }.start();
        }
    };

    private void isStartPrepared (int chapter) throws IOException {

        switch(chapter){
            case 1:
                afd = getActivity().getAssets().openFd("audio/Ch1.mp3");
                break;
            case 2:
                afd = getActivity().getAssets().openFd("audio/Ch2.mp3");
                break;
            case 3:
                afd = getActivity().getAssets().openFd("audio/Ch3.mp3");
                break;
            case 4:
                afd = getActivity().getAssets().openFd("audio/Ch4.mp3");
                break;
            case 5:
                afd = getActivity().getAssets().openFd("audio/Ch5.mp3");
                break;
            case 6:
                afd = getActivity().getAssets().openFd("audio/Ch6.mp3");
                break;
            case 7:
                afd = getActivity().getAssets().openFd("audio/Ch7.mp3");
                break;
            case 8:
                afd = getActivity().getAssets().openFd("audio/Ch8.mp3");
                break;
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        mediaPlayer.prepare();
    }

}
