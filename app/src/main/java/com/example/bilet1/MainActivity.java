package com.example.bilet1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bilet1.database.service.CitatService;
import com.example.bilet1.network.NetworkManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.bilet1.AdaugaCitat.ADAUGA_CITAT_KEY;
import static com.example.bilet1.AdaugaCitat.RESULT_DELETE;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 200;
    public static final int REQUEST_CODE_UPDATE = 201;
    private static String API_URL = "https://api.jsonbin.io/b/5e288eb54f60034b5f22bb6b";
    private int selectedIndex;
    ArrayList<Citat> listaCitate = new ArrayList<>();
    ListView lvCitate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        alert();
        getCitateFromDatabase();
    }
    private void initComponents() {
        lvCitate = findViewById(R.id.main_lv_citate);
        lvCitate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AdaugaCitat.class);
                intent.putExtra(ADAUGA_CITAT_KEY, listaCitate.get(position));
                selectedIndex = position;
                startActivityForResult(intent,REQUEST_CODE_UPDATE);
            }
        });

        lvCitate.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                insertIntoDatabase(listaCitate.get(position));
                return true;
            }
        });


    }

    @SuppressLint("StaticFieldLeak")
    private void insertIntoDatabase (Citat citat) {
        new CitatService.Insert(getApplicationContext()) {
            @Override
            protected void onPostExecute(Citat result) {
                if(result != null) {
                    Toast.makeText(getApplicationContext(), "Citat adaugat in BD", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(citat);
    }

    @SuppressLint("StaticFieldLeak")
    private void  getCitateFromDatabase() {
        ArrayAdapter<Citat> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,listaCitate);
        lvCitate.setAdapter(adapter);
        new CitatService.GetAll(getApplicationContext()) {
            @Override
            protected void onPostExecute(List<Citat> results) {
                if(results != null) {
                    listaCitate.addAll(results);
                    notifyAdapter();
                }
            }
        }.execute();
    }
    private void notifyAdapter() {
        ArrayAdapter adapter = (ArrayAdapter) lvCitate.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    private void alert() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Mesaj")
                .setMessage("Popa Raluca Nicoleta" + Calendar.getInstance().getTime())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);

         switch (item.getItemId()) {
             case R.id.adauga_citat_item:
                 Intent intent = new Intent(getApplicationContext(), AdaugaCitat.class);
                 startActivityForResult(intent,REQUEST_CODE);
                 break;
             case R.id.sincronizare_retea_item:
                 new NetworkManager() {
                     @Override
                     protected void onPostExecute(List<Citat> citats) {
                         listaCitate.addAll(citats);
                         Toast.makeText(getApplicationContext(), listaCitate.toString(),Toast.LENGTH_LONG).show();
                         notifyAdapter();

                     }
                 }.execute(API_URL);
                 break;
         }
         return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Citat citat = data.getParcelableExtra(ADAUGA_CITAT_KEY);
            if(citat != null) {
                listaCitate.add(citat);
                Toast.makeText(getApplicationContext(), listaCitate.toString(),Toast.LENGTH_LONG).show();

                ArrayAdapter<Citat> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, listaCitate);
                lvCitate.setAdapter(adapter);
            }
        }

        if(requestCode == REQUEST_CODE_UPDATE && resultCode ==RESULT_OK && data != null) {
            Citat citat = data.getParcelableExtra(ADAUGA_CITAT_KEY);
            if(citat != null) {
                updateCitatIntoDatabase(citat);
                updateCitat(citat);
                ArrayAdapter<Citat> adapter = (ArrayAdapter<Citat>) lvCitate.getAdapter();
                adapter.notifyDataSetChanged();
            }
        }

        if(resultCode == RESULT_DELETE && data != null) {
            deleteCitatFromDatabase(selectedIndex);
           listaCitate.remove(selectedIndex);
            ArrayAdapter<Citat> adapter = (ArrayAdapter<Citat>) lvCitate.getAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    private void updateCitat(Citat citat) {
        listaCitate.get(selectedIndex).setAutor(citat.getAutor());
        listaCitate.get(selectedIndex).setText(citat.getText());
        listaCitate.get(selectedIndex).setNumarAprecieri(citat.getNumarAprecieri());
        listaCitate.get(selectedIndex).setCategorie(citat.getCategorie());
    }

    private void updateCitatIntoDatabase(final Citat citat) {
        citat.setId(listaCitate.get(selectedIndex).getId());
        new CitatService.Update(getApplicationContext()) {
            @Override
            protected void onPostExecute(Integer result) {
                if(result == 1) {
                    updateCitat(citat);
                    notifyAdapter();
                }
            }
        }.execute(citat);
    }

    private void deleteCitatFromDatabase(final int index) {
        new CitatService.Delete(getApplicationContext()) {
            @Override
            protected void onPostExecute(Integer result) {
                if(result == 1) {
                    Toast.makeText(getApplicationContext(), "S-a efectuat stergerea si in baza de date", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Nu s-a putut efectua stergerea", Toast.LENGTH_LONG).show();
                }
            }
        }.execute(listaCitate.get(index));
    }
}
