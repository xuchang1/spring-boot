package smoketest.tomcat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:configA.yml")
public class ConfigA {
}
