package feup.traverse;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @author hugof
 * @date 28/12/2015.
 */
public class PhaseCustomAdapter extends BaseAdapter {

    Context context;
    List<PhaseDoneItem> rowItem;

    PhaseCustomAdapter(Context context, List<PhaseDoneItem> rowItem) {
        this.context = context;
        this.rowItem = rowItem;

    }

    @Override
    public int getCount() {
        return rowItem.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItem.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_phase, null);
        }

        TextView tv_phaseId = (TextView) convertView.findViewById(R.id.tv_progress_phase_id);
        TextView tv_localName = (TextView) convertView.findViewById(R.id.tv_progress_local_name);
        TextView tv_scorePhase = (TextView) convertView.findViewById(R.id.tv_progress_score_phase);

        PhaseDoneItem row_pos = rowItem.get(position);

        // setting the status and warning name
        tv_phaseId.setText(Integer.toString(row_pos.getChapterNumber()));
        tv_localName.setText(row_pos.getLocalName());
        tv_scorePhase.setText(Integer.toString(row_pos.getScore()));

        return convertView;

    }

}