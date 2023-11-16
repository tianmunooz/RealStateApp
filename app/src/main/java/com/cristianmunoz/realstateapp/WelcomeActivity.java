package com.cristianmunoz.realstateapp;

        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.Toast;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import com.google.android.material.textfield.TextInputEditText;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.Query;
        import com.google.firebase.firestore.QuerySnapshot;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";
    private RecyclerView recyclerViewProperties;
    private PropertyAdapter propertyAdapter;
    private FirebaseFirestore db;
    private DocumentSnapshot lastVisible;
    private Button btnLoadMore;
    private Spinner spinnerProvinces;
    private List<Property> propertyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

        recyclerViewProperties = findViewById(R.id.recyclerViewProperties);
        recyclerViewProperties.setLayoutManager(new LinearLayoutManager(this));
        btnLoadMore = findViewById(R.id.btnLoadMore);

        db = FirebaseFirestore.getInstance();

        propertyAdapter = new PropertyAdapter(propertyList);
        recyclerViewProperties.setAdapter(propertyAdapter);

        btnLoadMore.setOnClickListener(v -> loadPropertiesFromFirestore(true));
        loadPropertiesFromFirestore(false);

        setupProvinceSpinner();
    }

    private void setupProvinceSpinner() {
        spinnerProvinces = findViewById(R.id.spinnerProvinces);
        List<String> provinces = Arrays.asList("araba", "albacete", "alicante", "almería", "asturias", "ávila", "badajoz", "barcelona", "burgos", "cáceres", "cádiz", "cantabria", "castellon", "ciudad real", "córdoba", "la coruña", "cuenca", "gerona", "granada", "guadalajara", "gipuzkoa", "huelva", "huesca", "baleares", "jaén", "león", "lerida", "lugo", "madrid", "malaga", "murcia", "navarra", "orense", "palencia", "las palmas", "pontevedra", "la rioja", "salamanca", "segovia", "sevilla", "soria", "tarragona", "santa cruz de tenerife", "teruel", "toledo", "valencia", "valladolid", "bizkaia", "zamora", "zaragoza");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provinces);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvinces.setAdapter(adapter);
    }

    private void loadPropertiesFromFirestore(boolean loadMore) {
        Query query = db.collection("real_estate_data_new");

        if (loadMore && lastVisible != null) {
            query = query.startAfter(lastVisible);
            Log.d("WelcomeActivity", "Loading more properties...");
        } else {
            Log.d("WelcomeActivity", "Loading initial properties...");
        }

        query.limit(10).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    lastVisible = querySnapshot.getDocuments().get(querySnapshot.size() - 1);

                    if (!loadMore) {
                        propertyList.clear();
                    }

                    int initialSize = propertyList.size();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        Property property = documentSnapshot.toObject(Property.class);
                        if (property != null) {
                            propertyList.add(property);
                            Log.d("WelcomeActivity", "Added property: " + property.getTitle());
                        }
                    }

                    int newSize = propertyList.size();
                    Log.d("WelcomeActivity", "Number of properties fetched: " + (newSize - initialSize));

                    runOnUiThread(() -> {
                        propertyAdapter.setPropertyList(propertyList);
                        Log.d("WelcomeActivity", "Property list updated. Size: " + propertyList.size());
                        if (loadMore) {
                            recyclerViewProperties.scrollToPosition(initialSize - 1);
                        }
                    });
                } else if (!loadMore) {
                    runOnUiThread(() -> Toast.makeText(WelcomeActivity.this, "No se encontraron propiedades", Toast.LENGTH_SHORT).show());
                }
            } else {
                runOnUiThread(() -> Toast.makeText(WelcomeActivity.this, "Error al cargar propiedades", Toast.LENGTH_SHORT).show());
            }
        });
    }


    public void onSearchClicked(View view) {
        lastVisible = null;
        applyFiltersAndLoad();
    }

    private void applyFiltersAndLoad() {
        String selectedProvince = spinnerProvinces.getSelectedItem().toString();
        String minPriceStr = ((TextInputEditText) findViewById(R.id.editTextMinPrice)).getText().toString();
        String maxPriceStr = ((TextInputEditText) findViewById(R.id.editTextMaxPrice)).getText().toString();
        String minSizeStr = ((TextInputEditText) findViewById(R.id.editTextMinSize)).getText().toString();
        String maxSizeStr = ((TextInputEditText) findViewById(R.id.editTextMaxSize)).getText().toString();

        Query query = db.collection("real_estate_data_new");

        if (!selectedProvince.isEmpty()) {
            query = query.whereEqualTo("province", selectedProvince);
        }

        try {
            if (!minPriceStr.isEmpty()) {
                long minPrice = Long.parseLong(minPriceStr);
                query = query.whereGreaterThanOrEqualTo("price", minPrice);
            }
            if (!maxPriceStr.isEmpty()) {
                long maxPrice = Long.parseLong(maxPriceStr);
                query = query.whereLessThanOrEqualTo("price", maxPrice);
            }
            if (!minSizeStr.isEmpty()) {
                long minSize = Long.parseLong(minSizeStr);
                query = query.whereGreaterThanOrEqualTo("size", minSize);
            }
            if (!maxSizeStr.isEmpty()) {
                long maxSize = Long.parseLong(maxSizeStr);
                query = query.whereLessThanOrEqualTo("size", maxSize);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error en los valores numéricos", Toast.LENGTH_SHORT).show();
            return;
        }

        query.limit(10).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    propertyList.clear();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        Property property = documentSnapshot.toObject(Property.class);
                        if (property != null) {
                            propertyList.add(property);
                        }
                    }
                    propertyAdapter.setPropertyList(propertyList);
                } else {
                    Toast.makeText(WelcomeActivity.this, "No se encontraron propiedades con los filtros aplicados", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(WelcomeActivity.this, "Error al realizar la consulta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
