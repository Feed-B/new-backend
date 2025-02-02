#!/bin/bash

echo "> ============ deploy.sh 실행 ($(date)) =============" >> /home/ec2-user/deploy.log
BASE_PATH=/home/ec2-user/FeedB-project

# 🔹 JAR 파일 찾기
BUILD_PATH=$(ls -t $BASE_PATH/build/libs/*.jar | grep -v 'plain.jar' | head -n 1)
JAR_NAME=$(basename $BUILD_PATH)

echo "> 빌드된 JAR 파일명: $JAR_NAME" >> /home/ec2-user/deploy.log

# 🔹 .env 파일 복사 (필요하면 유지)
sudo cp /home/ec2-user/FeedB-project/.env / 

# 🔹 기존 실행 중인 애플리케이션 종료
echo "> 실행 중인 애플리케이션 종료" >> /home/ec2-user/deploy.log
EXISTING_PID=$(pgrep -f java)

if [ -n "$EXISTING_PID" ]; then
  echo "> kill -15 $EXISTING_PID" >> /home/ec2-user/deploy.log
  kill -15 "$EXISTING_PID"
  sleep 5
else
  echo "> 실행 중인 애플리케이션이 없습니다." >> /home/ec2-user/deploy.log
fi

# 🔹 새 애플리케이션 실행 (포트 8080)
echo "> 애플리케이션 실행 (포트 8080)" >> /home/ec2-user/deploy.log
nohup java -jar -Dserver.port=8080 -Dspring.profiles.active=prod $BUILD_PATH >> /home/ec2-user/FeedB.log 2>> /home/ec2-user/FeedB_err.log &

# 🔹 Health Check
echo "> 8080 포트에서 HEALTH check 시작" >> /home/ec2-user/deploy.log
sleep 10

for retry_count in {1..10}
do
  response=$(curl -s http://localhost:8080/actuator/health)
  up_count=$(echo "$response" | grep 'UP' | wc -l)

  if [ "$up_count" -ge 1 ]; then
    echo "> Health check 성공" >> /home/ec2-user/deploy.log
    break
  else
    echo "> Health check 실패 (응답 없음 또는 status가 UP이 아님)" >> /home/ec2-user/deploy.log
    echo "> Health check 응답: ${response}" >> /home/ec2-user/deploy.log
  fi

  if [ "$retry_count" -eq 10 ]; then
    echo "> Health check 실패. 배포 종료" >> /home/ec2-user/deploy.log
    exit 1
  fi

  echo "> Health check 재시도.." >> /home/ec2-user/deploy.log
  sleep 10
done

echo "> ============ 배포 완료 ($(date)) =============" >> /home/ec2-user/deploy.log
