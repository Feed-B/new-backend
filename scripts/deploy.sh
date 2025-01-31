echo "> ============ deploy.sh 실행($(date)) =============" >> /home/ec2-user/deploy.log
BASE_PATH=/home/ec2-user/FeedB-project
BUILD_PATH=$(ls $BASE_PATH/build/libs/*.jar | grep -i 'SNAPSHOT.jar$')
JAR_NAME=$(basename $BUILD_PATH)
sudo cp /home/ec2-user/FeedB-project/.env / #.env 파일 ec2 절대경로 복사

echo "> build 파일명: $JAR_NAME" >> /home/ec2-user/deploy.log #deploy.log에 기록 남기기

echo "> build 파일 복사" >> /home/ec2-user/deploy.log
DEPLOY_PATH=$BASE_PATH/jar/
cp $BUILD_PATH $DEPLOY_PATH

echo "> 현재 구동중인 Set 확인" >> /home/ec2-user/deploy.log
CURRENT_PROFILE=$(curl -s https://feedb.store/nginx/profile)
echo "> $CURRENT_PROFILE" >> /home/ec2-user/deploy.log

# 쉬고 있는 set 찾기: set1이 사용중이면 set2가 쉬고 있고 , 반대면 set1이 쉬고 있음
if [ $CURRENT_PROFILE == set1 ]
then
  IDLE_PROFILE=set2
  IDLE_PORT=8082
elif [ $CURRENT_PROFILE == set2 ]
then
  IDLE_PROFILE=set1
  IDLE_PORT=8081
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE" >> /home/ec2-user/deploy.log
  echo "> set1을 할당합니다. IDLE_PROFILE: set1" >> /home/ec2-user/deploy.log
  IDLE_PROFILE=set1
  IDLE_PORT=8081
fi

echo "> application.jar 교체" >> /home/ec2-user/deploy.log
IDLE_APPLICATION=$IDLE_PROFILE-FeedB.jar
IDLE_APPLICATION_PATH=$DEPLOY_PATH$IDLE_APPLICATION
ln -Tfs $DEPLOY_PATH$JAR_NAME $IDLE_APPLICATION_PATH

echo "> $IDLE_PROFILE 에서 구동중인 애플리케이션 Pid 확인" >> /home/ec2-user/deploy.log
IDLE_PID=$(pgrep -f $IDLE_APPLICATION)

if [ -z $IDLE_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/deploy.log
else
  echo "> kill -15 $IDLE_PID" >> /home/ec2-user/deploy.log
  kill -15 $IDLE_PID
  sleep 10
fi

echo "> $IDLE_PROFILE 배포" >> /home/ec2-user/deploy.log
nohup java -jar -Dspring.profiles.active=$IDLE_PROFILE $IDLE_APPLICATION_PATH >> /home/ec2-user/FeedB.log 2>>/home/ec2-user/FeedB_err.log &

echo "> $IDLE_PROFILE 10초 후 HEALTH check 시작" >> /home/ec2-user/deploy.log
echo "> curl -s http://localhost:$IDLE_PORT/actuator/health " >> /home/ec2-user/deploy.log
sleep 10

for retry_count in {1..10}
do
  response=$(curl -s http://localhost:$IDLE_PORT/actuator/health)
  up_count=$(echo $response | grep 'UP' | wc -l)

  if [ $up_count -ge 1 ] #up_count가 1이상인지
  then
    echo "> Health check 성공" >> /home/ec2-user/deploy.log
    break
  else
    echo "> Health check의 응답을 알 수 없거나 혹은 status가 UP이 아닙니다." >> /home/ec2-user/deploy.log
    echo "> Health check: ${response}" >> /home/ec2-user/deploy.log
  fi

  if [ $retry_count -eq 10 ]
  then
    echo "> Health check 실패. " >> /home/ec2-user/deploy.log
    echo "> Nginx에 연결하지 않고 배포를 종료합니다." >> /home/ec2-user/deploy.log
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도.." >> /home/ec2-user/deploy.log
  sleep 10
done


echo "> 스위칭" >> /home/ec2-user/deploy.log
sleep 10
sudo chmod +x $BASE_PATH/scripts/switch.sh
echo "> ============ deploy.sh 종료($(date)) =============" >> /home/ec2-user/deploy.log
$BASE_PATH/scripts/switch.sh
