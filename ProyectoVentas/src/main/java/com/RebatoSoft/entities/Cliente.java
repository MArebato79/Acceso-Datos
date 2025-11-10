package com.RebatoSoft.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "cliente")
public class Cliente {


    private int id;
    private String dni;
    private String nombre;
    private String apellido;
    private String telefonoContacto;
    private String direccionHabitual;
    private String direccionEnvio;

    public Cliente(int id, String dni, String nombre, String apellido, String telefonoContacto, String direccionHabitual, String direccionEnvio) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefonoContacto = telefonoContacto;
        this.direccionHabitual = direccionHabitual;
        this.direccionEnvio = direccionEnvio;
    }

    public Cliente(String dni, String nombre, String apellido, String telefonoContacto, String direccionHabitual, String direccionEnvio) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefonoContacto = telefonoContacto;
        this.direccionHabitual = direccionHabitual;
        this.direccionEnvio = direccionEnvio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccionHabitual() {
        return direccionHabitual;
    }

    public void setDireccionHabitual(String direccionHabitual) {
        this.direccionHabitual = direccionHabitual;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    @Override
    public String toString() {
        return "Cliente=" +
                "id=" + id +
                "\ndni='" + dni + '\'' +
                "\nnombre='" + nombre + '\'' +
                "\napellido='" + apellido + '\'' +
                "\ntelefonoContacto='" + telefonoContacto + '\'' +
                "\ndireccionHabitual='" + direccionHabitual + '\'' +
                "\ndireccionEnvio='" + direccionEnvio + '\''+
                "\n------------------------\n";
    }

}
