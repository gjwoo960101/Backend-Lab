package study.demo;

import net.datafaker.Faker;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class dummyDataController {

    private final  duumyRepositroy duumyRepositroy;

    public dummyDataController(study.demo.duumyRepositroy duumyRepositroy) {
        this.duumyRepositroy = duumyRepositroy;
    }

    public void createDummyData(){
        Faker faker = new Faker(new Locale("ko"));
        int fakeDataCount = 10000;
        String name = faker.name().firstName()+faker.name().lastName();
        String address = faker.address().fullAddress();

        List<dummy> dummys = IntStream.range(0,fakeDataCount)
                .mapToObj(i -> new dummy(
                        null,
                        faker.name().firstName()+faker.name().lastName(),
                        faker.address().fullAddress()
                ))
                .collect(Collectors.toList());

        duumyRepositroy.saveAll(dummys);
    }

}
