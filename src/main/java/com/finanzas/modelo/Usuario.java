package com.finanzas.modelo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Usuario implements Serializable {
    private int id;
    private String username;
    private String password;
    private String nombre;
    private LocalDateTime createdAt;

    public Usuario() {}

    public Usuario(int id, String username, String nombre) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
