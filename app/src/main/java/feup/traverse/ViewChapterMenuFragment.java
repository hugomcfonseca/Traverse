package feup.traverse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapterMenuFragment extends Fragment {

    private Button btn_viewchapterAudio, btn_viewchapterMaps, btn_viewchapterTextP1, btn_viewchapterTakePicture, btn_viewchapterTextP2;
    private TextView tv_viewchapterAudioname, tv_viewchapterLocalName, tv_viewchapter_menu_chapterTitle, tv_viewchapter_menu_chapterNumber;
    private TextView tv_viewchapterMusicTimer, instructions_text;
    private ImageView im_viewchapter_menu_Music, im_viewchapter_secretHint;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

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

        int[] checker_phase = dataBaseAdapter.getScoreFlags(session.getusername(), ((ViewChapter) getActivity()).value);

        btn_viewchapterAudio = (Button) rootView.findViewById(R.id.btn_viewchapter_listen);
        btn_viewchapterTextP1 = (Button) rootView.findViewById(R.id.btn_viewchapter_text_part_i);
        btn_viewchapterTextP2 = (Button) rootView.findViewById(R.id.btn_viewchapter_text_part_ii);
        btn_viewchapterMaps = (Button) rootView.findViewById(R.id.btn_viewchapter_maps);
        btn_viewchapterTakePicture = (Button) rootView.findViewById(R.id.btn_viewchapter_takephoto);
        tv_viewchapterAudioname = (TextView)rootView.findViewById(R.id.tv_view_chapter_audio);
        tv_viewchapterLocalName = (TextView)rootView.findViewById(R.id.tv_viewchapter_localname);
        tv_viewchapterMusicTimer = (TextView)rootView.findViewById(R.id.tv_viewchapter_music_timer);
        im_viewchapter_menu_Music = (ImageView)rootView.findViewById(R.id.im_viewchapter_menu_music);
        tv_viewchapter_menu_chapterTitle = (TextView)rootView.findViewById(R.id.tv_viewchapter_chapter_title);
        tv_viewchapter_menu_chapterNumber = (TextView)rootView.findViewById(R.id.tv_viewchapter_chapter_number);
        im_viewchapter_secretHint = (ImageView)rootView.findViewById(R.id.im_viewchapter_secret_hint);

        lockedButtons();
        secretThing();

        Cursor cursor = dataBaseAdapter.getChapterInfo(session.getusername(), ((ViewChapter) getActivity()).value);
        tv_viewchapterLocalName.setText(cursor.getString(cursor.getColumnIndex("local")));

        Cursor cursor2 = dataBaseAdapter.getInfoByChapter(session.getusername(), ((ViewChapter) getActivity()).value);
        tv_viewchapter_menu_chapterTitle.setText(cursor2.getString(cursor2.getColumnIndex("chapter_name")));
        tv_viewchapter_menu_chapterNumber.setText("Chapter " + ((ViewChapter) getActivity()).value);

        if (((ViewChapter) getActivity()).finished == 1){
            btn_viewchapterMaps.setEnabled(false);
        }

        btn_viewchapterAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mediaPlayer.isPlaying()) {
                    btn_viewchapterAudio.setText("Listening To");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        im_viewchapter_menu_Music.setImageDrawable(getResources().getDrawable(R.drawable.play_logo, getActivity().getTheme()));
                    } else {
                        im_viewchapter_menu_Music.setImageDrawable(getResources().getDrawable(R.drawable.play_logo));
                    }

                    if (firstStart || !isPaused){
                        try {
                            isStartPrepared(((ViewChapter) getActivity()).value);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (((ViewChapter)getActivity()).value != 1){
                            int[] val = {1,0,0,0,0};
                            dataBaseAdapter.updateChallengesByChapter(session.getusername(),((ViewChapter)getActivity()).value,val);
                        }

                        handler_music.post(music_timer);
                    }

                    mediaPlayer.start();
                    firstStart = false;
                } else {
                    btn_viewchapterAudio.setText("Paused/Stopped");
                    isPaused = true;
                    mediaPlayer.pause();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        im_viewchapter_menu_Music.setImageDrawable(getResources().getDrawable(R.drawable.pause_logo, getActivity().getTheme()));
                    } else {
                        im_viewchapter_menu_Music.setImageDrawable(getResources().getDrawable(R.drawable.pause_logo));
                    }
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
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);

                if (cm.getActiveNetworkInfo() != null){
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 2500);
                } else {
                    Toast.makeText(getActivity(),"Please, enable your wifi or data services.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                handler_music.removeCallbacks(music_timer);
                mediaPlayer.stop();
                mediaPlayer.reset();

                int[] checker_phase = dataBaseAdapter.getScoreFlags(session.getusername(), ((ViewChapter) getActivity()).value);

                if (pontuationOnMusic && checker_phase[0] == 0 && checker_phase[1] == 0 && checker_phase[2] == 0 && checker_phase[3] == 0 && checker_phase[4] == 0) {
                    Toast.makeText(getActivity(), "Congratulations, you have earned 20 points.", Toast.LENGTH_LONG).show();
                    dataBaseAdapter.updateScore(20, session.getusername(), ((ViewChapter) getActivity()).value);
                } else if (!pontuationOnMusic && checker_phase[0] == 0 && checker_phase[1] == 0 && checker_phase[2] == 0 && checker_phase[3] == 0 && checker_phase[4] == 0){
                    Toast.makeText(getActivity(), "Ops, you did not listened to music on time required.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Sorry, you already listened to this audio file.", Toast.LENGTH_LONG).show();
                }

                if (((ViewChapter)getActivity()).value == 1 && checker_phase[0] == 0 && checker_phase[1] == 0 && checker_phase[2] == 0 && checker_phase[3] == 0 && checker_phase[4] == 0){
                    int[] val = {1,0,0,0,0};
                    dataBaseAdapter.updateChallengesByChapter(session.getusername(),((ViewChapter)getActivity()).value,val);

                    ((ViewChapter) getActivity()).flag = 1;
                    ((ViewChapter) getActivity()).mPagerAdapter = new ViewChapterTextPagerAdapter(getActivity().getSupportFragmentManager(),1);
                    ((ViewChapter) getActivity()).mPager.setAdapter(((ViewChapter) getActivity()).mPagerAdapter);
                } else ;

            }
        });

        return rootView;
    }

    /* TODO: Implement sharing on Facebook */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2500 && resultCode == getActivity().RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data"); // Bitmap that is used to share on FB
            Bitmap scaleBitmap =Bitmap.createScaledBitmap(image,800,800,true);
            int[] checker_phase = dataBaseAdapter.getScoreFlags(session.getusername(), ((ViewChapter) getActivity()).value);

            if (checker_phase[0] == 1 && checker_phase[1] == 1 && checker_phase[2] == 1 && checker_phase[3] == 1 && checker_phase[4] == 0){
                int[] val = {0,0,0,0,1};
                dataBaseAdapter.updateChallengesByChapter(session.getusername(), ((ViewChapter) getActivity()).value, val);
                Toast.makeText(getActivity(), "Congratulations, you have earned 20 points.", Toast.LENGTH_LONG).show();
                dataBaseAdapter.updateScore(20, session.getusername(), ((ViewChapter) getActivity()).value);
            }

            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(scaleBitmap)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            ShareDialog.show(getActivity(), content);

        } else {
            Toast.makeText(getActivity(), "You didn't get any photo. :(", Toast.LENGTH_SHORT).show();
            int[] val = {0,0,0,0,1};
            dataBaseAdapter.updateChallengesByChapter(session.getusername(), ((ViewChapter) getActivity()).value, val);
        }
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
            music_countdown = new CountDownTimer(mediaPlayer.getDuration()+10*1000, 1000) {

                public void onTick(long millisUntilFinished) {

                    String time = String.format("%02d:%02d:%02d",
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) -
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                    tv_viewchapterMusicTimer.setText("( "+ time+" )");

                    pontuationOnMusic = true;
                }

                public void onFinish() {
                    tv_viewchapterMusicTimer.setText("( 00:00:00 )");
                    Toast.makeText(getActivity(),"Time to listening to music finished!", Toast.LENGTH_SHORT).show();
                    if ((mediaPlayer.getCurrentPosition()/1000 == mediaPlayer.getDuration()/1000)){
                        pontuationOnMusic = true;
                    }
                    else
                        pontuationOnMusic = false;

                    handler_music.removeCallbacks(music_timer);
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

    private void lockedButtons (){
        Cursor cursor  = dataBaseAdapter.getInfoByChapter(session.getusername(),((ViewChapter)getActivity()).value);

        if (cursor.getInt(cursor.getColumnIndex("score_audio")) == 0 && cursor.getInt(cursor.getColumnIndex("score_text1")) == 0
                && cursor.getInt(cursor.getColumnIndex("score_maps")) == 0 && cursor.getInt(cursor.getColumnIndex("score_text2")) == 0
                && cursor.getInt(cursor.getColumnIndex("score_sharing")) == 0 ){
            btn_viewchapterAudio.setEnabled(true);
            btn_viewchapterTextP1.setEnabled(false);
            btn_viewchapterMaps.setEnabled(false);
            btn_viewchapterTextP2.setEnabled(false);
            btn_viewchapterTakePicture.setEnabled(false);
        } else if (cursor.getInt(cursor.getColumnIndex("score_audio")) == 1 && cursor.getInt(cursor.getColumnIndex("score_text1")) == 0
                && cursor.getInt(cursor.getColumnIndex("score_maps")) == 0 && cursor.getInt(cursor.getColumnIndex("score_text2")) == 0
                && cursor.getInt(cursor.getColumnIndex("score_sharing")) == 0 ){
            btn_viewchapterAudio.setEnabled(true);
            btn_viewchapterTextP1.setEnabled(true);
            btn_viewchapterMaps.setEnabled(false);
            btn_viewchapterTextP2.setEnabled(false);
            btn_viewchapterTakePicture.setEnabled(false);
        } else if (cursor.getInt(cursor.getColumnIndex("score_audio")) == 1 && cursor.getInt(cursor.getColumnIndex("score_text1")) == 1
                && cursor.getInt(cursor.getColumnIndex("score_maps")) == 0 && cursor.getInt(cursor.getColumnIndex("score_text2")) == 0
                && cursor.getInt(cursor.getColumnIndex("score_sharing")) == 0 ){
            btn_viewchapterAudio.setEnabled(true);
            btn_viewchapterTextP1.setEnabled(true);
            btn_viewchapterMaps.setEnabled(true);
            btn_viewchapterTextP2.setEnabled(false);
            btn_viewchapterTakePicture.setEnabled(false);
        } else if (cursor.getInt(cursor.getColumnIndex("score_audio")) == 1 && cursor.getInt(cursor.getColumnIndex("score_text1")) == 1
                && cursor.getInt(cursor.getColumnIndex("score_maps")) == 1 && cursor.getInt(cursor.getColumnIndex("score_text2")) == 0
                && cursor.getInt(cursor.getColumnIndex("score_sharing")) == 0 ){
            btn_viewchapterAudio.setEnabled(true);
            btn_viewchapterTextP1.setEnabled(true);
            btn_viewchapterMaps.setEnabled(false);
            btn_viewchapterTextP2.setEnabled(true);
            btn_viewchapterTakePicture.setEnabled(false);
        } else if (cursor.getInt(cursor.getColumnIndex("score_audio")) == 1 && cursor.getInt(cursor.getColumnIndex("score_text1")) == 1
                && cursor.getInt(cursor.getColumnIndex("score_maps")) == 1 && cursor.getInt(cursor.getColumnIndex("score_text2")) == 1
                && cursor.getInt(cursor.getColumnIndex("score_sharing")) == 0 ){
            btn_viewchapterAudio.setEnabled(true);
            btn_viewchapterTextP1.setEnabled(true);
            btn_viewchapterMaps.setEnabled(false);
            btn_viewchapterTextP2.setEnabled(true);
            btn_viewchapterTakePicture.setEnabled(true);
        } else if (cursor.getInt(cursor.getColumnIndex("score_audio")) == 1 && cursor.getInt(cursor.getColumnIndex("score_text1")) == 1
                && cursor.getInt(cursor.getColumnIndex("score_maps")) == 1 && cursor.getInt(cursor.getColumnIndex("score_text2")) == 1
                && cursor.getInt(cursor.getColumnIndex("score_sharing")) == 1 ){
            btn_viewchapterAudio.setEnabled(false);
            btn_viewchapterTextP1.setEnabled(true);
            btn_viewchapterMaps.setEnabled(false);
            btn_viewchapterTextP2.setEnabled(true);
            btn_viewchapterTakePicture.setEnabled(false);
        }
    }

    private void secretThing (){
        if (((ViewChapter)getActivity()).value == 2){
            im_viewchapter_secretHint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            View view = getActivity().getLayoutInflater().inflate(R.layout.instructions, null);

                            instructions_text = (TextView)view.findViewById(R.id.instructions_info);

                            instructions_text.setText("Blue Bird\n"+"M0172oF14gg");

                            alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setView(view);
                            alertDialogBuilder.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                            alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });
                }
            });
        }
    }

}
