package feup.traverse;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapterTextFragment extends Fragment {

    private TextView tv_viewchapter_textContent, tv_viewchapter_ch_title,tv_viewchapter_ch_number;
    int[] values;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_viewchapter_text, container, false);

        tv_viewchapter_textContent = (TextView)rootView.findViewById(R.id.tv_chapter_content);
        tv_viewchapter_ch_number = (TextView)rootView.findViewById(R.id.tv_viewchapter_text_ch_number);
        tv_viewchapter_ch_title = (TextView)rootView.findViewById(R.id.tv_viewchapter_text_ch_title);

        int[] checker_phase = ((ViewChapter)getActivity()).dataBaseAdapter.getScoreFlags(((ViewChapter)getActivity()).session.getusername(),((ViewChapter)getActivity()).value);

        if (((ViewChapter)getActivity()).part == 1 && checker_phase[0] == 1 && checker_phase[1] == 0 && checker_phase[2] == 0 && checker_phase[3] == 0 && checker_phase[4] == 0) {
             values = new int[]{0, 1, 0, 0, 0};
            ((ViewChapter)getActivity()).dataBaseAdapter.updateChallengesByChapter(((ViewChapter)getActivity()).session.getusername(),((ViewChapter)getActivity()).value,values);
            ((ViewChapter)getActivity()).dataBaseAdapter.updateScore(10, ((ViewChapter) getActivity()).session.getusername(), ((ViewChapter) getActivity()).value);
            Toast.makeText(getActivity(), "Congratulations, you have earned 10 points.", Toast.LENGTH_LONG).show();
        } else if (((ViewChapter)getActivity()).part == 2 && checker_phase[0] == 1 && checker_phase[1] == 1 && checker_phase[2] == 1 && checker_phase[3] == 0 && checker_phase[4] == 0){
            values = new int[]{0, 0, 0, 1, 0};
            ((ViewChapter)getActivity()).dataBaseAdapter.updateChallengesByChapter(((ViewChapter)getActivity()).session.getusername(),((ViewChapter)getActivity()).value,values);
            ((ViewChapter)getActivity()).dataBaseAdapter.updateScore(10, ((ViewChapter) getActivity()).session.getusername(), ((ViewChapter) getActivity()).value);
            Toast.makeText(getActivity(), "Congratulations, you have earned 10 points.", Toast.LENGTH_LONG).show();
        } else ;

        Cursor cursor2 = ((ViewChapter)getActivity()).dataBaseAdapter.getInfoByChapter(((ViewChapter)getActivity()).session.getusername(), ((ViewChapter) getActivity()).value);
        tv_viewchapter_ch_title.setText(cursor2.getString(cursor2.getColumnIndex("chapter_name")));
        if (((ViewChapter)getActivity()).part == 1)
            tv_viewchapter_ch_number.setText(String.valueOf(((ViewChapter) getActivity()).value)+" (Part I)");
        else
            tv_viewchapter_ch_number.setText(String.valueOf(((ViewChapter) getActivity()).value) + " (Part II)");

        loadTextToTextView(((ViewChapter) getActivity()).value, ((ViewChapter) getActivity()).part);

        return rootView;
    }

    private void loadTextToTextView (int chapter, int part){

        try {
            InputStream is = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };

            switch (chapter){
                case 1:
                    if (part == 1)
                        is = getActivity().getAssets().open("chapters/chapter1_p1.txt");
                    else
                        is = getActivity().getAssets().open("chapters/chapter1_p2.txt");
                    break;
                case 2:
                    if (part == 1)
                        is = getActivity().getAssets().open("chapters/chapter2_p1.txt");
                    else
                        is = getActivity().getAssets().open("chapters/chapter2_p2.txt");
                    break;
                case 3:
                    if (part == 1)
                        is = getActivity().getAssets().open("chapters/chapter3_p1.txt");
                    else
                        is = getActivity().getAssets().open("chapters/chapter3_p2.txt");
                    break;
                case 4:
                    if (part == 1)
                        is = getActivity().getAssets().open("chapters/chapter4_p1.txt");
                    else
                        is = getActivity().getAssets().open("chapters/chapter4_p2.txt");
                    break;
                case 5:
                    if (part == 1)
                        is = getActivity().getAssets().open("chapters/chapter5_p1.txt");
                    else
                        is = getActivity().getAssets().open("chapters/chapter5_p2.txt");
                    break;
                case 6:
                    if (part == 1)
                        is = getActivity().getAssets().open("chapters/chapter6_p1.txt");
                    else
                        is = getActivity().getAssets().open("chapters/chapter6_p2.txt");
                    break;
                case 7:
                    if (part == 1)
                        is = getActivity().getAssets().open("chapters/chapter7_p1.txt");
                    else
                        is = getActivity().getAssets().open("chapters/chapter7_p2.txt");
                    break;
                case 8:
                    if (part == 1)
                        is = getActivity().getAssets().open("chapters/chapter8_p1.txt");
                    else
                        is = getActivity().getAssets().open("chapters/chapter8_p2.txt");
                    break;
            }
            int size = is.available();

            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String text = new String(buffer);

            tv_viewchapter_textContent.setText(text);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}