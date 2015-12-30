package feup.traverse;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapterMenuFragment extends Fragment {

    private Button btn_viewchapterAudio, btn_viewchapterMaps, btn_viewchapterText, btn_viewchapterTakePicture;
    private TextView tv_viewchapterAudioname, tv_viewchapterTextName, tv_viewchapterLocalName;
    private ImageView im_play;
    private SeekBar sb_music_progress;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    private MediaPlayer mediaPlayer = new MediaPlayer();
    int mediaFileLengthInMilliseconds;

    DataBaseAdapter dataBaseAdapter;

    private Session session;

    Handler handler = new Handler();

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

        Bundle extras = getActivity().getIntent().getExtras();
        int value = extras.getInt("chapter_selected");

        Cursor cursor = dataBaseAdapter.getChapterInfo(session.getusername(), value);
        tv_viewchapterLocalName.setText(cursor.getString(cursor.getColumnIndex("local")));

        btn_viewchapterAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final boolean[] play_status = {false};

                if (!mediaPlayer.isPlaying()) {
                    btn_viewchapterAudio.setText("Listening To");
                    isStartPrepared();
                }
                else {
                    btn_viewchapterAudio.setText("Paused/Stopped");
                    mediaPlayer.stop();
                    mediaPlayer.reset();
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

        return rootView;
    }

    /* TODO: Implement sharing on Facebook */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2500 && data.getData() != null) {
            Bitmap image = (Bitmap) data.getExtras().get("data"); // Bitmap that is used to share on FB
        } else
            Toast.makeText(getActivity(),"You didn't get any photo. :(", Toast.LENGTH_SHORT).show();
    }

    private void isStartPrepared() {
        try {
            AssetFileDescriptor afd = getActivity().getAssets().openFd("audio/"+"Victoria_ch1.mp3");
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
