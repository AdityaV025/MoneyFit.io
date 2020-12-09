package com.example.moneyfitio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Application;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private LineChart mChart;
    private RequestQueue requestQueue;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setData();

    }

    private void init() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        requestQueue = Volley.newRequestQueue(this);
        mChart = findViewById(R.id.chart);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisRight().setDrawGridLines(false);
    }

    private void setData() {
        final ArrayList<Entry> values = new ArrayList<>();
        String url = "https://jsonkeeper.com/b/EEDM";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int i = 0 ; i <= jsonArray.length() ; i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                String invest_value = object.getString("investment_invest_value");
                                String current_invest_value = object.getString("investment_current_value");
                                values.add(new Entry(Float.parseFloat(invest_value),Float.parseFloat(current_invest_value)));
                                LineDataSet lineDataSet = new LineDataSet(values, "Investment Invest Value");
                                ArrayList<ILineDataSet> dataSet  = new ArrayList<>();
                                dataSet.add(lineDataSet);
                                LineData data = new LineData(dataSet);
                                int backgroundColor = getResources().getColor(R.color.testColor);
                                int lineColor = getResources().getColor(R.color.lineColor);

                                XAxis xAxis = mChart.getXAxis();
                                xAxis.setTextColor(Color.WHITE);
                                //xAxis.setValueFormatter(new DateFormatter(chart));

                                xAxis.setAxisLineColor(backgroundColor);
                                xAxis.setAxisLineWidth(1.5f);
                                //xAxis.enableGridDashedLine(20, 10, 0);

                                YAxis yAxisLeft = mChart.getAxisLeft();
                                yAxisLeft.setAxisLineColor(backgroundColor);
                                yAxisLeft.setTextColor(Color.WHITE);
                                yAxisLeft.setAxisLineWidth(1.5f);
                                //yAxisLeft.enableGridDashedLine(20,40,0);

                                YAxis yAxisRight = mChart.getAxisRight();
                                yAxisRight.setTextColor(Color.WHITE);
                                yAxisRight.setAxisLineColor(backgroundColor);
                                yAxisRight.setAxisLineWidth(2);
                                yAxisRight.enableGridDashedLine(20, 40, 0);

//                                Legend Setup
                                Legend legend = mChart.getLegend();
                                legend.setEnabled(true);
                                legend.setTextColor(Color.WHITE);
                                legend.setTextSize(14f);

//                                LineDataSet Values
                                lineDataSet.setDrawCircles(true);
                                lineDataSet.setDrawFilled(true);
                                lineDataSet.setDrawValues(true);
                                lineDataSet.setValueTextColor(Color.WHITE);
                                lineDataSet.setValueTextSize(11f);
                                lineDataSet.setFillColor(backgroundColor);
                                lineDataSet.setColors(lineColor);
                                lineDataSet.setLineWidth(3f);
                                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                                lineDataSet.setCubicIntensity(0.1f);
                                lineDataSet.setFillAlpha(80);
                                Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.chart_drawable);
                                lineDataSet.setDrawFilled(true);
                                lineDataSet.setFillDrawable(drawable);

//                                Setting up Chart
                                mChart.setDrawGridBackground(false);
                                mChart.setBackgroundColor(backgroundColor);
                                mChart.setDragEnabled(true);
                                mChart.setScaleEnabled(true);
                                mChart.setPinchZoom(false);
                                mChart.getDescription().setEnabled(false);
                                mChart.setData(data);
                                mChart.notifyDataSetChanged();
                                mChart.invalidate();
                                mProgressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);

    }

}