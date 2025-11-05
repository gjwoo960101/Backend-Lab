package study.demo.rewriteBatchedStatementsTest;

import jakarta.transaction.Transactional;
import net.datafaker.Faker;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import study.demo.RequestDto;

import java.util.*;


// MySql의 옵션중 rewriteBatchedStatements의 활성화 여부에 따른 성능차이 테스트
@RestController
public class optionTestController {

    private final JdbcTemplate jdbcTemplate;

    public optionTestController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping(value = "/insert-test")
    public ResponseEntity<Map<String,Object>> insertTest(@RequestBody RequestDto requestDto){
        long start = System.currentTimeMillis();
        int count = requestDto.getCount();
        String sql = "INSERT INTO dummy_table (id,name,address,book_content) VALUES (?,?,?,?)";
        List<Object[]> batch = new ArrayList<>();
        Faker faker = new Faker(new Locale("ko"));

        for(int i = 0; i< count; i++){
            UUID id = UUID.randomUUID();
            batch.add(new Object[]{
                    id.toString(),
                    faker.name().firstName() + faker.name().lastName(),
                    faker.address().fullAddress(),
                    faker.lorem().fixedString(1000)
            });
            if(batch.size() == 1000){
                jdbcTemplate.batchUpdate(sql,batch);
                batch.clear();
            }
        }

        if (!batch.isEmpty()) jdbcTemplate.batchUpdate(sql, batch);

        long end = System.currentTimeMillis();

        Map<String, Object> result = new HashMap<>();
        result.put("insertCount", count);
        result.put("elapsedTimeSec", (end - start) / 1000.0);
        return ResponseEntity.ok(result);

    }

}
