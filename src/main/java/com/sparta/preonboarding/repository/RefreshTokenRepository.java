package com.sparta.preonboarding.repository;

import com.sparta.preonboarding.entity.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {
    private final RedisTemplate redisTemplate;
    private final Logger log = LoggerFactory.getLogger(RefreshTokenRepository.class);

    @Autowired
    public RefreshTokenRepository(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void save(RefreshToken refreshToken){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();  //opsFor은 특정 컬렉션의 작업(operation)들을 호출할 수 있는 인터페이스를 반환. 이것은 string을 위한 것.

        if(!Objects.isNull(valueOperations.get(refreshToken.getUsername()))){    //만약 이미 그 username을 key로 한 refreshtoken이 있을 경우, 업데이트를 위해 삭제.
            redisTemplate.delete(refreshToken.getUsername());
            log.info("refreshToken Repository save -> update ");
        }
        valueOperations.set(refreshToken.getUsername(), refreshToken.getRefreshToken());  //redis에 데이터 저장.
        redisTemplate.expire(refreshToken.getUsername(), 60 * 60 * 24, TimeUnit.SECONDS);  //그 데이터 만료 시간 지정. 24시간.
    }

    public Optional<RefreshToken> findByUsername(String username){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String refreshToken = valueOperations.get(username);     //username을 key로 가진 데이터 찾음.

        if(refreshToken== null){
            return Optional.empty();
        }
        else{
            return Optional.of(new RefreshToken(username, refreshToken));
        }
    }

    public void delete(String username){
        redisTemplate.delete(username);
    }
}