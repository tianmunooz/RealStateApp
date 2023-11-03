package com.cristianmunoz.realstateapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DateFormatWatcher implements TextWatcher {

    private static final String DATE_PATTERN = "##/##/####";
    private boolean isUpdating;
    private EditText editText;

    public DateFormatWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String str = s.toString().replaceAll("[^\\d]", "");  // Removes all non-digit characters
        String mascara = "";

        if (isUpdating) {
            isUpdating = false;
            return;
        }

        int i = 0;
        for (char m : DATE_PATTERN.toCharArray()) {
            if (m != '#' && str.length() > i) {
                mascara += m;
                continue;
            }
            try {
                mascara += str.charAt(i);
            } catch (Exception e) {
                break;
            }
            i++;
        }

        isUpdating = true;
        editText.setText(mascara);
        editText.setSelection(mascara.length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
