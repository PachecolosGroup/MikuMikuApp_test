package com.mikumuki.fondos_pantalla;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mikumuki.fondos_pantalla.FragmentosAdministrador.InicioAdmin;
import com.mikumuki.fondos_pantalla.FragmentosAdministrador.ListaAdmin;
import com.mikumuki.fondos_pantalla.FragmentosAdministrador.PerfilAdmin;
import com.mikumuki.fondos_pantalla.FragmentosAdministrador.RegistroAdmin;
import com.mikumuki.fondos_pantalla.FragmentosClientes.AcercaDeCliente;
import com.mikumuki.fondos_pantalla.FragmentosClientes.CompartirCliente;
import com.mikumuki.fondos_pantalla.FragmentosClientes.InicioCliente;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toggle = new  ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        //fragmento por defecto

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new InicioCliente()).commit();
            navigationView.setCheckedItem(R.id.InicioCliente);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.InicioCliente:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new InicioCliente()).commit();

                break;

            case R.id.AcercaDe:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AcercaDeCliente()).commit();
                break;

            case R.id.Compartir:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CompartirCliente()).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}