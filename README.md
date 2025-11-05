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


durationMS : 데이터생성 + 배치준비 + DB전송
rowsPerSec: 초당 처리 가능한 행 수

{
    "method": "JDBC batch",
    "count": 500,
    "batchSize": 1000,
    "durationMs": 730.1209,
    "rowsPerSec": 684.8180897163744
}


{
    "method": "JPA batch (EntityManager)",
    "count": 500,
    "batchSize": 1000,
    "durationMs": 669.4309,
    "rowsPerSec": 746.90307842079
}


{
    "method": "JDBC batch",
    "count": 10000,
    "batchSize": 1000,
    "durationMs": 3256.338401,
    "rowsPerSec": 3070.933904452027
}

{
    "method": "JPA batch (EntityManager)",
    "count": 10000,
    "batchSize": 1000,
    "durationMs": 2629.2451,
    "rowsPerSec": 3803.373067044986
}