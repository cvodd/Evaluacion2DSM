package com.example.evaluaciondsm.ui.home;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.evaluaciondsm.AdminSQLiteOpenHelper;
import com.example.evaluaciondsm.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private EditText txt_code, txt_description, txt_price;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        txt_code = binding.txtCode;
        txt_description =  binding.txtDescription;
        txt_price =  binding.txtPrice;

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        binding.btnSearchP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Metodo para guardar los productos
    public void save() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(getActivity(), "administration", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        String code = txt_code.getText().toString();
        String description = txt_description.getText().toString();
        String price = txt_price.getText().toString();
        Cursor rawquery = database.rawQuery("select code from products where code =" + code, null);

        if(rawquery.moveToFirst()){
            Toast.makeText(getActivity(), "El producto ya existe", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!code.isEmpty() && !description.isEmpty() && !price.isEmpty()) {
            ContentValues register = new ContentValues();
            register.put("code", code);
            register.put("description", description);
            register.put("price", price);
            database.insert("products", null, register);
            database.close();
            clean();
            Toast.makeText(getActivity(), "Producto agregado", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Todos los campos son necesarios", Toast.LENGTH_SHORT).show();
        }
    }


    //Metodo para buscar productos
    public void search() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getActivity(), "administration", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        admin.getWritableDatabase();
        String code = txt_code.getText().toString();
        if (!code.isEmpty()) {
            Cursor row = database.rawQuery("select description, price  from products where code =" + code, null);
            if (row.moveToFirst()) {
                txt_description.setText(row.getString(0));
                txt_price.setText(row.getString(1));
                database.close();
            } else {
                Toast.makeText(this.getActivity(), "No se encontro producto", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(this.getActivity(), "No se ingreso un codigo", Toast.LENGTH_SHORT).show();
        }

    }

    public void delete() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getActivity(), "administration", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();
        admin.getWritableDatabase();
        String code = txt_code.getText().toString();
        if (!code.isEmpty()) {
            int count = database.delete("products", "code=" + code, null);
            if (count == 1) {
                database.close();
                Toast.makeText(this.getActivity(), "Eliminado exitosamente", Toast.LENGTH_SHORT).show();
                clean();
            } else {
                Toast.makeText(this.getActivity(), "El producto no existe", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this.getActivity(), "No se ingreso un codigo", Toast.LENGTH_SHORT).show();
        }
    }

    public void edit() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this.getActivity(), "administration", null, 1);
        SQLiteDatabase database = admin.getWritableDatabase();

        String code = txt_code.getText().toString();
        String description = txt_description.getText().toString();
        String price = txt_price.getText().toString();

        if (!code.isEmpty() && !description.isEmpty() && !price.isEmpty()) {
            ContentValues register = new ContentValues();
            register.put("code", code);
            register.put("description", description);
            register.put("price", price);
            int cant = database.update("products", register, "code=" + code, null);
            if (cant == 1) {
                database.close();
                Toast.makeText(this.getActivity(), "El producto fue actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getActivity(), "Error al editar", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this.getActivity(), "No se ingreso un codigo", Toast.LENGTH_SHORT).show();
        }
    }
    public void clean(){
        txt_code.setText("");
        txt_description.setText("");
        txt_price.setText("");
    }


}