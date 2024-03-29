package com.me.testproject.server.auth;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.me.testproject.server.entities.User;
import com.me.testproject.server.repositories.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User u = userRepository.findByUserName(username);
		if (u == null) {
			throw new UsernameNotFoundException("user doesnt exists");
		}
		return new UserDetailsWrapper(u, AuthorityUtils.commaSeparatedStringToAuthorityList(u.getAuthority()));
	}

}
