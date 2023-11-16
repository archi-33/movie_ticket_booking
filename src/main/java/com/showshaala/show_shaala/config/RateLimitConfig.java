package com.showshaala.show_shaala.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimitConfig {
  @Bean
  public Bucket createBucket() {
    // Create a bucket with a capacity of 100 tokens and refill rate of 100 tokens per minute
    Refill refill = Refill.of(10, Duration.ofMinutes(20));
    Bandwidth limit = Bandwidth.classic(10, refill);
    return Bucket4j.builder().addLimit(limit).build();
  }

}
