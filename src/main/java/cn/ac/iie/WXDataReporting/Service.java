package cn.ac.iie.WXDataReporting;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.ac.iie.Entity.SampleEntity;
import cn.ac.iie.IO.MQProducerWrapper;

public class Service {

	private final static String MQNameAddr="10.144.32.21:9876;10.144.32.22:9876;10.144.32.23:9876";
	private final static String ProducerGroup="wxdatareport_pg";
	private final static String TOPIC="wxdatareport_tp";
	private final static ObjectMapper om=new ObjectMapper();
	public static void main(String[] args) {
		MQProducerWrapper mqp=new MQProducerWrapper(MQNameAddr,ProducerGroup);
		mqp.startProducer();
		SampleEntity samp=new SampleEntity();
		samp.setM_chat_room(10000000L);
		samp.setM_publish_time(1490000000L);
		samp.setU_ch_id(123456789L);
		samp.setM_content("hello world");
		try {
			String obString=om.writeValueAsString(samp);
			mqp.sendMess(TOPIC, obString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		mqp.stopProducer();
	}
}
