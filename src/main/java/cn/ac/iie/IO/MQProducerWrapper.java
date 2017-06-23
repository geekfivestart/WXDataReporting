package cn.ac.iie.IO;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public class MQProducerWrapper {
	private static final Logger LOG=LoggerFactory.getLogger(MQProducerWrapper.class);
	
    private DefaultMQProducer producer = null;
    private DefaultSendCallBack sendCallBack=new DefaultSendCallBack();
    /**
     * @param mqAddress node ip of mq cluster
     * @param mqPort port of mq service
     * @param producerGroup producer group
     */
    public MQProducerWrapper(String mqAddress,int mqPort,String producerGroup){
    	producer = new DefaultMQProducer(producerGroup);  
    	producer.setNamesrvAddr(mqAddress+":"+mqPort);
    }
    
    public MQProducerWrapper(String nameAddr,String producerGroup){
    	producer=new DefaultMQProducer(producerGroup);
    	producer.setNamesrvAddr(nameAddr);
    }
    
    public void startProducer(){
    	try {
			producer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
    }
    
    public void stopProducer(){
    	producer.shutdown();
    }
    
    public void sendMess(String topic, String mess){
    	try {
    		Message msg = new Message(topic,mess.getBytes("UTF-8"));
			producer.send(msg);
		} catch (MQClientException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (RemotingException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (MQBrokerException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
    }
    
    public void sendMessAsync(String topic, String mess){
    	try {
    		Message msg = new Message(topic,mess.getBytes("UTF-8"));
			producer.send(msg,sendCallBack);
		} catch (MQClientException e) {
			LOG.error(e.getMessage());
		} catch (RemotingException e) {
			LOG.error(e.getMessage());
		} catch (InterruptedException e) {
			LOG.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage());
		}
    }
    
    public void sendMess(String topic, List<String> messes){
    	for(String st:messes){
    		sendMess(topic, st);
    	}
    }
    
    class DefaultSendCallBack implements SendCallback{

		@Override
		public void onSuccess(SendResult sendResult) {
		}

		public void onException(Throwable e) {
			LOG.error(e.getMessage());
		}
    	
    }
}