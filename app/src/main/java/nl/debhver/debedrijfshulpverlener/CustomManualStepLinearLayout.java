package nl.debhver.debedrijfshulpverlener;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Koen on 26-10-2015.
 */
public class CustomManualStepLinearLayout extends RelativeLayout{
    private TextView progressTV;
    private TextView detailsTV;

    public CustomManualStepLinearLayout(Context context, int stepX, int outOfXSteps, String details) {
        super(context);
        // TODO Auto-generated constructor stub

        this.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        this.setGravity(Gravity.CENTER_HORIZONTAL);
        //android:layout_marginTop="130dp"
        progressTV = new TextView(context);
        detailsTV = new TextView(context);
        progressTV.setTextAppearance(context, android.R.style.TextAppearance_Large);
        detailsTV.setTextAppearance(context, android.R.style.TextAppearance_Large);
        progressTV.setTextColor(Color.BLACK);
        detailsTV.setTextColor(Color.BLACK);

        LayoutParams params1 = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        LayoutParams params2 = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );


        float d = context.getResources().getDisplayMetrics().density;
        int margin = (int)(80 * d); // margin in pixels

        this.addView(progressTV);
        params1.topMargin = margin;
        params1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        progressTV.setGravity(Gravity.CENTER);
        progressTV.setLayoutParams(params1);
        progressTV.setText(getResources().getString(R.string.step) + " " + stepX + " / " + outOfXSteps);


        margin = (int)(130 * d); // margin in pixels
        this.addView(detailsTV);
        params2.topMargin = margin;
        params2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        detailsTV.setGravity(Gravity.CENTER);
        detailsTV.setLayoutParams(params2);
        detailsTV.setText(details);


    }
}