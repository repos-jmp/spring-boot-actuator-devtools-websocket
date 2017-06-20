package net.jayanth

import org.springframework.data.repository.CrudRepository
import org.springframework.security.core.userdetails.UserDetailsService

/**
 * Created by jmp on 6/20/2017.
 */
interface UserRepository: CrudRepository<User, Long> {

    fun findByUsername(username: String): User
}