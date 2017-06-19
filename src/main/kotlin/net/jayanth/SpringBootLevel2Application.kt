package net.jayanth

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.SpringApplication
import org.springframework.boot.actuate.endpoint.MetricReaderPublicMetrics
import org.springframework.boot.actuate.endpoint.PublicMetrics
import org.springframework.boot.actuate.metrics.repository.InMemoryMetricRepository
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class SpringBootLevel2Application {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(SpringBootLevel2Application::class.java, *args)
        }
    }

    @Bean(name= arrayOf("appInMemoryMetricsRepository"))
    fun inMemoryMetricsRepository(): InMemoryMetricRepository {
        return  InMemoryMetricRepository()
    }

    @Bean
    fun publicMetrics(@Qualifier("appInMemoryMetricsRepository") repository: InMemoryMetricRepository ): PublicMetrics{
        return  MetricReaderPublicMetrics(repository)
    }
}