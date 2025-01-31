echo "> ============ switch.sh 실행 (nignx 동적 프록시 설정) ==============" >> /home/ec2-user/deploy.log
echo "> 현재 구동중인 Port 확인" >> /home/ec2-user/deploy.log
CURRENT_PROFILE=$(curl -s https://feedb.store/nginx/profile)

# 쉬고 있는 set 찾기: set1이 사용중이면 set2가 쉬고 있고, 반대면 set1이 쉬고 있음
if [ $CURRENT_PROFILE == set1 ]
then
  IDLE_PORT=8082
elif [ $CURRENT_PROFILE == set2 ]
then
  IDLE_PORT=8081
else
  echo "> 일치하는 Profile이 없습니다. Profile: $CURRENT_PROFILE" >> /home/ec2-user/deploy.log
  echo "> 8081을 할당합니다." >> /home/ec2-user/deploy.log
  IDLE_PORT=8081
fi

echo "> 전환할 Port: $IDLE_PORT" >> /home/ec2-user/deploy.log
echo "> Port 전환" >> /home/ec2-user/deploy.log
echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" |sudo tee /etc/nginx/conf.d/service-url.inc

PROXY_PORT=$(curl -s https://feedb.store/nginx/profile)
echo "> Nginx Current Proxy Port: $PROXY_PORT" >> /home/ec2-user/deploy.log

echo "> Nginx Reload" >> /home/ec2-user/deploy.log
sudo service nginx reload
echo "> ============ switch.sh 종료 ==============" >> /home/ec2-user/deploy.log
