package study.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    dummyDataController dummyDataController;

    public StartupRunner(study.demo.dummyDataController dummyDataController) {
        this.dummyDataController = dummyDataController;
    }

    @Override
    public void run(String... args) throws Exception {
        //dummyDataController.createDummyData();
    }
}
