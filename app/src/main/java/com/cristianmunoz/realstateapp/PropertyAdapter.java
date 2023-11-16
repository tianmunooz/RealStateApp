package com.cristianmunoz.realstateapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private List<Property> propertyList;

    // Constructor that accepts a list of Property objects
    public PropertyAdapter(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    // Method to update the list of properties
    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
        notifyDataSetChanged(); // Notify the adapter that the data has changed
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

        // Converting Long to String for display
        holder.textViewPrice.setText(String.valueOf(property.getPrice()));
        holder.textViewLocation.setText(property.getLocation());
        holder.textViewSize.setText(String.valueOf(property.getSize()));

        // Use Picasso to load the image
        if (property.getImage_url() != null && !property.getImage_url().isEmpty()) {
            Picasso.get().load(property.getImage_url()).into(holder.imageViewProperty);
        } else {
            // If the image URL is empty or null, you can set a default image or leave it blank
            // Example: holder.imageViewProperty.setImageResource(R.drawable.default_image);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("PropertyAdapter", "Item count: " + propertyList.size());
        return propertyList.size();
    }

    // ViewHolder class
    static class PropertyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProperty;
        TextView textViewTitle, textViewPrice, textViewLocation, textViewSize;

        PropertyViewHolder(View itemView) {
            super(itemView);
            imageViewProperty = itemView.findViewById(R.id.imageViewProperty);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewSize = itemView.findViewById(R.id.textViewSize);
        }
    }
}

