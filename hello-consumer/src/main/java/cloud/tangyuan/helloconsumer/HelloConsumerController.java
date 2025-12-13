package cloud.tangyuan.helloconsumer;

import .HelloFeignService;

@RestController
public class HelloConsumerController {
    @Autowired
    private HelloFeignService helloFeignService;

    @GetMapping(value="/enter/{username}")
    public String sayHello(@PathVariable String username){
        return helloFeignService.sayHello(username);
    }
}