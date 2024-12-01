package com.mikumuki.fondos_pantalla.CategoriasAdmin.MangasA;

public class Manga {

    private String imagen;
    private String nombre;
    private int vistas;


    /* Constructor */

    public Manga(String imagen, String nombre, int vistas) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.vistas = vistas;
    }


    /* Getter & Setter */

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVistas() {
        return vistas;
    }

    public void setVistas(int vistas) {
        this.vistas = vistas;
    }
}
