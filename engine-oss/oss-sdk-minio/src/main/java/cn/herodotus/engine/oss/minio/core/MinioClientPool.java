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

package cn.herodotus.engine.oss.minio.core;


import cn.herodotus.engine.oss.minio.properties.MinioProperties;
import io.minio.MinioClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * <p>Description: Minio 客户端连接池 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/11/8 10:54
 */
public class MinioClientPool {

    private final GenericObjectPool<MinioClient> minioClientPool;

    public MinioClientPool(MinioProperties minioProperties) {

        MinioClientPoolFactory factory = new MinioClientPoolFactory(minioProperties);

        GenericObjectPoolConfig<MinioClient> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(minioProperties.getPool().getMaxTotal());
        config.setMaxIdle(minioProperties.getPool().getMaxIdle());
        config.setMinIdle(minioProperties.getPool().getMinIdle());
        config.setMaxWaitMillis(minioProperties.getPool().getMaxWait().toMillis());
        config.setBlockWhenExhausted(minioProperties.getPool().isBlockWhenExhausted());
        minioClientPool = new GenericObjectPool<>(factory, config);
    }

    public GenericObjectPool<MinioClient> getMinioClientPool() {
        return minioClientPool;
    }

    public static class MinioClientPoolFactory extends BasePooledObjectFactory<MinioClient> {

        private final MinioProperties minioProperties;

        public MinioClientPoolFactory(MinioProperties minioProperties) {
            this.minioProperties = minioProperties;
        }

        @Override
        public MinioClient create() throws Exception {
            return MinioClient.builder().endpoint(minioProperties.getEndpoint()).credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey()).build();
        }

        @Override
        public PooledObject<MinioClient> makeObject() throws Exception {
            return super.makeObject();
        }

        @Override
        public PooledObject<MinioClient> wrap(MinioClient minioClient) {
            return new DefaultPooledObject<>(minioClient);
        }
    }
}
