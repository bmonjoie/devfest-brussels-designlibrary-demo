package be.xzan.demo.designlibrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import be.xzan.demo.designlibrary.R;
import be.xzan.demo.designlibrary.data.Speaker;

/**
 * Created on 6/10/15 for DesignLibrary
 *
 * @author bmo
 * @version 1
 */
public class SpeakerListAdapter extends ArrayAdapter<Speaker> {

    public SpeakerListAdapter(Context context, Speaker[] objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_speaker, parent, false);

        }
        Glide.with(getContext()).load(getItem(position).getPhotoUrl()).into((ImageView) convertView.findViewById(R.id.ivPicture));
        ((TextView) convertView.findViewById(R.id.tvName)).setText(getItem(position).name);
        return convertView;
    }
}
