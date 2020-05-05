package com.example.bilet1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AdaugaCitat extends AppCompatActivity {

    public static final String ADAUGA_CITAT_KEY = "adaugaCitatKey";
    public  static final int RESULT_DELETE = 2;
    TextInputEditText etAutor;
    TextInputEditText etText;
    TextInputEditText etNrAprecieri;
    Spinner spnCategorie;
    Button btnSalveaza;
    Intent intent;
    Button btnSterge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_citat);
        intent = getIntent();
        initComponents();
        if(intent.hasExtra(ADAUGA_CITAT_KEY)) {
            Citat citat = intent.getParcelableExtra(ADAUGA_CITAT_KEY);
            updateUI(citat);
            Button btnSterge = new Button(getApplicationContext());
            btnSterge.setText("Sterge");

        }
    }

    private void updateUI(Citat citat) {
        etAutor.setText(citat.getAutor());
        etText.setText(citat.getText());
        if(citat.getNumarAprecieri() != null) {
            etNrAprecieri.setText(String.valueOf(citat.getNumarAprecieri()));
        }
        if(citat.getCategorie() != null) {
            addCategorie(citat);
        }
        btnSterge.setVisibility(View.VISIBLE);
        btnSterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_DELETE, intent);
                finish();
            }
        });
    }
    private void addCategorie(Citat citat) {

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spnCategorie.getAdapter();
        for(int i = 0; i<adapter.getCount(); i++) {
            if(adapter.getItem(i).equals(citat.getCategorie())) {
                spnCategorie.setSelection(i);
                break;
            }
        }
    }

    private void initComponents() {
        etAutor = findViewById(R.id.adauga_citat_et_autor);
        etText = findViewById(R.id.adauga_citat_et_text);
        etNrAprecieri= findViewById(R.id.adauga_citat_et_numArAprecieri);

        spnCategorie = findViewById(R.id.adauga_citat_spn_categorie);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.categorie_items,android.R.layout.simple_spinner_dropdown_item);
        spnCategorie.setAdapter(adapter);

        btnSalveaza = findViewById(R.id.adauga_citat_btn_salveaza);
        btnSterge = findViewById(R.id.adauga_citat_btn_sterge);

        btnSalveaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    Citat citat = createCitatFromView();
                    Toast.makeText(getApplicationContext(), citat.toString(), Toast.LENGTH_LONG).show();
                    intent.putExtra(ADAUGA_CITAT_KEY, citat);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

    private Citat createCitatFromView() {
        String autor = etAutor.getText().toString();
        String text = etText.getText().toString();
        Integer nrAprecieri = Integer.parseInt(etNrAprecieri.getText().toString());

        String categorie = spnCategorie.getSelectedItem().toString();

        return  new Citat(autor, text, nrAprecieri, categorie);

    }

    private boolean validate() {
        if(etAutor.getText() == null || etAutor.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.autor_message_error, Toast.LENGTH_LONG).show();
            return  false;
        }
        if(etText.getText() == null || etText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.text_message_error, Toast.LENGTH_LONG).show();
            return  false;
        }
        if(etNrAprecieri.getText() == null || etAutor.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.nrAprecieri_message_error, Toast.LENGTH_LONG).show();
            return  false;
        }
        return true;
    }
}
