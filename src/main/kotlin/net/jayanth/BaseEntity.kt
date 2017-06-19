package net.jayanth
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass

/**
 * Created by jmp on 6/17/2017.
 */

@MappedSuperclass
abstract class BaseEntity(@Id @GeneratedValue(strategy =GenerationType.AUTO) var id: Long? = null)