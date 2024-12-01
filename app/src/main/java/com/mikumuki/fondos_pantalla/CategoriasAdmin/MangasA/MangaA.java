package com.mikumuki.fondos_pantalla.CategoriasAdmin.MangasA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikumuki.fondos_pantalla.R;

public class MangaA extends AppCompatActivity {


    /*Listar imagenes*/

    RecyclerView recyclerViewManga;
    FirebaseDatabase mFirebaseDataBase;
    DatabaseReference mRef;

    FirebaseRecyclerAdapter<Manga,ViewHolderManga> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Manga> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);

        //Titulo + boton de retroceso.

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Manga");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Iniciar las variables del Listar

        recyclerViewManga = findViewById(R.id.recyclerViewManga);
        recyclerViewManga.setHasFixedSize(true);                //Se modifice deacuerdo a lo que se haga en la base

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDataBase.getReference("´/");        //Nombre de la base de datos en Firebase Storage



        //Metodo para listar las imagenes en la categoria Mangas




    }


    //Icono agregar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_agregar,menu);
        menuInflater.inflate(R.menu.menu_vista,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.Agregar:
                startActivity(new Intent(MangaA.this,AgregarMangas.class));
                break;
            case R.id.Vista:
                Toast.makeText(this, "Listar Imagenes", Toast.LENGTH_SHORT).show();
                break;
                

        }
        return super.onOptionsItemSelected(item);
    }

    //Accion del retroceso
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return super.onSupportNavigateUp();
    }
}