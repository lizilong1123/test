package com.leo.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import io.lettuce.core.ReadFrom;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.text.SimpleDateFormat;
import java.util.HashSet;

@Configuration
public class RedisConfiguration {

    /**
     *  配置redis序列化json
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    @Primary    //若有相同类型的Bean时，优先使用此注解标注的Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 为了开发方便,一般直接使用<String, Object>
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 配置具体的序列化方式
        // JSON解析任意对象
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域,field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        // 设置日期格式
        om.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // String的序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();


        //key采用String的序列化
        template.setKeySerializer(stringRedisSerializer);
        //hash的key也采用String的序列化
        template.setHashKeySerializer(stringRedisSerializer);
        //value的序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        //设置所有配置
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 配置读写分离
     * @param redisProperties
     * @return
     */
    @Bean
    public RedisConnectionFactory lettuceConnectionFactory(RedisProperties redisProperties) {
        // 配置哨兵节点以及主节点
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration(
                redisProperties.getSentinel().getMaster(), new HashSet<>(redisProperties.getSentinel().getNodes())
        );

        // 配置读写分离
        LettucePoolingClientConfiguration lettuceClientConfiguration = LettucePoolingClientConfiguration.builder()
                // 读写分离,这里的ReadFrom是配置Redis的读取策略,是一个枚举,包括下面选择
                // MASTER   仅读取主节点
                // MASTER_PREFERRED   优先读取主节点,如果主节点不可用,则读取从节点
                // REPLICA_PREFERRED   优先读取从节点,如果从节点不可用,则读取主节点
                // REPLICA   仅读取从节点
                // NEAREST   从最近节点读取
                // ANY   从任意一个从节点读取
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();

        return new LettuceConnectionFactory(redisSentinelConfiguration, lettuceClientConfiguration);
    }

}
