package feup.traverse;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class ViewProgressList extends ListFragment implements AdapterView.OnItemClickListener {

    List<PhaseDoneItem> phaseDoneItem;
    DataBaseAdapter dataBaseAdapter;
    private Session session;

    PhaseCustomAdapter adapter;

    private TextView tv_progressActualChapter, tv_progressValue;

    private Typeface regularF;
    private Typeface boldF;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phase_list, container, false);

        regularF = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/qsR.otf");
        boldF = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/qsB.otf");

        dataBaseAdapter = new DataBaseAdapter(getActivity());
        dataBaseAdapter.open();

        session = new Session(getActivity()); //in oncreate

        tv_progressActualChapter = (TextView) (getActivity()).findViewById(R.id.tv_progress_actual_chapter);
        tv_progressActualChapter.setTypeface(boldF);
        tv_progressValue = (TextView) (getActivity()).findViewById(R.id.tv_progress_value);
        tv_progressValue.setTypeface(regularF);


        Cursor cursor_localname = dataBaseAdapter.getLastLocalName(session.getusername());

        String displayedText = cursor_localname.getString(cursor_localname.getColumnIndex("phase"))
                +". "+cursor_localname.getString(cursor_localname.getColumnIndex("local"));

        tv_progressActualChapter.setText(displayedText);

        Cursor cursor_progress = dataBaseAdapter.getProfileData(session.getusername());
        tv_progressValue.setText(Integer.toString(cursor_progress.getInt(cursor_progress.getColumnIndex("progress")))+"/100");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        phaseDoneItem = new ArrayList<PhaseDoneItem>();
        phaseDoneItem = dataBaseAdapter.getAllPhasesDone(session.getusername());
        adapter = new PhaseCustomAdapter(getActivity(), phaseDoneItem);
        //View header = getLayoutInflater().inflate(R.layout.header, null);
        //getListView().addHeaderView(header);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), dataBaseAdapter.nChapter[position] + " Clicked!"
                , Toast.LENGTH_SHORT).show();

        //ToDo: Implement a AlertDialog to show row details when user click in one item.
    }
}
