package com.example.pb;

public class UserDetails {
    private String nama, email, nim;

    public UserDetails() {}

    public UserDetails(String nama, String email, String nim) {
        this.nama = nama;
        this.email = email;
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getNim() {
        return nim;
    }
}
