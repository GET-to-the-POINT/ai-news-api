package getToThePoint.unicatApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import getToThePoint.unicatApi.common.propertie.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class UnicatApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnicatApiApplication.class, args);
	}

}
