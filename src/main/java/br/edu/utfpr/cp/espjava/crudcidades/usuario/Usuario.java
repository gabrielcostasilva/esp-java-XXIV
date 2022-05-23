package br.edu.utfpr.cp.espjava.crudcidades.usuario;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Usuario implements Serializable, UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String senha;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> papeis;
    
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public List<String> getPapeis() {
        return papeis;
    }

    public String getSenha() {
        return senha;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPapeis(List<String> papeis) {
        this.papeis = papeis;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String getUsername() {
        return this.nome;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.papeis
                        .stream()
                        .map(papelAtual -> new SimpleGrantedAuthority(papelAtual))
                        .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
}
