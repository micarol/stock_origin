package com.micarol.stock.service.rabbitmq;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.micarol.stock.util.Loggers;


/**
 * 消息队列服务
 */
@Service
public class RabbitMQService {

//	protected final static Logger RABBIT_MQ_LOGGER = LoggerFactory.getLogger(RabbitMQService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 消息进入队列
     *
     * @param queueName 队列标识
     * @param message   消息内容
     */
    public void putMessage(String queueName, String message) throws AmqpException {
//		rabbitTemplate.setQueue(queueName);
//		rabbitTemplate.setRoutingKey(queueName);
//		rabbitTemplate.convertAndSend(message);
        rabbitTemplate.convertAndSend(queueName, message);
        Loggers.RUNNING_LOG.info("Put Message: queue={}, message={}", queueName, message);
    }

    public void putMessage(Queue queue, String message) {
        this.putMessage(queue.getName(), message);
    }

    /**
     * 取出队列消息
     *
     * @param queueName 队列标识
     */
    public String getMessage(String queueName) throws AmqpException {
//		rabbitTemplate.setQueue(queueName);
//		rabbitTemplate.setRoutingKey(queueName);
//		Object message = rabbitTemplate.receiveAndConvert();
        Object message = rabbitTemplate.receiveAndConvert(queueName);
        if (null != message) {
            Loggers.RUNNING_LOG.info("Get Message: queue={}, message={}", queueName, message);
            String resultMsg = "";
            if (message instanceof String) {
                resultMsg = message.toString();
            } else {
                byte[] msgs = (byte[]) message;
                resultMsg = new String(msgs);
            }

            return resultMsg;
        }
        return "";
    }

    public String getMessage(Queue queue) {
        return this.getMessage(queue.getName());
    }

}
