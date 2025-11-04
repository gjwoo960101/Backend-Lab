package study.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface duumyRepositroy extends JpaRepository<dummy,Long> {
}
