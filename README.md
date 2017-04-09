
# EXAMPLE GROOVY


##### Arquitetura
Uma API principal utilizando  Spring Boot + Groovy; acesso ao banco com  utilizando Spring Data; conexão com a fila utilizando Spring AMQP; framework de testes utilizando Spring Test + Spock + Cobertura.
Para isso serviço de mensageira estamos utilizando o RABBITMQ
Para o serviço de banco de dados estamos utilizando MariaDB
Para execução em ambiente de desenvolvimento estamos mock ando o serviço externo
Todos os serviços; com exceção da aplicação, estão embarcados em container Docker

##### Caso de Uso
- o sistema recebe a requisição do usuário e valida os dados recebidos; caso os dados de entrada não sejam satisfatórios retorna uma mensagem de erro para o usuário
- salva num banco de solicitações;
envia para ser processado no serviço de SMS externo; e valida a resposta; atualiza as informações da mensagem e atualiza o banco de solicitações; retorna o status para o usuário;
- caso o serviço externo retorne um erro interno ou não esteja disponível, encaminha a mensagem para o serviço de filas; somente entram na fila as mensagem que não expiraram, ou seja, que possuem data de validade maior  que a data atual, e não foram processadas por indisponibilidade do serviço externo;
- o serviço de filas periodicamente disponibiliza as mensagens para serem processadas novamente;

## Execução

##### Pre Requisitos
  - DOCKER / DOCKER-COMPOSE
  - JDK 8 / GROOVY 2.4.7 / MAVEN 3.3.9

##### Execução via linha de comando
Na premissa que seu ambiente docker/docker-compose estão instalados, e você chegou na pasta inicial do projeto por terminal (linha de comando ), basta executar os seguintes comandos de inicialização:
Para garantir que não existem execuções dos containers
```sh
$ docker-compose down --remove-orphans
$ for i in `docker ps -a |grep example|awk '{print $1;}'`;do docker rmi $i -f;done
$ for i in `docker images |grep example|awk '{print $3;}'`;do docker rmi $i -f;done
```
Para executar os containers
```sh
$ docker-compose pull
$ docker-compose up -d --build
```
Para acompanhar o log
```sh
$ docker-compose logs --follow
```
Em outro terminal , navegue até a pasta da aplicação e execute o comando
```sh
$ mvn clean compile package exec:java
```

## Executando Testes
Abra o terminal, navegue até a pasta da aplicação e execute o comando
```sh
$ mvn clean cobertura:cobertura
```
O resultado estará disponível no local
```sh
target/site/index.html
```

##### Chamada via linha de comando
Abra o terminal e execute o comando
```sh
$ curl -i -X PUT -H "Content-Type: application/json" -d '{"to":"21999999999", "from":"21988888888", "body":"hello world"}' http://localhost:8080/sms
```
A resposta esperada será
```sh
HTTP/1.1 201
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 23 Feb 2017 15:25:21 GMT

{"id":50,"from":"21988888888","to":"21999999999","body":"hello world","created":1487863521062,"expire":1487863581063,"ok":true,"status":201}
```
Para simular erro, execute o comando
```sh
curl -i -X PUT -H "Content-Type: application/json" -d '{"to":"21999999999", "from":"21988888888", "body":"hello world", "expire":"1487704836178"}' http://localhost:8080/sms
```
A resposta de erro esperada será
```sh
HTTP/1.1 400
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 23 Feb 2017 17:02:15 GMT
Connection: close

{"id":null,"from":"21988888888","to":"21999999999","body":"hello world","created":1487869335782,"expire":1487704836178,"ok":false,"status":400}
```
