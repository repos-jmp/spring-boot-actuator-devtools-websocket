package net.jayanth

import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.Entity

/**
 * Created by jmp on 6/20/2017.
 */

@Entity
class User (var username: String, var password: String, var roles: Array<String>): BaseEntity(){

}