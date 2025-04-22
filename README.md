# LnR 게시판 프로젝트

JSP와 MySQL을 사용한 게시판 프로젝트입니다. Docker를 사용하여 쉽게 실행할 수 있습니다.



## 실행 방법

1. 프로젝트 클론:
```bash
git clone [repository-url]
cd LnR
```

2. 최초 실행 시 (도커 이미지 빌드 필요):
```bash
docker-compose up --build
```

3. 이후 실행 시 (이미 빌드된 이미지가 있는 경우):
```bash
docker-compose up
```

4. 웹 브라우저에서 접속:
- http://localhost:8085

## 도커 컨테이너 중지

```bash
docker-compose down
```

## 주의사항

1. 처음 실행 시 MySQL 컨테이너가 초기화되는 동안 잠시 대기가 필요할 수 있습니다.
2. 이미 8085 포트를 사용 중인 경우 docker-compose.yml 파일에서 포트를 변경해주세요.
3. 기존 데이터를 초기화하려면:
```bash
docker-compose down -v
docker-compose up --build
```

