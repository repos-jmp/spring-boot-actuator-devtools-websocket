package net.jayanth

import javax.persistence.Entity
import javax.persistence.OneToOne

/**
 * Created by jmp on 6/17/2017.
 */

@Entity
class Image(var name: String, @OneToOne var owner: User) : BaseEntity()