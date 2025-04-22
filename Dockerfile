# 톰캣 10.1 버전을 베이스 이미지로 사용
FROM tomcat:10.1-jdk17

# 작업 디렉토리 설정
WORKDIR /usr/local/tomcat

# 기존 웹앱 삭제
RUN rm -rf /usr/local/tomcat/webapps/*

# context.xml 복사
COPY src/main/webapp/META-INF/context.xml /usr/local/tomcat/conf/context.xml

# WAR 파일 복사
COPY build/libs/*.war /usr/local/tomcat/webapps/ROOT.war

# 포트 설정
EXPOSE 8085

# 톰캣 실행
CMD ["catalina.sh", "run"] 