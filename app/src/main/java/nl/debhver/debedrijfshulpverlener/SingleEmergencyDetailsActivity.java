package nl.debhver.debedrijfshulpverlener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.EvacuationPlan;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Koen on 26-10-2015.
 */
public class SingleEmergencyDetailsActivity extends HomeActivity {
    private float lastX;
    private ViewFlipper viewFlipper;
    private ImageView evacuationPlan;
    private PhotoViewAttacher photoViewAttacher;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String emergencyType = getIntent().getStringExtra(EmergencyManualsActivity.EMERGENCY_EXTRA);

        if(emergencyType != null){ // emergency chosen
            populateSingleEmergency(emergencyType);
        }
        else{
            setContentView(R.layout.activity_single_emergency_details);
            System.out.println("NO EXTRA");
        }

        setBackButtonOnToolbar(false);
    }

    private void populateSingleEmergency(String emergency){
        if(emergency.equals(getResources().getString(R.string.heart_attack))){
            loadHeartAttackInstructions();
        }
        else if(emergency.equals(getResources().getString(R.string.suffocation))){
            loadSuffocationInstructions();
        }
        else if(emergency.equals(getResources().getString(R.string.evacuation_plan))){
            loadEvacuationPlan();
        }
    }

    private void loadHeartAttackInstructions(){
        setContentView(R.layout.activity_single_emergency_details);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        final Context context = getApplicationContext();
        CustomManualStepLinearLayout stepOne = new CustomManualStepLinearLayout(context, 1, 8, getResources().getString(R.string.ha_step1_details));
        CustomManualStepLinearLayout stepTwo = new CustomManualStepLinearLayout(context, 2, 8, getResources().getString(R.string.ha_step2_details));
        CustomManualStepLinearLayout stepThree = new CustomManualStepLinearLayout(context, 3, 8, getResources().getString(R.string.ha_step3_details));
        CustomManualStepLinearLayout stepFour = new CustomManualStepLinearLayout(context, 4, 8, getResources().getString(R.string.ha_step4_details));
        CustomManualStepLinearLayout stepFive = new CustomManualStepLinearLayout(context, 5, 8, getResources().getString(R.string.ha_step5_details));
        CustomManualStepLinearLayout stepSix = new CustomManualStepLinearLayout(context, 6, 8, getResources().getString(R.string.ha_step6_details));
        CustomManualStepLinearLayout stepSeven = new CustomManualStepLinearLayout(context, 7, 8, getResources().getString(R.string.ha_step7_details));
        CustomManualStepLinearLayout stepEight = new CustomManualStepLinearLayout(context, 8, 8, getResources().getString(R.string.ha_step8_details));

        viewFlipper.addView(stepOne);
        viewFlipper.addView(stepTwo);
        viewFlipper.addView(stepThree);
        viewFlipper.addView(stepFour);
        viewFlipper.addView(stepFive);
        viewFlipper.addView(stepSix);
        viewFlipper.addView(stepSeven);
        viewFlipper.addView(stepEight);

        viewFlipper.setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeLeft() {
                if (viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1) {
                    return;
                }
                viewFlipper.setInAnimation(context, R.anim.in_from_right);
                viewFlipper.setOutAnimation(context, R.anim.out_to_left);
                viewFlipper.showNext();
                changeProgress();
            }

            @Override
            public void onSwipeRight() {
                if (viewFlipper.getDisplayedChild() == 0) {
                    return;
                }
                viewFlipper.setInAnimation(context, R.anim.in_from_left);
                viewFlipper.setOutAnimation(context, R.anim.out_to_right);
                viewFlipper.showPrevious();
                changeProgress();
            }
        });
        changeProgress();
    }

    private void loadSuffocationInstructions(){
        setContentView(R.layout.activity_single_emergency_details);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        final Context context = getApplicationContext();
        CustomManualStepLinearLayout stepOne = new CustomManualStepLinearLayout(context, 1, 6, getResources().getString(R.string.suffocation_step1_details));
        CustomManualStepLinearLayout stepTwo = new CustomManualStepLinearLayout(context, 2, 6, getResources().getString(R.string.suffocation_step2_details));
        CustomManualStepLinearLayout stepThree = new CustomManualStepLinearLayout(context, 3, 6, getResources().getString(R.string.suffocation_step3_details));
        CustomManualStepLinearLayout stepFour = new CustomManualStepLinearLayout(context, 4, 6, getResources().getString(R.string.suffocation_step4_details));
        CustomManualStepLinearLayout stepFive = new CustomManualStepLinearLayout(context, 5, 6, getResources().getString(R.string.suffocation_step5_details));
        CustomManualStepLinearLayout stepSix = new CustomManualStepLinearLayout(context, 6, 6, getResources().getString(R.string.suffocation_step6_details));

        viewFlipper.addView(stepOne);
        viewFlipper.addView(stepTwo);
        viewFlipper.addView(stepThree);
        viewFlipper.addView(stepFour);
        viewFlipper.addView(stepFive);
        viewFlipper.addView(stepSix);

        viewFlipper.setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeLeft() {
                if (viewFlipper.getDisplayedChild() == viewFlipper.getChildCount() - 1) {
                    return;
                }
                viewFlipper.setInAnimation(context, R.anim.in_from_right);
                viewFlipper.setOutAnimation(context, R.anim.out_to_left);
                viewFlipper.showNext();
                changeProgress();
            }

            @Override
            public void onSwipeRight() {
                if (viewFlipper.getDisplayedChild() == 0) {
                    return;
                }
                viewFlipper.setInAnimation(context, R.anim.in_from_left);
                viewFlipper.setOutAnimation(context, R.anim.out_to_right);
                viewFlipper.showPrevious();
                changeProgress();
            }
        });
        changeProgress();
    }

    private void loadEvacuationPlan(){
        setContentView(R.layout.activity_evacuation_plan);
        evacuationPlan = (ImageView)findViewById(R.id.evacuationPlanImageView);
        photoViewAttacher = new PhotoViewAttacher(evacuationPlan);
        spinner = (Spinner)findViewById(R.id.floorSpinner);
        DBManager.getInstance().getEvacuationPlansFromCurrentBranch(this);
    }

    private void changeProgress(){ // neg steps is backwards
        ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        float progress;
        float current = viewFlipper.getDisplayedChild()+1;
        float total = viewFlipper.getChildCount();
        progress = (current/total)*100;
        Log.d("Progress", " " + progress);
        progressBar.setProgress(Math.round(progress));
    }

    public void createDropdownWithFloors(List<EvacuationPlan> evacuationPlans){

        ArrayAdapter<EvacuationPlan> adapter = new ArrayAdapter<EvacuationPlan>(SingleEmergencyDetailsActivity.this, android.R.layout.simple_spinner_dropdown_item, evacuationPlans);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.d("spinner", "selected" + position);
                DBManager.getInstance().getEvacuationPlan(SingleEmergencyDetailsActivity.this, (EvacuationPlan) spinner.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.d("spinner", "nothing selected?");
            }

        });


    }

    public void loadEvacuationPlan(byte[] data){
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        evacuationPlan.setImageBitmap(bmp);
        photoViewAttacher.update();
    }
}

