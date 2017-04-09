FROM rabbitmq:management
MAINTAINER "moalmeida" <moalmeida@koinosystems.com>

ADD rabbitmq_delayed_message_exchange-0.0.1.ez plugins/
RUN rabbitmq-plugins enable rabbitmq_delayed_message_exchange
