package net.jayanth

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by jmp on 6/19/2017.
 */
@Component
class SpringBootLevel2HealthIndicator : HealthIndicator{

    override fun health(): Health {
      var responseCode: Int = -1
      var exceptionMessage : String? = null
      try {
          val connection = URL("http://github.com/repos-jmp").openConnection() as HttpURLConnection
          connection.requestMethod = "GET"
          connection.connect()
          responseCode = connection.responseCode
      } catch (e:IOException){
          exceptionMessage = e.message
      } finally {
          if(responseCode in 200..300) return Health.up().build()
          else if(exceptionMessage!= null) return Health.down().withDetail("Exception: ", exceptionMessage).build()
          else return Health.down().withDetail("Http Status Code: ", responseCode).build()
      }
    }
}