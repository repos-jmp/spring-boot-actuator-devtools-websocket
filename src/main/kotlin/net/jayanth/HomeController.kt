package net.jayanth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.IOException



/**
 * Created by jmp on 6/17/2017.
 */
@Controller
class HomeController @Autowired constructor(val imageService: ImageService) {
    companion object {
        const val BASE_PATH: String = "/images"
        const val FILE_NAME: String = "{filename:.+}"
    }

    @RequestMapping(value = "/")
    fun index(model: Model, pageable: Pageable): String {
        val page: Page<Image> = imageService.findPage(pageable)
        model.addAttribute("page", page)
        if (page.hasPrevious()) model.addAttribute("prev", pageable.previousOrFirst())
        if (page.hasNext()) model.addAttribute("next", pageable.next())
        return "index"
    }

    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "$BASE_PATH/$FILE_NAME/raw")
    @ResponseBody
    fun oneRawImage(@PathVariable filename: String): ResponseEntity<Any> {
        try {
            var file: Resource = imageService.findOneImage(filename)
            return ResponseEntity.ok().contentLength(file.contentLength()).contentType(MediaType.IMAGE_JPEG).body(InputStreamResource(file.inputStream))
        } catch (e: IOException) {
            return ResponseEntity.badRequest().body("Could not find $filename => " + e.message)
        }
    }

    @RequestMapping(method = arrayOf(RequestMethod.POST), value = BASE_PATH)
    fun createFile(@RequestParam("file") file: MultipartFile, redirectAttributes: RedirectAttributes): String {
        try {
            imageService.createImage(file)
            redirectAttributes.addFlashAttribute("flash.message", "Successfully uploaded image : ${file.originalFilename}")
        } catch (e: IOException) {
            redirectAttributes.addFlashAttribute("flash.message", "Failed to upload : ${file.originalFilename} \n" + e.message)
        }
        return "redirect:/"
    }

    @RequestMapping(method = arrayOf(RequestMethod.DELETE), value = "$BASE_PATH/$FILE_NAME")
    fun deleteFile(@PathVariable filename: String, redirectAttributes: RedirectAttributes): String {
        try {
            imageService.deleteImage(filename)
            redirectAttributes.addFlashAttribute("flash.message", "Successfully deleted - " + filename)
        } catch (e: Exception) {
            when (e) {
                is IOException, is RuntimeException -> {
                    redirectAttributes.addFlashAttribute("flash.message", "Failed to delete => " + e.message)
                }
            }

        }
        return "redirect:/"
    }
}