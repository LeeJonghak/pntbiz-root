### Activemq config properties (hanwhateckwin) ###

- brokerURL : ${activemq.url} // queue를 전송할 URL
- userName : ${activemq.username} // 계정이 있는 경우 설정
- password : ${activemq.password} // 계정에 대한 비밀번호가 있는 경우 설정
- destination : ${activemq.destination} // queue를 전송할 목적지
- session cache size : ${activemq.session.cache.size}

### config-properties.xml ###

<entry key="activemq.url">tcp://api.pntbiz.com:61616</entry>
<entry key="activemq.username">admin</entry>
<entry key="activemq.password"></entry>
<entry key="activemq.destination">pntbiz</entry>
<entry key="activemq.session.cache.size">300</entry>
