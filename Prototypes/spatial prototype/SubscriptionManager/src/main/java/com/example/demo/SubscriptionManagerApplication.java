package com.example.demo;


import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.databind.Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.geo.GeoJsonModule;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class SubscriptionManagerApplication {

	@LoadBalanced
	@Bean
	RestTemplate restTemplate(){
		return new RestTemplate();
	}
	@Bean
	public JtsModule jtsModule()
	{
		return new JtsModule();
	}
	@Bean
	public Module registerGeoJsonModule(){
		return new GeoJsonModule();
	}

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionManagerApplication.class, args);
	}

}
