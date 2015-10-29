package nl.debhver.debedrijfshulpverlener;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import nl.debhver.debedrijfshulpverlener.models.Training;

public class AdminTrainingActivity extends HomeActivity{


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_training);
            FloatingActionButton trainingCreate = (FloatingActionButton) findViewById(R.id.trainingAdd);
            FloatingActionButton trainingUser = (FloatingActionButton) findViewById(R.id.trainingUser);

            trainingUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(AdminTrainingActivity.this, AdminUserTraining.class);
                    startActivity(i);
                }
            });

            trainingCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(AdminTrainingActivity.this, AdminTrainingAddActivity.class);
                    startActivity(i);
                }
            });
        }

        @Override
        protected void onResume() {
            super.onResume();
            retreiveTraining();
        }

        private void retreiveTraining() {
            DBManager.getInstance().getAllTraining(this);
        }

        public void populateTrainingList(final List<Training> trainings) {
            final ListView trainingListView = (ListView)findViewById(R.id.traininglistView);
            final TrainingAdapter trainingAdapter = new TrainingAdapter(AdminTrainingActivity.this, trainings);
            trainingListView.setAdapter(trainingAdapter);
            trainingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String s = trainings.get(position).getName();
                    Intent i = new Intent(AdminTrainingActivity.this, AdminTrainingAddActivity.class);
                    i.putExtra("Training", trainings.get(position).getObjectId());
                    startActivity(i);
                }
            });
        }}
