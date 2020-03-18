import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {JdbcTemplateAutoConfiguration.class})
@ComponentScan(basePackages = "com.yaypay")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
