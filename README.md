# 재고 시스템 기반의 간단한 주문 API 구현
![Untitled (4)](https://github.com/kksjae-hyyejji/Payment-System/assets/87571953/986224b9-caf5-4af6-9f1f-1ef06c43b924)

# 목적

- 트랜잭션 레벨에 따른 데이터 정합성을 확인한다
    - `다수의 사용자가 동시에 한 상품에 접근했을 때 정확히 상품 개수만큼의 결제가 이루어지는가`
    - 데드락 상황을 유도하고 개선 방법에 대해 고민한다

# 필요한 테스트

- 원하는 상황을 연출하고 트랜잭션별 테스트를 진행하며, 데드락을 유도해야 하므로 `부하테스트` 로 선정하였다
- 부하 테스트 툴 : `Jmeter`
    - Java로 구성된 부하 테스트 툴로 무료 서비스이고, 러닝커브가 높지 않다는 판단 하에 선정

# 사전 준비 사항

- 재고 시스템 주문을 위해 샘플 상품의 재고를 10000개씩 저장
    - 샘플 상품 : apple, grapes, strawberry, banana
- 더미 유저의 잔고를 테스트에 문제 없을 정도의 금액으로 저장
    - 유저는 1억씩 가지고 있음.

# 테스트

## 1. 메서드에 아무런 제약을 걸지 않고 동시에 100개의 요청
<br>
    - MySQL 기본값을 따르므로, 트랜잭션 격리 레벨 수준이 REPEATABLE-READ로, Lock이 아닌 MVCC가 이용됨<br>
    - 데이터 정합성이 지켜지지 않음, 약 7초 소요
    <img width="863" alt="Untitled" src="https://github.com/kksjae-hyyejji/Payment-System/assets/87571953/07eb9bd4-6ac6-43c1-9b87-d3f29940fbea">
    
## 2.  Java 수준에서의 메소드 `synchronized` 키워드 추가 후 동시에 100개의 요청
<br>
    - 데이터 정합성이 올바르게 지켜짐, 약 9초 소요<br>
    - 서버가 여러개인 경우 사용할 수 없음.
    <img width="974" alt="Untitled (1)" src="https://github.com/kksjae-hyyejji/Payment-System/assets/87571953/6c38f989-1f01-4975-8daa-04dd8fba920f">

## 3. 트랜잭션 격리 레벨 수준을 `Serializable`로 상향 후 동시에 `1000개`의 요청

- `데드락 발생`

```java
LATEST DETECTED DEADLOCK
------------------------
2024-01-04 06:33:15 0x7f80400ce640
*** (1) TRANSACTION:
TRANSACTION 2070, ACTIVE 0 sec starting index read
mysql tables in use 1, locked 1
LOCK WAIT 7 lock struct(s), heap size 1128, 7 row lock(s), undo log entries 4
MariaDB thread id 35, OS thread handle 140188807128640, query id 18697 172.17.0.1 root Updating
update product set amount=99899,price=4000 where product_name='apple'
*** WAITING FOR THIS LOCK TO BE GRANTED:
RECORD LOCKS space id 9 page no 3 n bits 8 index PRIMARY of table `payment`.`product` trx id 2070 lock_mode X locks rec but not gap waiting
Record lock, heap no 2 PHYSICAL RECORD: n_fields 5; compact format; info bits 0
 0: len 5; hex 6170706c65; asc apple;;
 1: len 6; hex 000000000807; asc       ;;
 2: len 7; hex 71000000390150; asc q   9 P;;
 3: len 4; hex 8001863c; asc    <;;
 4: len 4; hex 80000fa0; asc     ;;

*** CONFLICTING WITH:
RECORD LOCKS space id 9 page no 3 n bits 8 index PRIMARY of table `payment`.`product` trx id 2067 lock mode S locks rec but not gap
Record lock, heap no 2 PHYSICAL RECORD: n_fields 5; compact format; info bits 0
 0: len 5; hex 6170706c65; asc apple;;
 1: len 6; hex 000000000807; asc       ;;
 2: len 7; hex 71000000390150; asc q   9 P;;
 3: len 4; hex 8001863c; asc    <;;
 4: len 4; hex 80000fa0; asc     ;;
```

- shared lock과 exlusive lock이 동시에 발생하여 서로 대기하는 상황 발생
- 위의 내용을 보면 transaction 2070번이 apple의 양을 update하려 X lock을 기다리고 있음.
- transaction 2067번이 동일한 apple record에 대해서 S lock을 보유하고 있는 상황.

## 4. (데드락 회피) `@Lock(value=LockModeType.PESSIMISTIC_WRITE)` 추가 후 동시에 `1000개`의 요청

- 엔티티에 대한 동시 접근을 처리하므로 상품 테이블의 데드락을 해결
- 낙관락과 비관락
    - 이미 충돌이 발생한 상황이므로, 충돌이 발생할 소지가 있는 것으로 판단해 충돌 상황에서 트랜잭션 자체가 롤백될 수 있는 비관락(PESSIMISTIC)을 택함
    - 타 트랜잭션이 진행중일 때에는 상품의 잔여 개수를 읽지 못하도록 `PESSIMISTIC_WRITE` 모드로 설정
        
        <img width="687" alt="Untitled" src="https://github.com/kksjae-hyyejji/Payment-System/assets/87571953/c9f8ee94-d400-47a3-b7d5-c9f6ec09201b">
