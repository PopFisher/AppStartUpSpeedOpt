package startup.app.speedopt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import startup.app.speedopt.log.TimeNoteData;

/**
 * Created by popfisher on 2017/1/4.
 */

public class TimeLogAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<TimeNoteData> mTimeNoteDatas;
    private int mMarginLeft = 0;

    public TimeLogAdapter(Context context, ArrayList<TimeNoteData> timeNoteDatas) {
        mContext = context;
        mTimeNoteDatas = timeNoteDatas;
        mMarginLeft = (int) (context.getResources().getDisplayMetrics().widthPixels * (2.0 / 3.0));
    }

    @Override
    public int getCount() {
        return mTimeNoteDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.time_log_list_item, null);
            viewHolder.line = convertView.findViewById(R.id.line);
            viewHolder.noteName = (TextView) convertView.findViewById(R.id.note_text);
            viewHolder.noteDiffTime = (TextView) convertView.findViewById(R.id.note_diff_time);
            viewHolder.noteTotalTime = (TextView) convertView.findViewById(R.id.note_total_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            viewHolder.noteDiffTime.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.noteDiffTime.setVisibility(View.VISIBLE);
        }
        TimeNoteData timeNoteData = mTimeNoteDatas.get(position);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.line.getLayoutParams();
        params.leftMargin = mMarginLeft;
        viewHolder.noteName.setText(timeNoteData.tag);
        viewHolder.noteTotalTime.setText(timeNoteData.totalTime + " ms");
        viewHolder.noteTotalTime.setTextColor(0xaaff0000);
        if (timeNoteData.isSystemCost) {
            viewHolder.noteDiffTime.setText("(" + timeNoteData.timeDiff + " ms) 系统耗时");
        } else {
            viewHolder.noteDiffTime.setText("(" + timeNoteData.timeDiff + " ms)");
        }
        return convertView;
    }

    class ViewHolder {
        View line;
        TextView noteTotalTime;
        TextView noteDiffTime;
        TextView noteName;
    }
}
