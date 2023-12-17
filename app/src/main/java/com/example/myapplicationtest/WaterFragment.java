package com.example.myapplicationtest;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WaterFragment extends Fragment {

    private static final String TAG = "WaterFragment";

    private LineChart lineChart;

    public WaterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize chart
        lineChart = new LineChart(requireContext());
        lineChart.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // Add the chart to the fragment's layout
        View view = getView();
        if (view != null) {
            ((ViewGroup) view).addView(lineChart);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_water, container, false);

        // Initialize chart
        lineChart = new LineChart(requireContext());
        lineChart.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // Add the chart to the fragment's layout
        ((ViewGroup) view.findViewById(R.id.chartContainer)).addView(lineChart);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Reference to the "plantdata" node in Firebase
        DatabaseReference plantDataRef = FirebaseDatabase.getInstance().getReference("plantdata");

        // Retrieve data from Firebase and update chart
        plantDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Entry> entries = new ArrayList<>();
                List<String> xAxisLabels = new ArrayList<>();

                // Iterate through the dataSnapshot and filter entries based on the key
                int index = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();

                    // Check if the key contains the word "sensor"
                    if (key != null && key.toLowerCase().contains("sensor")) {
                        float sensorReading = snapshot.getValue(Float.class);
                        entries.add(new Entry(index, sensorReading));
                        //xAxisLabels.add((index * 10) + "min");
                        index++;
                    }
                }
                index--;

                //new
                while (index >= 0) {
                    int minutes = index * 10;
                    if (minutes >= 60) {
                        int hours = minutes / 60;
                        int remainingMinutes = minutes % 60;
                        if (remainingMinutes == 0) {
                            xAxisLabels.add(hours + "hr");
                        } else {
                            xAxisLabels.add(hours + "hr " + remainingMinutes + "min");
                        }
                    } else {
                        xAxisLabels.add(minutes + "min");
                    }
                    index--;
                }


                // Update the chart with the retrieved data
                updateChartWithData(entries, xAxisLabels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void updateChartWithData(List<Entry> entries, List<String> xAxisLabels) {
        // Create a LineDataSet from your data
        LineDataSet dataSet = new LineDataSet(entries, "Moisture Level");
        dataSet.setColors(getResources().getColor(R.color.chart_color)); // Change color as needed
        dataSet.setValueTextSize(14f); // Set the text size for data values

        // Create a LineData object with the LineDataSet
        LineData lineData = new LineData(dataSet);

        // Set the LineData to the chart
        lineChart.setData(lineData);

        // Customize chart appearance
        lineChart.getDescription().setTextSize(0f); // Set the text size for the description
        lineChart.getDescription().setText("");
        lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setGranularity(1f);

        // Set the text size for the X-axis labels
        lineChart.getXAxis().setTextSize(12f);

        // Set the text size for the Y-axis labels
        lineChart.getAxisLeft().setTextSize(12f);

        // Set the text size for the chart legend
        lineChart.getLegend().setTextSize(14f);

        // Set the background color
        lineChart.setBackgroundColor(Color.parseColor("#b8e5ff")); // Change color as needed

        // Animate the chart
        lineChart.animateY(1000);

        // Refresh the chart
        lineChart.invalidate();
    }

}
