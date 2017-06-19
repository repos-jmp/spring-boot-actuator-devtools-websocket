package net.jayanth

import org.springframework.data.repository.PagingAndSortingRepository

/**
 * Created by jmp on 6/17/2017.
 */
interface ImageRepository: PagingAndSortingRepository<Image, Long> {
    fun findByName(name: String): Image
}