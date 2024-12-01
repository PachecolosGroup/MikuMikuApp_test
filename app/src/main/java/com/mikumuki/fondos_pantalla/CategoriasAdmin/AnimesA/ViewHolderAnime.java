package com.mikumuki.fondos_pantalla.CategoriasAdmin.AnimesA;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;
import com.mikumuki.fondos_pantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderAnime extends RecyclerView.ViewHolder {

    View mview;

    private ViewHolderAnime.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);    /*Admin Presiona Normal el Item*/
        void onItemLongClick(View view, int position);  /*Admin mantiene presionado el Item*/
    }


    //Metodo para poder presionar un item
    public void setOnClickListener(ViewHolderAnime .ClickListener clickListener){
        mClickListener = clickListener;
    }


    public ViewHolderAnime(@NonNull View itemView) {
        super(itemView);
        mview = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view,getAdapterPosition());
                return true;
            }
        });


    }

    public void SeteoAnimes(Context context, String nombre, int vista, String imagen){

        ImageView ImagenAnime;
        TextView NombreImagenAnime;
        TextView VistaAnime;

        //Conexion con el item

        ImagenAnime= mview.findViewById(R.id.ImagenAnime);
        NombreImagenAnime = mview.findViewById(R.id.NombreImagenAnime);
        VistaAnime = mview.findViewById(R.id.VistaAnime);

        NombreImagenAnime.setText(nombre);

        //Convertir a String parametro vista

        String VistaString = String.valueOf(vista);

        VistaAnime.setText(VistaString);

        // Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente
            Picasso.get().load(imagen).into(ImagenAnime);
        }
        catch (Exception e){
            //Si la imagen no fue traida exitosamente
            //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            Log.d("Mensaje", "Intentelo de nuevo");
        }


    }


}
