package com.kotekka.app.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.kotekka.app.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.kotekka.app.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.kotekka.app.domain.User.class.getName());
            createCache(cm, com.kotekka.app.domain.Authority.class.getName());
            createCache(cm, com.kotekka.app.domain.User.class.getName() + ".authorities");
            createCache(cm, com.kotekka.app.domain.Beneficiary.class.getName());
            createCache(cm, com.kotekka.app.domain.CacheInfo.class.getName());
            createCache(cm, com.kotekka.app.domain.Card.class.getName());
            createCache(cm, com.kotekka.app.domain.Cin.class.getName());
            createCache(cm, com.kotekka.app.domain.Device.class.getName());
            createCache(cm, com.kotekka.app.domain.FailedAttempt.class.getName());
            createCache(cm, com.kotekka.app.domain.FailedAttemptHistory.class.getName());
            createCache(cm, com.kotekka.app.domain.FeatureFlag.class.getName());
            createCache(cm, com.kotekka.app.domain.Image.class.getName());
            createCache(cm, com.kotekka.app.domain.IncomingCall.class.getName());
            createCache(cm, com.kotekka.app.domain.MoneyRequest.class.getName());
            createCache(cm, com.kotekka.app.domain.Notification.class.getName());
            createCache(cm, com.kotekka.app.domain.OneTimePassword.class.getName());
            createCache(cm, com.kotekka.app.domain.Organisation.class.getName());
            createCache(cm, com.kotekka.app.domain.PartnerCall.class.getName());
            createCache(cm, com.kotekka.app.domain.Recipient.class.getName());
            createCache(cm, com.kotekka.app.domain.ReferalInfo.class.getName());
            createCache(cm, com.kotekka.app.domain.ServiceClient.class.getName());
            createCache(cm, com.kotekka.app.domain.Transaction.class.getName());
            createCache(cm, com.kotekka.app.domain.UserAccess.class.getName());
            createCache(cm, com.kotekka.app.domain.UserAffiliation.class.getName());
            createCache(cm, com.kotekka.app.domain.UserPreference.class.getName());
            createCache(cm, com.kotekka.app.domain.Wallet.class.getName());
            createCache(cm, com.kotekka.app.domain.WalletHolder.class.getName());
            createCache(cm, com.kotekka.app.domain.Product.class.getName());
            createCache(cm, com.kotekka.app.domain.Product.class.getName() + ".collections");
            createCache(cm, com.kotekka.app.domain.Order.class.getName());
            createCache(cm, com.kotekka.app.domain.Discount.class.getName());
            createCache(cm, com.kotekka.app.domain.Cart.class.getName());
            createCache(cm, com.kotekka.app.domain.CartItem.class.getName());
            createCache(cm, com.kotekka.app.domain.AuditLog.class.getName());
            createCache(cm, com.kotekka.app.domain.Review.class.getName());
            createCache(cm, com.kotekka.app.domain.Category.class.getName());
            createCache(cm, com.kotekka.app.domain.Collection.class.getName());
            createCache(cm, com.kotekka.app.domain.Collection.class.getName() + ".products");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
