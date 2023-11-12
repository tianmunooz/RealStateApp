package com.cristianmunoz.realstateapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProperties;
    private PropertyAdapter propertyAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_user);

        recyclerViewProperties = findViewById(R.id.recyclerViewProperties);
        recyclerViewProperties.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        // Inicializa la lista de propiedades
        List<Property> propertyList = new ArrayList<>();

        // Crea el adaptador con la lista de propiedades
        propertyAdapter = new PropertyAdapter(propertyList);
        recyclerViewProperties.setAdapter(propertyAdapter);

        // Cargar datos desde Firestore
        loadPropertiesFromFirestore();
    }



    private void loadPropertiesFromFirestore() {
        db.collection("real_estate_data_new").limit(10).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    List<Property> properties = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        String title = documentSnapshot.getString("title");
                        String price = documentSnapshot.getLong("price").toString();
                        String location = documentSnapshot.getString("location");
                        String size = documentSnapshot.getString("size");
                        String imageUrl = documentSnapshot.getString("image_url"); // Cambio aquí

                        properties.add(new Property(title, price, location, size, imageUrl));
                    }
                    propertyAdapter.setPropertyList(properties);
                }
            } else {
                //errores
            }
        });
    }

}
