package com.example.trabajo.model;

import java.util.Date;

public class Registro {
    private Date registro;
    private Float lectura;

    public Registro(Date registro, Float lectura) {
        this.registro = registro;
        this.lectura = lectura;

    }

    public Date getRegistro() {
        return registro;
    }

    public void setRegistro(Date registro) {
        this.registro = registro;
    }

    public Float getLectura() {
        return lectura;
    }

    public void setLectura(Float lectura) {
        this.lectura = lectura;
    }
}
