package com.example.springstart.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String token;

    public RefreshToken(String username, String token){
        this.username =username;
        this.token = token;
    }

    public RefreshToken updateToken(String newToken){
        this.token = newToken;
        return this;
    }
}
