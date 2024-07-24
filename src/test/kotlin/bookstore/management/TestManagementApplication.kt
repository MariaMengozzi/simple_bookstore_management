package bookstore.management

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<ManagementApplication>().with(TestcontainersConfiguration::class).run(*args)
}
