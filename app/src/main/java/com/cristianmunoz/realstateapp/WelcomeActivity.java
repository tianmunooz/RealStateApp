package com.cristianmunoz.realstateapp;

        import android.os.Bundle;
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
        List<String> provinces = Arrays.asList(
                "Araba", "Albacete", "Alicante", "Almería", "Asturias", "Ávila", "Badajoz", "Barcelona",
                "Burgos", "Cáceres", "Cádiz", "Cantabria", "Castellon", "Ciudad Real", "Córdoba",
                "La Coruña", "Cuenca", "Gerona", "Granada", "Guadalajara", "Gipuzkoa", "Huelva",
                "Huesca", "Baleares", "Jaén", "León", "Lerida", "Lugo", "Madrid", "Malaga", "Murcia",
                "Navarra", "Orense", "Palencia", "Las Palmas", "Pontevedra", "La Rioja", "Salamanca",
                "Segovia", "Sevilla", "Soria", "Tarragona", "Santa Cruz de Tenerife", "Teruel",
                "Toledo", "Valencia", "Valladolid", "Bizkaia", "Zamora", "Zaragoza");

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

        if (!selectedProvince.isEmpty()) {
            query = query.whereEqualTo("province", selectedProvince);
        }
        if (!minPriceStr.isEmpty()) {
            try {
                long minPrice = Long.parseLong(minPriceStr);
                query = query.whereGreaterThanOrEqualTo("price", minPrice);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Precio mínimo no válido", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!maxPriceStr.isEmpty()) {
            try {
                long maxPrice = Long.parseLong(maxPriceStr);
                query = query.whereLessThanOrEqualTo("price", maxPrice);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Precio máximo no válido", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!minSizeStr.isEmpty()) {
            try {
                long minSize = Long.parseLong(minSizeStr);
                query = query.whereGreaterThanOrEqualTo("size", minSize);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Tamaño mínimo no válido", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (!maxSizeStr.isEmpty()) {
            try {
                long maxSize = Long.parseLong(maxSizeStr);
                query = query.whereLessThanOrEqualTo("size", maxSize);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Tamaño máximo no válido", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        query.limit(10).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Procesa los resultados...
            } else {
                Toast.makeText(WelcomeActivity.this, "Error al filtrar propiedades", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
