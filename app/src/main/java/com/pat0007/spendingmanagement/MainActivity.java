package com.pat0007.spendingmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    BigDecimal balance;
    Button add_btn, minus_btn;
    Context applicationContext;
    EditText date, amount, purpose;
    LinearLayout history_list;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String history;
    TextView header;

    private View.OnClickListener listen = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.add_btn:
                    add_btn_clicked();
                    break;
                case R.id.minus_btn:
                    minus_btn_clicked();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        header = findViewById(R.id.header);
        date = findViewById(R.id.date);
        amount = findViewById(R.id.amount);
        purpose = findViewById(R.id.purpose);
        add_btn = findViewById(R.id.add_btn);
        minus_btn = findViewById(R.id.minus_btn);
        history_list = findViewById(R.id.history_list);
        applicationContext = this;
        sharedPref = getSharedPreferences(
                "com.pat0007.spendingmanagement.PREFERENCE_FILE_KEY", MODE_PRIVATE);

        add_btn.setOnClickListener(listen);
        minus_btn.setOnClickListener(listen);

        balance = new BigDecimal(sharedPref.getString("BALANCE", "0"));
        history = sharedPref.getString("HISTORY", "");

        header.setText(sharedPref.getString("HEADER", "Current Balance: $0"));

        final LinearLayout prevHistory = new LinearLayout(applicationContext);
        prevHistory.setOrientation(LinearLayout.VERTICAL);

        TextView newEntry = new TextView(applicationContext);
        newEntry.setText(history);
        prevHistory.addView(newEntry);
        history_list.addView(prevHistory);
    }

    @Override
    protected void onPause() {
        super.onPause();

        sharedPref = getSharedPreferences(
                "com.pat0007.spendingmanagement.PREFERENCE_FILE_KEY", MODE_PRIVATE);
        editor = sharedPref.edit();

        editor.putString("HEADER", header.getText().toString());
        String balance = header.getText().toString().substring(18);
        editor.putString("BALANCE", balance);
        editor.putString("HISTORY", history);
        editor.apply();
    }

    private void add_btn_clicked() {
        final LinearLayout newHistory = new LinearLayout(applicationContext);
        newHistory.setOrientation(LinearLayout.VERTICAL);

        String dateText = date.getText().toString();
        String amountText = amount.getText().toString();
        String purposeText = purpose.getText().toString();

        String output = "Added $" + amountText + " on " + dateText + " from " +
                purposeText;

        TextView newEntry = new TextView(applicationContext);
        newEntry.setText(output);
        newHistory.addView(newEntry);
        history_list.addView(newHistory);

        BigDecimal newBalance = new BigDecimal(amountText);
        history += "\n" + output;

        balance = balance.add(newBalance);
        header.setText("Current Balance: $" + balance);
    }

    private void minus_btn_clicked() {
        final LinearLayout newHistory = new LinearLayout(applicationContext);
        newHistory.setOrientation(LinearLayout.VERTICAL);

        String dateText = date.getText().toString();
        String amountText = amount.getText().toString();
        String purposeText = purpose.getText().toString();

        String output = "Spent $" + amountText + " on " + dateText + " on " +
                purposeText;

        TextView newEntry = new TextView(applicationContext);
        newEntry.setText(output);
        newHistory.addView(newEntry);
        history_list.addView(newHistory);

        BigDecimal newBalance = new BigDecimal(amountText);
        history += "\n" + output;

        balance = balance.subtract(newBalance);
        header.setText("Current Balance: $" + balance);
    }
}