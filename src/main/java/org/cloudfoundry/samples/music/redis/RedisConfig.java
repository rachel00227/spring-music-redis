package org.cloudfoundry.samples.music.redis;

import org.cloudfoundry.samples.music.domain.Album;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig extends AbstractCloudConfig{
	
	/* for local
	@Bean
	JedisConnectionFactory jedisConnectionFactory(){
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		jedisConFactory.setHostName("localhost");
		jedisConFactory.setPort(6379);
		
		return jedisConFactory;
	}*/
	/**
	Redis를 Cache로 사용할 때의 Redisconfig입니다.
	**/
	@Bean
    public RedisConnectionFactory redisConnection() {
        return connectionFactory().redisConnectionFactory();
    }
	
	@Bean
	public RedisTemplate<String, Album> redisTemplate(){
		RedisTemplate<String, Album> template = new RedisTemplate<String,Album>();
		template.setConnectionFactory(redisConnection());
		
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        RedisSerializer<Album> albumSerializer = new Jackson2JsonRedisSerializer<>(Album.class);

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(albumSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(albumSerializer);
		
		return template;
	}
}
