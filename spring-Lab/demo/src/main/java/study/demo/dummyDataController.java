package study.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import net.datafaker.Faker;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
public class dummyDataController {

    private final  duumyRepositroy duumyRepositroy;
    private final JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;
    public static Faker faker;
    public dummyDataController(duumyRepositroy duumyRepositroy, JdbcTemplate jdbcTemplate) {
        this.duumyRepositroy = duumyRepositroy;
        this.jdbcTemplate = jdbcTemplate;
        faker = new Faker(new Locale("ko"));
    }

    @PostMapping(value = "/nonJpa")
    @Transactional
    public ResponseEntity<Map<String, Object>> nonUseJPA(@RequestBody RequestDto requestDto) {
        final int batchSize = 1000;
        int count = requestDto.getCount();
        final String sql = """
                    INSERT INTO dummy_table (id, name, address,book_content)
                    VALUES (?,?,?,?)
                """;

        long start = System.nanoTime();


        List<Object[]> batch = new ArrayList<>(batchSize);
        for (int i = 1; i <= count; i++) {
            UUID id = UUID.randomUUID();
            batch.add(new Object[]{
                    id.toString(),
                    faker.name().firstName() + faker.name().lastName(),
                    faker.address().fullAddress(),
                    faker.lorem().fixedString(1000)
            });
            if (i % batchSize == 0) {
                jdbcTemplate.batchUpdate(sql, batch);
                batch.clear();
            }
        }
            if (!batch.isEmpty()) {
                jdbcTemplate.batchUpdate(sql, batch);
            }

        long end = System.nanoTime();
        double ms = (end - start) / 1_000_000.0;
        double tps = (count / (ms / 1000.0));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("method", "JDBC batch");
        result.put("count", count);
        result.put("batchSize", batchSize);
        result.put("durationMs", ms);
        result.put("rowsPerSec", tps);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/jpa")
    @Transactional
    public ResponseEntity<Map<String, Object>> useJPA(@RequestBody RequestDto requestDto) {
        final int batchSize = 1000;
        int count = requestDto.getCount();
        long start = System.nanoTime();

        for (int i = 1; i <= requestDto.getCount(); i++) {
            dummy entity = new dummy(
                    /* id: JPA 안 쓸 거면 null이어도 되고, 여기서는 무시됨 */
                    null,
                    faker.name().firstName() + faker.name().lastName(),
                    faker.address().fullAddress(),
                    faker.lorem().fixedString(1000) // 길이 안전
            );
            em.persist(entity);

            if (i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }
        em.flush(); // 커밋 전 강제 flush

        long end = System.nanoTime();
        double ms = (end - start) / 1_000_000.0;
        double tps = (count / (ms / 1000.0));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("method", "JPA batch (EntityManager)");
        result.put("count", count);
        result.put("batchSize", batchSize);
        result.put("durationMs", ms);
        result.put("rowsPerSec", tps);

        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/read")
    public String readData (){
        return faker.lorem().paragraph(50);
    }

    @PostMapping(value = "/bodyTest")
    public int bodyTest(@RequestBody RequestDto dto){
        return dto.getCount();
    }

}
