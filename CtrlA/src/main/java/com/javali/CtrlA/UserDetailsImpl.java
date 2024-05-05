package com.javali.CtrlA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.javali.CtrlA.modelo.Perfil;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID =1L;
	
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> autoridade;
	
	public UserDetailsImpl() {
	}
	
	public UserDetailsImpl(String email, String senha, List<Perfil> autoridade) {
		this.email = email;
		this.senha = senha;
		this.gerarAutoridades(autoridade);
	}
	
	private void gerarAutoridades(List<Perfil> perfil) {
		List<SimpleGrantedAuthority> autoridadesPerfis = new ArrayList<>();
		for (Perfil p : perfil) {
			autoridadesPerfis.add(new SimpleGrantedAuthority(p.name()));
		}
		this.autoridade = autoridadesPerfis;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.autoridade;
	}
	
	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.email;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
