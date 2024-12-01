package com.mikumuki.fondos_pantalla.FragmentosAdministrador;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mikumuki.fondos_pantalla.CategoriasAdmin.AnimesA.AnimeA;
import com.mikumuki.fondos_pantalla.CategoriasAdmin.ComicsA.ComicsA;
import com.mikumuki.fondos_pantalla.CategoriasAdmin.DibujosA.DibujosA;
import com.mikumuki.fondos_pantalla.CategoriasAdmin.MangasA.MangaA;
import com.mikumuki.fondos_pantalla.R;

public class InicioAdmin extends Fragment {

    Button Manga, Anime, Dibujos, Comics;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_admin, container, false);

        Manga = view.findViewById(R.id.Manga);
        Anime = view.findViewById(R.id.Anime);
        Dibujos = view.findViewById(R.id.Dibujos);
        Comics = view.findViewById(R.id.Comics);

        Manga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), MangaA.class));

            }
        });

        Anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), AnimeA.class));

            }
        });

        Dibujos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), DibujosA.class));

            }
        });

        Comics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ComicsA.class));

            }
        });


        return view;
    }
}