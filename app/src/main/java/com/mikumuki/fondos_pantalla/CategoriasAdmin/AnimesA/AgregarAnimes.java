package com.mikumuki.fondos_pantalla.CategoriasAdmin.AnimesA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikumuki.fondos_pantalla.CategoriasAdmin.MangasA.AgregarMangas;
import com.mikumuki.fondos_pantalla.CategoriasAdmin.MangasA.Manga;
import com.mikumuki.fondos_pantalla.CategoriasAdmin.MangasA.MangaA;
import com.mikumuki.fondos_pantalla.R;

public class AgregarAnimes extends AppCompatActivity {

    TextView VistaAnime;
    EditText NombreTXTAnime;
    ImageView ImagenAgregarAnime;
    Button PublicarAnime;

    String RutaDeAlmacenamiento = "Anime_Subida/";
    String RutaDeBaseDeDatos = "ANIMES";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;
    ProgressDialog progressDialog;

    int CODIGO_DE_SOLICITUD_IMAGEN = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_animes);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Publicar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        VistaAnime = findViewById(R.id.VistaAnime);
        NombreTXTAnime = findViewById(R.id.NombreTXTAnime);
        ImagenAgregarAnime = findViewById(R.id.ImagenAgregarAnime);
        PublicarAnime = findViewById(R.id.PublicarAnime);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarAnimes.this);

        //// Evento al presionar el boton de agregar una imagen

        ImagenAgregarAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"),CODIGO_DE_SOLICITUD_IMAGEN);

            }
        });

        //// Evento al pUBLICAR UNA NUEVA IMAGEN DE MANGA

        PublicarAnime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubirImagen();

            }
        });
    }

    //// Si la imagen esta seleciconada de manera correcta
    private void SubirImagen() {

        if (RutaArchivoUri!=null){
            progressDialog.setTitle("Espere por favor");
            progressDialog.setMessage("Subiendo imagen Anime . . .");
            progressDialog.show();
            progressDialog.setCancelable(false);
            StorageReference storageReference2 = mStorageReference.child(RutaDeAlmacenamiento+System.currentTimeMillis()+"."+ObtenerExtensionDelArchivo(RutaArchivoUri));
            storageReference2.putFile(RutaArchivoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());

                            Uri downloadUri = uriTask.getResult();

                            String mNombre = NombreTXTAnime.getText().toString();
                            String mVista = VistaAnime.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Anime anime = new Anime(downloadUri.toString(),mNombre,VISTA);
                            String ID_IMAGEN = DatabaseReference.push().getKey();

                            DatabaseReference.child(ID_IMAGEN).setValue(anime);

                            progressDialog.dismiss();
                            Toast.makeText(AgregarAnimes.this, "Subido Exitosamente", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(AgregarAnimes.this, AnimeA.class));
                            finish();





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AgregarAnimes.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                            progressDialog.setTitle("Publicando");
                            progressDialog.setCancelable(false);

                        }
                    });
        }

        else {
            Toast.makeText(this, "Seleccione correctamente una Imagen", Toast.LENGTH_SHORT).show();
        }
    }

    //// Metodo para extraer extension de la imagen --- SI es JPG-PNG-SVG.

    private String ObtenerExtensionDelArchivo(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODIGO_DE_SOLICITUD_IMAGEN
                && resultCode == RESULT_OK
                && data !=null
                && data.getData() !=null){

            RutaArchivoUri = data.getData();
            //// Verificamos si tiene algun error en la seleccion de dicha imagen

            try {
                //// Con esto lo que estamos haciendo es convertir la ruta de la imagen en un bitmap

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),RutaArchivoUri);
                ImagenAgregarAnime.setImageBitmap(bitmap);

            } catch (Exception e){

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }


        }
    }
}