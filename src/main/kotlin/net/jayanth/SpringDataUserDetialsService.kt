package net.jayanth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * Created by jmp on 6/20/2017.
 */
@Component
class SpringDataUserDetialsService @Autowired constructor(var repository: UserRepository): UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        var user: User = repository.findByUsername(username!!)
        return org.springframework.security.core.userdetails.User(user.username, user.password, AuthorityUtils.createAuthorityList(*user.roles))
    }
}