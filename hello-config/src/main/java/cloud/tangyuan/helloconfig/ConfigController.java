package cloud.tangyuan.helloconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {
    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @GetMapping("/config01")
    public String getConfig01(){
        String result = "db.username=%s, db.password=%s".formatted(username, password);
        return result;
    }

}
