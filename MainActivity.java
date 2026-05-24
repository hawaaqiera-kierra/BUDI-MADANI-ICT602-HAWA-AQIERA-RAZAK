package com.example.budihawa;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Spinner petrolType;
    EditText editPrice;
    SeekBar seekFuel;
    Switch switch_status;
    Button btn_calculate;
    TextView totCost, rebate, totSaving;
    TextView txt_totCost, txt_rebate, txt_totSaving, txt_UserUsage;
    double liters = 0;
    final double SUBSIDY_RATE = 1.99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorOnPrimary));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        petrolType = findViewById(R.id.petrolType);
        editPrice = findViewById(R.id.editPrice);
        seekFuel = findViewById(R.id.seekFuel);
        seekFuel.setMax(100);
        switch_status = findViewById(R.id.switch_status);
        btn_calculate = findViewById(R.id.btn_calculate);
        totCost = findViewById(R.id.totCost);
        rebate = findViewById(R.id.rebate);
        totSaving = findViewById(R.id.totSaving);
        txt_totCost = findViewById(R.id.txt_totCost);
        txt_rebate = findViewById(R.id.txt_rebate);
        txt_totSaving = findViewById(R.id.txt_totSaving);
        txt_UserUsage = findViewById(R.id.txt_UserUsage);


        String[] fuelTypes = {"RON95", "RON97", "Diesel"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                fuelTypes
        );

        petrolType.setAdapter(adapter);

        switch_status.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                switch_status.setText("Yes");
            } else {
                switch_status.setText("No");
            }
        });

        seekFuel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                liters = progress;
                txt_UserUsage.setText(progress + "L");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btn_calculate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calculateFuel();
            }
        });
    }

    private void calculateFuel() {
        String type = petrolType.getSelectedItem().toString();
        String priceStr = editPrice.getText().toString();
        if (priceStr.isEmpty()) {
            Toast.makeText(this,
                    "Please enter petrol price",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (liters == 0) {
            Toast.makeText(this,
                    "Please select fuel usage",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        double price = Double.parseDouble(priceStr);

        double totalCost = liters * price;

        double rebateValue = 0;

        if (type.equals("RON95") && switch_status.isChecked()) {
            rebateValue = liters * SUBSIDY_RATE;
        } else if (!type.equals("RON95") && switch_status.isChecked()) {

            Toast.makeText(this,
                    "Only RON95 is eligible for subsidy",
                    Toast.LENGTH_SHORT).show();
        }

        double saving = totalCost - rebateValue;

        txt_totCost.setVisibility(View.VISIBLE);
        txt_rebate.setVisibility(View.VISIBLE);
        txt_totSaving.setVisibility(View.VISIBLE);
        totCost.setVisibility(View.VISIBLE);
        rebate.setVisibility(View.VISIBLE);
        totSaving.setVisibility(View.VISIBLE);
        totCost.setText("RM " + String.format("%.2f", totalCost));
        rebate.setText("RM " + String.format("%.2f", rebateValue));
        totSaving.setText("RM " + String.format("%.2f", saving));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home) {
            Toast.makeText(this, "You are already on Home Page", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutPage.class);
                    startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}