package com.fei.arnutri;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.List;

public class Validation {



    public boolean validateField(List<EditText> fields){
        for(int i = 0; i < fields.size(); i++){
            EditText field = fields.get(i);
            if(TextUtils.isEmpty(field.getText())){
                field.setError("Field is required");
                return false;
            }
        }
        return true;
    }
}
