/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author ingri
 */
public class Cliente {
  private String idInterno;
    private String nombre;
    private String correo;
    private String telefono;
    private byte[] cedulaCifrada;

    public Cliente() {}

    public Cliente(String idInterno, String nombre, String correo, String telefono, byte[] cedulaCifrada) {
        this.idInterno = idInterno;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.cedulaCifrada = cedulaCifrada != null ? Arrays.copyOf(cedulaCifrada, cedulaCifrada.length) : null;
    }   
    
    public String getIdInterno() { return idInterno; }
    public void setIdInterno(String idInterno) { this.idInterno = idInterno; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public byte[] getCedulaCifrada() {
        return cedulaCifrada != null ? Arrays.copyOf(cedulaCifrada, cedulaCifrada.length) : null;
    }

    public void setCedulaCifrada(byte[] cedulaCifrada) {
        this.cedulaCifrada = cedulaCifrada != null ? Arrays.copyOf(cedulaCifrada, cedulaCifrada.length) : null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(idInterno, cliente.idInterno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInterno);
    }
 @Override
    public String toString() {
        return "Cliente{" +
                "idInterno='" + idInterno + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", cedulaCifrada=" + (cedulaCifrada != null ? "[CIFRADA]" : "null") +
                '}';
    }
}

