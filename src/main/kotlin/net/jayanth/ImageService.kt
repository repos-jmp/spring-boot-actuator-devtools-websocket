package net.jayanth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.actuate.metrics.CounterService
import org.springframework.boot.actuate.metrics.GaugeService
import org.springframework.boot.actuate.metrics.Metric
import org.springframework.boot.actuate.metrics.repository.InMemoryMetricRepository
import org.springframework.boot.actuate.metrics.writer.Delta
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.query.Param
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by jmp on 6/17/2017.
 */

@Service
class ImageService @Autowired constructor (var imageRepository: ImageRepository, var resourceLoader: ResourceLoader,
                                           var report: ConditionEvaluationReport, var counterService: CounterService,
                                           var gaugeService: GaugeService, var inMemoryMetricRepository: InMemoryMetricRepository,
                                           var messagingTemplate: SimpMessagingTemplate, var userRepository: UserRepository) {

    init {
        counterService.reset("files.uploaded")
        gaugeService.submit("files.uploaded.lastBytes", 0.toDouble())
        inMemoryMetricRepository.set(Metric("files.uploaded.totalBytes",0))
    }

    val UPLOAD_ROOT: String = "upload-dir"

    fun findOneImage(fileName: String): Resource {
        return resourceLoader.getResource("file:$UPLOAD_ROOT/$fileName")
    }

    fun findPage(pageable: Pageable): Page<Image> {
        return imageRepository.findAll(pageable)
    }

    fun createImage(file: MultipartFile) {
        if (!file.isEmpty) {
            Files.copy(file.inputStream, Paths.get(UPLOAD_ROOT, file.originalFilename))
            imageRepository.save(Image(file.originalFilename,
                    userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)))
            counterService.increment("files.uploaded")
            gaugeService.submit("files.uploaded.lastBytes",file.size.toDouble())
            inMemoryMetricRepository.increment(Delta("files.uploaded.totalBytes",file.size.toDouble()))
            messagingTemplate.convertAndSend("/topic/newImage", file.originalFilename)
        }
    }

    @PreAuthorize("@imageRepository.findByName(#filename)?.owner?.username == authentication?.name or hasRole('ADMIN')")
    fun deleteImage(@Param("filename")filename: String) {
        var byName: Image = imageRepository.findByName(filename)
        imageRepository.delete(byName)
        Files.deleteIfExists(Paths.get(UPLOAD_ROOT, filename))
        messagingTemplate.convertAndSend("/topic/deleteImage", filename)
    }

    @Bean
    fun setup(): CommandLineRunner = CommandLineRunner {

        FileSystemUtils.deleteRecursively(File(UPLOAD_ROOT))
        Files.createDirectory(Paths.get(UPLOAD_ROOT))

        var tyron:User = userRepository.save(User("tyron","lannister", arrayOf("ROLE_ADMIN","ROLE_USER")))
        var john:User = userRepository.save(User("john","snow", arrayOf("ROLE_USER")))
        var khalisi:User = userRepository.save(User("khalisi","dragon", arrayOf("ROLE_USER")))

        FileCopyUtils.copy("Test File 1", FileWriter(UPLOAD_ROOT + "/test1"))
        imageRepository.save(Image("test1", tyron))

        FileCopyUtils.copy("Test File 2", FileWriter(UPLOAD_ROOT + "/test2"))
        imageRepository.save(Image("test2", tyron))

        FileCopyUtils.copy("Test File 3", FileWriter(UPLOAD_ROOT + "/test3"))
        imageRepository.save(Image("test3", khalisi))


    }
}