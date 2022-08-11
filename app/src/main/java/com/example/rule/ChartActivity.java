package com.example.rule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    AnyChartView anyChart;
    String BUB, KP, SP, event;
    double dBUB, dKP, dSP, dEvent;
    String total;
    Button btnEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Chart");
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24);

        anyChart=findViewById(R.id.any_chart);

        BUB=getIntent().getStringExtra("BUB");
        KP=getIntent().getStringExtra("KP");
        SP=getIntent().getStringExtra("SP");
        event=getIntent().getStringExtra("EVENT");
        total=getIntent().getStringExtra("TOTAL");

        btnEmail=findViewById(R.id.btn_Email);

        dBUB=Double.parseDouble(BUB);
        dKP=Double.parseDouble(KP);
        dSP=Double.parseDouble(SP);
        dEvent=Double.parseDouble(event);


        ArrayList<DataEntry> data= new ArrayList<>();
        data.add(new ValueDataEntry("BUB",dBUB));
        data.add(new ValueDataEntry("KP",dKP));
        data.add(new ValueDataEntry("SP",dSP));
        data.add(new ValueDataEntry("EK",dEvent));

        Pie pie = AnyChart.pie();
        anyChart.setChart(pie);
        pie.data(data);
        anyChart.invalidate();

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "Schools@offcialEmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "[RULE]001: Yearly Point Submition");
                email.putExtra(Intent.EXTRA_TEXT, "Point is submitted as follow: \n Point for Badan/Unit Beruniform: "+BUB
                        +"\nPoint for Kelab dam Persatuan: "+KP
                        +"\nPoint for Sukan dan Permainan: "+SP
                        +"\nExtra Kokurikulum: "+event
                        +"\nTotal point calculated: "+total);

//need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}