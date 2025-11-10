package com.RebatoSoft.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "empresa")
public class Empresa {
    private int id;
    private String cif;
    private String nombre;
    private String domicilio;
    private String localidad;

    public Empresa(int id, String cif, String nombre, String domicilio, String localidad) {
        this.id = id;
        this.cif = cif;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.localidad = localidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }


}
