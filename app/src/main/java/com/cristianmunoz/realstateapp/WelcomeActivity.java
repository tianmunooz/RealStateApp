package com.cristianmunoz.realstateapp;

        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;
        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;
        import com.google.android.material.textfield.TextInputEditText;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.FirebaseFirestore;
        import com.google.firebase.firestore.Query;
        import com.google.firebase.firestore.QueryDocumentSnapshot;
        import com.google.firebase.firestore.QuerySnapshot;
        import java.util.ArrayList;
        import java.util.List;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;
        import java.util.Arrays;
        import java.util.List;


public class WelcomeActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProperties;
    private PropertyAdapter propertyAdapter;
    private FirebaseFirestore db;
    private DocumentSnapshot lastVisible;
    private Button btnLoadMore;
    private Spinner spinnerProvinces;

    private List<Property> propertyList; // Definir propertyList aquí

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

        recyclerViewProperties = findViewById(R.id.recyclerViewProperties);
        recyclerViewProperties.setLayoutManager(new LinearLayoutManager(this));
        btnLoadMore = findViewById(R.id.btnLoadMore);

        db = FirebaseFirestore.getInstance();

        propertyList = new ArrayList<>(); // Inicializar propertyList aquí

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
        }

        query.limit(10).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    lastVisible = querySnapshot.getDocuments().get(querySnapshot.size() - 1);
                    List<Property> newProperties = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        String title = documentSnapshot.getString("title");
                        String price = documentSnapshot.getLong("price").toString();
                        String location = documentSnapshot.getString("location");
                        String size = documentSnapshot.getLong("size").toString();
                        String imageUrl = documentSnapshot.getString("image_url");

                        newProperties.add(new Property(title, price, location, size, imageUrl));
                    }

                    if (loadMore) {
                        propertyList.addAll(newProperties);
                    } else {
                        propertyList = newProperties;
                    }
                    propertyAdapter.setPropertyList(propertyList);
                }
            } else {
                Toast.makeText(WelcomeActivity.this, "Error al cargar propiedades", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSearchClicked(View view) {
        String selectedProvince = spinnerProvinces.getSelectedItem().toString();
        String minPriceStr = ((TextInputEditText) findViewById(R.id.editTextMinPrice)).getText().toString();
        String maxPriceStr = ((TextInputEditText) findViewById(R.id.editTextMaxPrice)).getText().toString();
        String minSizeStr = ((TextInputEditText) findViewById(R.id.editTextMinSize)).getText().toString();
        String maxSizeStr = ((TextInputEditText) findViewById(R.id.editTextMaxSize)).getText().toString();

        Query query = db.collection("real_estate_data_new");

        // Aplicación de filtros
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

        // Realizando la consulta
        query.limit(10).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    List<Property> newProperties = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        // Suponiendo que estos son los campos en tus documentos de Firestore
                        String title = documentSnapshot.getString("title");
                        String price = documentSnapshot.getLong("price").toString();
                        String location = documentSnapshot.getString("location");
                        String size = documentSnapshot.getLong("size").toString();
                        String imageUrl = documentSnapshot.getString("image_url");

                        newProperties.add(new Property(title, price, location, size, imageUrl));
                    }
                    propertyAdapter.setPropertyList(newProperties);
                } else {
                    Toast.makeText(WelcomeActivity.this, "No se encontraron propiedades", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("FirestoreQueryError", "Error: ", task.getException());
                Toast.makeText(WelcomeActivity.this, "Error al realizar la consulta", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
