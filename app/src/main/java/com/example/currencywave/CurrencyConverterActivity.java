package com.example.currencywave;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CurrencyConverterActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private AutoCompleteTextView fromCurrencyAutoComplete;
    private AutoCompleteTextView toCurrencyAutoComplete;
    private EditText amountEditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        // We initialize FireBase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        // We initialize the UI elements
        fromCurrencyAutoComplete = findViewById(R.id.from_currency_autocomplete);
        toCurrencyAutoComplete = findViewById(R.id.to_currency_autocomplete);
        amountEditText = findViewById(R.id.amount_edit_text);
        resultTextView = findViewById(R.id.result_text_view);
        Button convertButton = findViewById(R.id.convert_button);

        // Set up the AutoCompleteTextViews
        setupAutoCompleteTextViews();

        convertButton.setOnClickListener(view -> {
            String fromCurrency = fromCurrencyAutoComplete.getText().toString();
            String toCurrency = toCurrencyAutoComplete.getText().toString();
            String amountStr = amountEditText.getText().toString();
            if (!amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                convertCurrency(fromCurrency, toCurrency, amount);
            } else {
                Toast.makeText(CurrencyConverterActivity.this, getString(R.string.enter_amount), Toast.LENGTH_SHORT).show();            }
        });
    }

    private void convertCurrency(String fromCurrency, String toCurrency, double amount) {
        Query query = db.collection("exchangeRates").orderBy("timestamp", Query.Direction.DESCENDING).limit(1);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot result = task.getResult();
                if (result != null && !result.isEmpty()) {
                    DocumentSnapshot document = result.getDocuments().get(0);
                    Object ratesObject = document.get("rates");
                    if (ratesObject instanceof Map) {
                        Map<String, Object> ratesData = (Map<String, Object>) ratesObject;
                        String fromCurrencyUpper = fromCurrency.toUpperCase();
                        String toCurrencyUpper = toCurrency.toUpperCase();
                        if (ratesData.containsKey(toCurrencyUpper) && ratesData.containsKey(fromCurrencyUpper)) {
                            Object toCurrencyRateObj = ratesData.get(toCurrencyUpper);
                            Object fromCurrencyRateObj = ratesData.get(fromCurrencyUpper);
                            if (toCurrencyRateObj instanceof Number && fromCurrencyRateObj instanceof Number) {
                                double toCurrencyRate = ((Number) toCurrencyRateObj).doubleValue();
                                double fromCurrencyRate = ((Number) fromCurrencyRateObj).doubleValue();
                                double convertedAmount = (amount * toCurrencyRate) / fromCurrencyRate;
                                resultTextView.setText(String.format(Locale.getDefault(), "%.2f", convertedAmount));
                            } else {
                                Toast.makeText(CurrencyConverterActivity.this, getString(R.string.exchange_rate_data_error), Toast.LENGTH_SHORT).show();                            }
                        } else {
                            Toast.makeText(CurrencyConverterActivity.this, getString(R.string.currency_not_available), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CurrencyConverterActivity.this, getString(R.string.exchange_rate_data_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CurrencyConverterActivity.this, getString(R.string.api_response_error), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CurrencyConverterActivity.this, getString(R.string.api_call_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAutoCompleteTextViews() {
        // List of currencies we want to display in the AutoCompleteTextViews
        List<String> currencyList = Arrays.asList("USD", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD",
                "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF",
                "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL",
                "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD",
                "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR",
                "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR",
                "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SLL", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD",
                "TVD", "TWD", "TZS", "UAH", "UGX", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL");


        // Created an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, currencyList);

        // Adapter applied to the AutoCompleteTextViews
        fromCurrencyAutoComplete.setAdapter(adapter);
        toCurrencyAutoComplete.setAdapter(adapter);

        // Set the threshold at 1 for showing suggestions
        fromCurrencyAutoComplete.setThreshold(1);
        toCurrencyAutoComplete.setThreshold(1);

        // Added click listeners to show the dropdown when the user clicks on the AutoCompleteTextView using lambda expressions
        fromCurrencyAutoComplete.setOnClickListener(v -> fromCurrencyAutoComplete.showDropDown());
        toCurrencyAutoComplete.setOnClickListener(v -> toCurrencyAutoComplete.showDropDown());
    }
}