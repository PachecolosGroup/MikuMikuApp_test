package com.mikumuki.fondos_pantalla.CategoriasAdmin.MangasA;

import android.app.AlertDialog;
import android.util.Log;
import android.view.ContentInfo;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.core.Context;
import com.mikumuki.fondos_pantalla.R;
import com.squareup.picasso.Picasso;

public class ViewHolderManga extends RecyclerView.ViewHolder {

    View mview;

    private ViewHolderManga.ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(View view, int position);    /*Admin Presiona Normal el Item*/
        void onItemLongClick(View view, int position);  /*Admin mantiene presionado el Item*/
    }


    //Metodo para poder presionar un item
    public void setOnClickListener(ViewHolderManga.ClickListener clickListener){
        mClickListener = clickListener;
    }


    public ViewHolderManga(@NonNull View itemView) {
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

    public void SeteoMangas(Context context, String nombre, int vista, String imagen){

        ImageView ImagenManga;
        TextView NombreImagenManga;
        TextView VistaMangas;

        //Conexion con el item

        ImagenManga = mview.findViewById(R.id.ImagenManga);
        NombreImagenManga = mview.findViewById(R.id.NombreImagenManga);
        VistaMangas = mview.findViewById(R.id.VistaMangas);

        NombreImagenManga.setText(nombre);

        //Convertir a String parametro vista

        String VistaString = String.valueOf(vista);

        VistaMangas.setText(VistaString);

        // Controlar posibles errores
        try {
            //Si la imagen fue traida exitosamente 
            Picasso.get().load(imagen).into(ImagenManga);
        }
        catch (Exception e){
            //Si la imagen no fue traida exitosamente 
            //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            Log.d("Mensaje", "Intentelo de nuevo");
        }


    }
}
