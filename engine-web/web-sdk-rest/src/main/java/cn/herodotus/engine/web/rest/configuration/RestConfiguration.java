/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.engine.web.rest.configuration;

import cn.herodotus.engine.assistant.core.annotation.ConditionalOnSwaggerEnabled;
import cn.herodotus.engine.web.core.definition.OpenApiServerResolver;
import cn.herodotus.engine.web.core.properties.PlatformProperties;
import cn.herodotus.engine.web.core.support.ContextHolder;
import cn.herodotus.engine.web.rest.processor.DefaultOpenApiServerResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;

/**
 * <p>Description: 服务信息配置类 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/6/13 13:40
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(PlatformProperties.class)
@Import({
        JacksonConfiguration.class,
        RestTemplateConfiguration.class
})
public class RestConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RestConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [Engine Web Rest] Auto Configure.");
    }

    @Bean
    @ConditionalOnMissingBean
    public ContextHolder contextHolder(PlatformProperties platformProperties) {
        ContextHolder contextHolder = new ContextHolder(platformProperties);
        log.trace("[Herodotus] |- Bean [Context Holder] Auto Configure.");
        return contextHolder;
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenApiServerResolver openApiServerResolver(ContextHolder contextHolder) {
        DefaultOpenApiServerResolver defaultOpenApiServerResolver = new DefaultOpenApiServerResolver(contextHolder);
        log.trace("[Herodotus] |- Bean [Open Api Server Resolver] Auto Configure.");
        return defaultOpenApiServerResolver;
    }

    /**
     * 为了方便控制注入的顺序
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnSwaggerEnabled
    @ConditionalOnBean(OpenApiServerResolver.class)
    @Import(OpenApiConfiguration.class)
    static class OpenApiInit {

    }
}
