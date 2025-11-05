# 백엔드 학습용 레포지토리


- Spring boot
- mysql
- postgresql


### IDE
- Spring Boot
    > InteliJ
- DB
    > cursor(Devcontainer로 실행예정)

### devcontainer
- devcontainer.json
    - 작성시 server 속성에 명시하는 서비스는 vscode가 접속할 대상의 컨테이너를 지정한다.


### Bulk Insert
- JPA를 사용하지않음
    - 엔티티를 영속성 컨텍스트에 저장 > 스냅샷 생성 > flush 시점에 SQL로 변환하여 Insert > PK가 IDENTITY면 LAST_INSERT_ID() 호출 > 트랜잭션 커밋 시 Clear 
    위와 같은 절차를 거치기때문에 대량적제용도로는 비효율적이다.


### Mysql
- rewriteBatchedStatements란?
    - MySQL JDBC 드라이버가 Batch Insert를 최적화해주는 설정이다.
    ``` java
        PreparedStatement ps = conn.prepareStatement("INSERT INTO dummy (name) VALUES (?)");
        for (int i = 0; i < 1000; i++) {
            ps.setString(1, "test" + i);
            ps.addBatch();
        }
        ps.executeBatch();

        rewriteBatchedStatements을 true로 하게되면 1000번의 insert가 아닌 SQL 병합을 사용하여
        한번의 insert로 해결한다.
    ```

 - 테스트 결과
    - 활성화
    ```json
    {
        "elapsedTimeSec": 1.283,
        "insertCount": 2000
    }
    ```
    - 비 활성화
    ```json
    {
        "elapsedTimeSec": 17.627,
        "insertCount": 2000
    }
    ```