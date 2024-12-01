package com.mikumuki.fondos_pantalla.CategoriasAdmin.MangasA;

import android.app.Notification;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikumuki.fondos_pantalla.R;

public class AgregarMangas  extends AppCompatActivity {

    TextView VistaManga;
    EditText NombreTXTMangas;
    ImageView ImagenAgregarManga;
    Button PublicarManga;

    String RutaDeAlmacenamiento = "Manga_Subida/";
    String RutaDeBaseDeDatos = "MANGAS";
    Uri RutaArchivoUri;

    StorageReference mStorageReference;
    DatabaseReference DatabaseReference;
    ProgressDialog progressDialog;

    int CODIGO_DE_SOLICITUD_IMAGEN = 5;


    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super .onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_mangas);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Publicar");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        VistaManga = findViewById(R.id.VistaMangas);
        NombreTXTMangas = findViewById(R.id.NombreTXTMangas);
        ImagenAgregarManga = findViewById(R.id.ImagenAgregarManga);
        PublicarManga = findViewById(R.id.PublicarManga);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference = FirebaseDatabase.getInstance().getReference(RutaDeBaseDeDatos);
        progressDialog = new ProgressDialog(AgregarMangas.this);

        //// Evento para ingresar a la galeria del telefono celular

        ImagenAgregarManga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"),CODIGO_DE_SOLICITUD_IMAGEN);

            }
        });

        //// Evento al pUBLICAR UNA NUEVA IMAGEN DE MANGA

        PublicarManga.setOnClickListener(new View.OnClickListener() {
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
            progressDialog.setMessage("Subiendo imagen Manga . . .");
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

                            String mNombre = NombreTXTMangas.getText().toString();
                            String mVista = VistaManga.getText().toString();
                            int VISTA = Integer.parseInt(mVista);

                            Manga manga = new Manga(downloadUri.toString(),mNombre,VISTA);
                            String ID_IMAGEN = DatabaseReference.push().getKey();

                            DatabaseReference.child(ID_IMAGEN).setValue(manga);

                            progressDialog.dismiss();
                            Toast.makeText(AgregarMangas.this, "Subido Exitosamente", Toast.LENGTH_SHORT).show();
                            
                            startActivity(new Intent(AgregarMangas.this,MangaA.class));
                            finish();





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AgregarMangas.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    //// Comprobar si la imagen seleccionada por el admin o el usuario esta correcta
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CODIGO_DE_SOLICITUD_IMAGEN
        && resultCode == RESULT_OK
        && data !=null
        && data.getData() !=null){

            RutaArchivoUri = data.getData();
            //// Verificamos si tiene algun error en la seleccion de dicha imagen

            try {
                //// Con esto lo que estamos haciendo es convertir la ruta de la imagen en un bitmap

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),RutaArchivoUri);
                //seteamos la imagen en la base de datos
                ImagenAgregarManga.setImageBitmap(bitmap); 

            } catch (Exception e){

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }


    }
}
}
