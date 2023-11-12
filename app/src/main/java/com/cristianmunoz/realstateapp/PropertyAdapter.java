package com.cristianmunoz.realstateapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private List<Property> propertyList;

    // Constructor que acepta una lista de Property
    public PropertyAdapter(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    // Método para actualizar la lista de propiedades
    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
        notifyDataSetChanged();  // Notificar al adaptador que los datos han cambiado
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_property, parent, false);
        return new PropertyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);
        holder.textViewTitle.setText(property.getTitle());
        holder.textViewPrice.setText(property.getPrice());
        holder.textViewLocation.setText(property.getLocation());
        holder.textViewSize.setText(property.getSize());

        if (property.getImageUrl() != null && !property.getImageUrl().isEmpty()) {
            Picasso.get().load(property.getImageUrl()).into(holder.imageViewProperty);
        } else {
            // Configura una imagen de reserva si la URL está vacía NO TENGO TIEMPO AAAAAA
            //holder.imageViewProperty.setImageResource(R.drawable.image_placeholder);
        }

        holder.likeButton.setOnClickListener(view -> {
            // Lógica para el botón 'like'
        });
    }


    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProperty;
        TextView textViewTitle, textViewPrice, textViewLocation, textViewSize;
        Button likeButton;

        PropertyViewHolder(View itemView) {
            super(itemView);
            imageViewProperty = itemView.findViewById(R.id.imageViewProperty);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewSize = itemView.findViewById(R.id.textViewSize);
            likeButton = itemView.findViewById(R.id.likeButton);
        }
    }
}
