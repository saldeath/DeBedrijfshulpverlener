package nl.debhver.debedrijfshulpverlener;

import android.content.Context;
import android.os.Bundle;
import android.widget.ViewFlipper;

/**
 * Created by Koen on 26-10-2015.
 */
public class SingleEmergencyDetailsActivity extends HomeActivity {
    private float lastX;
    private ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_emergency_details);
        //setBackButtonOnToolbar(true);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        String emergencyType = getIntent().getStringExtra(EmergencyManualsActivity.EMERGENCY_EXTRA);

        if(emergencyType != null){ // emergency chosen

            populateSingleEmergency(emergencyType);
        }
        else{
            System.out.println("NO EXTRA");
        }


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
            }

            @Override
            public void onSwipeRight() {
                if (viewFlipper.getDisplayedChild() == 0) {
                    return;
                }
                viewFlipper.setInAnimation(context, R.anim.in_from_left);
                viewFlipper.setOutAnimation(context, R.anim.out_to_right);
                viewFlipper.showPrevious();
            }
        });
    }

    private void loadSuffocationInstructions(){
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
            }

            @Override
            public void onSwipeRight() {
                if (viewFlipper.getDisplayedChild() == 0) {
                    return;
                }
                viewFlipper.setInAnimation(context, R.anim.in_from_left);
                viewFlipper.setOutAnimation(context, R.anim.out_to_right);
                viewFlipper.showPrevious();
            }
        });
    }

    private void loadEvacuationPlan(){

    }

}
