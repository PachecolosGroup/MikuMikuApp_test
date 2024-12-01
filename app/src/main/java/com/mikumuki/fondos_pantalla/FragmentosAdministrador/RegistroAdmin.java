package com.mikumuki.fondos_pantalla.FragmentosAdministrador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikumuki.fondos_pantalla.MainActivity;
import com.mikumuki.fondos_pantalla.MainActivityAdministrador;
import com.mikumuki.fondos_pantalla.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class RegistroAdmin extends Fragment {

    TextView FechaRegistro;
    EditText Correo, Password, Nombres, Apellidos, Edad;
    Button Registrar;


    FirebaseAuth auth;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registrar_admin, container, false);

        FechaRegistro = view.findViewById(R.id.FechaRegistro);
        Correo = view.findViewById(R.id.Correo);
        Password = view.findViewById(R.id.Password);
        Nombres = view.findViewById(R.id.Nombres);
        Apellidos = view.findViewById(R.id.Apellidos);
        Edad = view.findViewById(R.id.Edad);


        Registrar = view.findViewById(R.id.Registrar);

        auth = FirebaseAuth.getInstance(); //iniciando firebase autentication

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat( "d 'de' MMM 'del' yyyy"); //27 de octubre del 2022
        String SFecha = fecha.format(date); //convertimos la linea de fecha a String
        FechaRegistro.setText(SFecha);

        /// al hacer click en registrar

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = Correo.getText().toString();
                String pass = Password.getText().toString();
                String nombre = Nombres.getText().toString();
                String apellidos = Apellidos.getText().toString();
                String edad = Edad.getText().toString();

                //Tener un registro de administradores exitosos, y evitar el crash de la app.
                //Sirve tambien para generar un toast, donde se muestre que es obligatgorio llenar todos los campos.

                if(correo.equals("") || pass.equals("") || nombre.equals("") || apellidos.equals("")
                || edad.equals("")){
                    Toast.makeText(getActivity(), "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
                }else {
                    //validacion del correo electronico

                    if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
                        Correo.setError("Correo Invalido");
                        Correo.setFocusable(true);

                    }

                    else if (pass.length()<6){
                        Password.setError("La contraseña debe ser mayor o igual a 6");
                        Password.setFocusable(true);
                    }
                    else {
                        RegistroAdministradores(correo,pass);

                    }


                }

            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando, espere por favor");
        progressDialog.setCancelable(false);

        return view;
    }

    //Metodo para registrar administradores

    private void RegistroAdministradores(String correo, String pass) {

        progressDialog.show();
        auth.createUserWithEmailAndPassword(correo,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ///Si el administrador fue creado correctamente

                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = auth.getCurrentUser();
                            assert user != null; //afirmar que el admin no es objeto nulo
                            //Convertir a cadena los datos de los administradores

                            String UID = user.getUid();
                            String correo = Correo.getText().toString();
                            String pass = Password.getText().toString();
                            String nombre = Nombres.getText().toString();
                            String apellidos = Apellidos.getText().toString();
                            String edad = Edad.getText().toString();
                            int EdadInt = Integer.parseInt(edad);

                            HashMap<Object, Object> Administradores = new HashMap<>();

                            Administradores.put("UID", UID);
                            Administradores.put("CORREO", correo);
                            Administradores.put("PASSWORD", pass);
                            Administradores.put("Apellidos", apellidos);
                            Administradores.put("Edad", EdadInt);
                            Administradores.put("IMAGEN", "");


                            //Iniciar Firebase Database

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference reference = database.getReference("BASE DE DATOS ADMINISTRADOS");
                            reference.child(UID).setValue(Administradores);
                            startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                            Toast.makeText(getActivity(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Ha ocurddio un error", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}