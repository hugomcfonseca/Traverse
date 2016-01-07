package feup.traverse;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author hugof
 * @date 29/12/2015.
 */
public class ViewChapterTextFragment extends Fragment {

    private TextView tv_viewchapter_textContent, tv_viewchapter_ch_title,tv_viewchapter_ch_number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_viewchapter_text, container, false);

        tv_viewchapter_textContent = (TextView)rootView.findViewById(R.id.tv_chapter_content);
        tv_viewchapter_ch_number = (TextView)rootView.findViewById(R.id.tv_viewchapter_text_ch_number);
        tv_viewchapter_ch_title = (TextView)rootView.findViewById(R.id.tv_viewchapter_text_ch_title);

        loadTextToTextView(1,((ViewChapter) getActivity()).part);

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