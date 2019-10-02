package com.witcurve.config;

import io.github.jhipster.config.JHipsterProperties;
import io.github.jhipster.config.jcache.BeanClassLoaderAwareJCacheRegionFactory;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        BeanClassLoaderAwareJCacheRegionFactory.setBeanClassLoader(this.getClass().getClassLoader());
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.witcurve.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.witcurve.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.witcurve.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.witcurve.domain.Event.class.getName(), jcacheConfiguration);
            cm.createCache(com.witcurve.domain.Keyword.class.getName(), jcacheConfiguration);
            cm.createCache(com.witcurve.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.witcurve.domain.Permission.class.getName(), jcacheConfiguration);
            cm.createCache(com.witcurve.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.witcurve.domain.Authority.class.getName() + ".permissions", jcacheConfiguration);
            cm.createCache(com.witcurve.domain.Event.class.getName() + ".keywords", jcacheConfiguration);
            cm.createCache(com.witcurve.domain.ReportCardDesign.class.getName() + ".courses", jcacheConfiguration);
            cm.createCache(com.witcurve.domain.ReportCard.class.getName() + ".scholasticCourses", jcacheConfiguration);
            cm.createCache(com.witcurve.domain.ReportCard.class.getName() + ".nonScholasticCourses", jcacheConfiguration);
            cm.createCache(com.witcurve.domain.ReportCard.class.getName() + ".nonScholasticRcds", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
