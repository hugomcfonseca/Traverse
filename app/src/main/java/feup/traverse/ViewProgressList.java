package feup.traverse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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


public class   ViewProgressList extends ListFragment implements AdapterView.OnItemClickListener {

    List<PhaseDoneItem> phaseDoneItem;
    DataBaseAdapter dataBaseAdapter;
    private Session session;

    PhaseCustomAdapter adapter;

    private TextView tv_progressActualChapter, tv_progressValue;

    private Typeface regularF;
    private Typeface boldF;

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

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
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                View view = getActivity().getLayoutInflater().inflate(R.layout.view_progress_item_info, null);

                alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setPositiveButton("Done!",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                alertDialog = alertDialogBuilder.create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }
        });
    }
}
