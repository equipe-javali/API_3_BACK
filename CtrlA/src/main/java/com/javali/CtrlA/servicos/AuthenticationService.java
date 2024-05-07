package com.javali.CtrlA.servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.javali.CtrlA.entidades.Usuario;
import com.javali.CtrlA.modelos.ModeloAutenticacao;
import com.javali.CtrlA.repositorios.UsuarioRepositorio;

@Service
public class AuthenticationService {
	@Autowired
	private UserDetailsServiceImpl userDetailsServico;
	@Autowired
	private UsuarioLoginValidadorServico usuarioValidadorServico;
	@Autowired
	private UsuarioRepositorio usuarioProcurar;
	@Autowired
	private JwtService jwtService;

	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	private final String secret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
	private final long duration = 9000000;

	
	public ResponseEntity<?> authenticate(Usuario loginUsuario) {
		ResponseEntity<?> response = new ResponseEntity<>(new ModeloAutenticacao(), HttpStatus.BAD_REQUEST);
		if (usuarioValidadorServico.isCredentialValid(loginUsuario)) {
			UserDetails userDetails = this.userDetailsServico.loadUserByUsername(loginUsuario.getEmail());
			if (userDetails != null) {
				Usuario usuario = usuarioProcurar.findByEmail(loginUsuario.getEmail());
				String jwtToken = this.jwtService.createToken(userDetails.getUsername(), duration, secret);
				jwtToken = "Bearer " + jwtToken;
				ModeloAutenticacao modelo = new ModeloAutenticacao(jwtToken, usuario);
				response = new ResponseEntity<>(modelo, HttpStatus.ACCEPTED);
			}
		}
		return response;
	}
}
