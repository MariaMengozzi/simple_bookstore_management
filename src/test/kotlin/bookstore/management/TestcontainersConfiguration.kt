package bookstore.management

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	fun mysqlContainer(): MySQLContainer<*> {
		val container = MySQLContainer(DockerImageName.parse("mysql:latest")).apply {
			withDatabaseName("bookstoretest")
			withUsername("root")
			withPassword("")
			start()
		}
		return container
	}

	companion object {
		private val mysqlContainer: MySQLContainer<*> = MySQLContainer(DockerImageName.parse("mysql:latest")).apply {
			withDatabaseName("bookstoretest")
			withUsername("root")
			withPassword("")
			start()
		}

		@JvmStatic
		@DynamicPropertySource
		fun registerMysqlProperties(registry: DynamicPropertyRegistry) {
			registry.add("spring.datasource.url") { mysqlContainer.jdbcUrl }
			registry.add("spring.datasource.username") { mysqlContainer.username }
			registry.add("spring.datasource.password") { mysqlContainer.password }
		}
	}
}
