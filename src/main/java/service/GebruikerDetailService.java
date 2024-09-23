package service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import domain.Gebruiker;
import domain.Rol;
//import lombok.NoArgsConstructor;
import repository.GebruikerRepository;

@Service
//@NoArgsConstructor
public class GebruikerDetailService implements UserDetailsService {
	
	@Autowired
	private GebruikerRepository gebruikerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Gebruiker user = gebruikerRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(user.getUsername(), user.getPassword(), convertAuthorities(user.getRol()));
	}
	
	  private Collection<? extends GrantedAuthority> convertAuthorities(Rol rol) {
		    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.toString()));
	  }
	  
	//als een gebruiker meerdere rollen heeft -> kan uitgebreid worden

}
