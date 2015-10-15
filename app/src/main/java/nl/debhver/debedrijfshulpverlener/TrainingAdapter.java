package nl.debhver.debedrijfshulpverlener;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import nl.debhver.debedrijfshulpverlener.enums.TrainingType;
import nl.debhver.debedrijfshulpverlener.models.Training;

/**
 * Created by Dukahone on 14-10-2015.
 */
public class TrainingAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<Training> trainingList;

    public TrainingAdapter(Activity activity, List<Training> trainingList)
    {
        this.activity = activity;
        this.trainingList = trainingList;
    }

    @Override
    public int getCount() {
        return trainingList.size();
    }

    @Override
    public Object getItem(int position) {
        return  trainingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(layoutInflater == null)
            layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = layoutInflater.inflate(R.layout.training_row, null);

        TextView name = (TextView) convertView.findViewById(R.id.trainingName);
        TextView description = (TextView)convertView.findViewById(R.id.trainingDescription);
        TextView type = (TextView)convertView.findViewById(R.id.trainingType);
        ImageView image = (ImageView)convertView.findViewById(R.id.trainingImage);

        Training t = trainingList.get(position);
        name.setText(t.getName());
        description.setText(t.getDescription());
        type.setText(t.getType().toString());

        if(t.getType() == TrainingType.REPEAT)
        image.setImageResource(R.drawable.ic_checkmark);
        return convertView;
    }
}
