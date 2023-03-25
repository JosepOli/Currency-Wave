package com.example.currencywave;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.currencywave.api.CurrencyApi;
import com.example.currencywave.model.CurrencyApiResponse;
import com.example.currencywave.network.RetrofitClient;

public class CurrencyConverterActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Spinner fromCurrencySpinner;
    private Spinner toCurrencySpinner;
    private EditText amountEditText;
    private TextView resultTextView;
    private Button convertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        // Inicializamos Firebase
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        // Inicializa los elementos de la interfaz de usuario
        fromCurrencySpinner = findViewById(R.id.from_currency_spinner);
        toCurrencySpinner = findViewById(R.id.to_currency_spinner);
        amountEditText = findViewById(R.id.amount_edit_text);
        resultTextView = findViewById(R.id.result_text_view);
        convertButton = findViewById(R.id.convert_button);

        // Llama a setupSpinners() después de inicializar los spinners
        setupSpinners();

        convertButton.setOnClickListener(view -> {
            String fromCurrency = fromCurrencySpinner.getSelectedItem().toString();
            String toCurrency = toCurrencySpinner.getSelectedItem().toString();
            String amountStr = amountEditText.getText().toString();
            if (!amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                convertCurrency(fromCurrency, toCurrency, amount);
            } else {
                Toast.makeText(CurrencyConverterActivity.this, "Por favor, ingrese una cantidad", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void convertCurrency(String fromCurrency, String toCurrency, double amount) {
        Query query = db.collection("exchangeRates").orderBy("timestamp", Query.Direction.DESCENDING).limit(1);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot result = task.getResult();
                if (result != null && !result.isEmpty()) {
                    DocumentSnapshot document = result.getDocuments().get(0);
                    Map<String, Object> ratesData = (Map<String, Object>) document.get("rates");
                    if (ratesData != null && ratesData.containsKey(toCurrency) && ratesData.containsKey(fromCurrency)) {
                        double toCurrencyRate = ((Number) ratesData.get(toCurrency)).doubleValue();
                        double fromCurrencyRate = ((Number) ratesData.get(fromCurrency)).doubleValue();
                        double convertedAmount = (amount * toCurrencyRate) / fromCurrencyRate;
                        resultTextView.setText(String.format(Locale.getDefault(), "%.2f", convertedAmount));
                    } else {
                        Toast.makeText(CurrencyConverterActivity.this, "Moneda no disponible", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CurrencyConverterActivity.this, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CurrencyConverterActivity.this, "Error en la llamada a la API", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupSpinners() {
        // Lista de monedas que quieres mostrar en los spinners
        List<String> currencyList = Arrays.asList("USD", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD",
                "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF",
                "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL",
                "HRK", "HTG", "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD",
                "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR",
                "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR",
                "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLE", "SLL", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD",
                "TVD", "TWD", "TZS", "UAH", "UGX", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW", "ZWL");


        // Crea un ArrayAdapter usando el string array y un spinner predeterminado
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyList);

        // Especifica el layout a utilizar cuando la lista de opciones aparece
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplica el adaptador a los spinners
        fromCurrencySpinner.setAdapter(adapter);
        toCurrencySpinner.setAdapter(adapter);
    }
}
