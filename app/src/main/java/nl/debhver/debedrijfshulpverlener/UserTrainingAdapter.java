package nl.debhver.debedrijfshulpverlener;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import nl.debhver.debedrijfshulpverlener.enums.TrainingType;
import nl.debhver.debedrijfshulpverlener.models.Training;
import nl.debhver.debedrijfshulpverlener.models.UserTraining;

/**
 * Created by Dukahone on 14-10-2015.
 */
public class UserTrainingAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<UserTraining> trainingList;

    public UserTrainingAdapter(Activity activity, List<UserTraining> trainingList)
    {
        this.activity = activity;
        this.trainingList = trainingList;
    }

    private static DateFormat getDateFormat() {
        return new SimpleDateFormat("EEE,MMM dd,yyyy", Locale.getDefault());
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
            convertView = layoutInflater.inflate(R.layout.user_training_row, null);
        TextView userName = (TextView) convertView.findViewById(R.id.userTrainingUserNameTextView);
        TextView trainingName = (TextView)convertView.findViewById(R.id.userTrainingTrainingNameTextView);
        TextView expDate = (TextView)convertView.findViewById(R.id.userTrainingExpirationDateTextView);
        TextView schelDate = (TextView)convertView.findViewById(R.id.userTrainingScheclduleDateTextView);
        UserTraining ut = trainingList.get(position);
        userName.setText(ut.getUser().getName());
        trainingName.setText(ut.getTraining().getName());
        expDate.setText(getDateFormat().format(ut.getExpirationDate()));
        schelDate.setText(getDateFormat().format(ut.getScheduleDate()));
        return convertView;
    }
}
