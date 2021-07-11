# [Spring-Boot2-Data](https://github.com/happyflyer/Spring-Boot2-Data)

- [SpringBoot2 零基础入门](https://www.bilibili.com/video/BV19K4y1L7MT)
- [SpringBoot2 核心技术与响应式编程](https://www.yuque.com/atguigu/springboot)

## 1. SpringBoot2 核心技术

- SpringBoot2 基础入门
  - Spring 能做什么
  - 什么是 SpringBoot
  - 快速体验 SpringBoot
  - 自动配置原理入门
- SpringBoot2 核心功能
  - 配置文件
  - web 开发
  - 数据访问
  - JUnit5 单元测试
  - Actutor 生产指标监控
  - SpringBoot 核心原理解析
- SpringBoot2 场景整合
  - 虚拟化技术
  - 安全控制
  - 缓存技术
  - 消息中间件
  - 分布式入门
  - ...

## 2. 数据源自动配置

### 2.1. 导入 jdbc 场景

- 项目的依赖

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>
```

- `spring-boot-starter-data-jdbc` 的依赖

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
    <version>2.3.4.RELEASE</version>
    <scope>compile</scope>
  </dependency>
  <dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-jdbc</artifactId>
    <version>2.0.4.RELEASE</version>
    <scope>compile</scope>
  </dependency>
</dependencies>
```

- `spring-boot-starter-jdbc` 的依赖

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>2.3.4.RELEASE</version>
    <scope>compile</scope>
  </dependency>
  <dependency>
    <groupId>com.zaxxer</groupId>
    <artifactId>HikariCP</artifactId>
    <version>3.4.5</version>
    <scope>compile</scope>
  </dependency>
  <dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.2.9.RELEASE</version>
    <scope>compile</scope>
  </dependency>
</dependencies>
```

### 2.2. 导入 mysql 驱动

```xml
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
</dependency>
```

- 注意：数据库版本和驱动版本对应

```xml
<properties>
  <mysql.version>8.0.22</mysql.version>
</properties>
```

### 2.3. 自动配置原理

- `package org.springframework.boot.autoconfigure.jdbc;`
  - `DataSourceAutoConfiguration` 数据源自动配置
    - 修改数据源相关的配置 `spring.datasource`
    - 数据库连接池的配置，是自己容器中没有 `DataSource` 才自动配置的
    - 默认配置好的连接池 `HikariDataSource`
  - `DataSourceTransactionManagerAutoConfiguration` 事务管理器自动配置
  - `JdbcTemplateAutoConfiguration` JdbcTemplate 自动配置
    - 修改 JdbcTemplate 相关的配置 `spring.jdbc`
  - `JndiDataSourceAutoConfiguration` Jndi 自动配置
  - `XADataSourceAutoConfiguration` 分布式事务相关

```java
package org.springframework.boot.autoconfigure.jdbc;
@SuppressWarnings("deprecation")
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
@ConditionalOnMissingBean(type = "io.r2dbc.spi.ConnectionFactory")
@EnableConfigurationProperties(DataSourceProperties.class)
@Import({ ... })
public class DataSourceAutoConfiguration {
    // ...
    @Configuration(proxyBeanMethods = false)
    @Conditional(PooledDataSourceCondition.class)
    @ConditionalOnMissingBean({ DataSource.class, XADataSource.class })
    @Import({ DataSourceConfiguration.Hikari.class, DataSourceConfiguration.Tomcat.class,
            DataSourceConfiguration.Dbcp2.class, DataSourceConfiguration.OracleUcp.class,
            DataSourceConfiguration.Generic.class, DataSourceJmxConfiguration.class })
    protected static class PooledDataSourceConfiguration {
    }
    // ...
}
```

```java
package org.springframework.boot.autoconfigure.jdbc;
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties implements BeanClassLoaderAware, InitializingBean {}
```

```java
package org.springframework.boot.autoconfigure.jdbc;
abstract class DataSourceConfiguration {
    protected static <T> T createDataSource(DataSourceProperties properties,
                                            Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }
    // ...
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(HikariDataSource.class)
    @ConditionalOnMissingBean(DataSource.class)
    @ConditionalOnProperty(
            name = "spring.datasource.type",
            havingValue = "com.zaxxer.hikari.HikariDataSource",
            matchIfMissing = true)
    static class Hikari {
        @Bean
        @ConfigurationProperties(prefix = "spring.datasource.hikari")
        HikariDataSource dataSource(DataSourceProperties properties) {
            HikariDataSource dataSource = createDataSource(properties, HikariDataSource.class);
            if (StringUtils.hasText(properties.getName())) {
                dataSource.setPoolName(properties.getName());
            }
            return dataSource;
        }
    }
    // ...
}
```

### 2.4. 修改配置项

```yaml
spring:
  datasource:
    url: jdbc:mysql://192.168.9.16:44060/spring_boot2_data?useUnicode=true&characterEncoding=UTF-8
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 2.5. 使用 JdbcTemplate 操作数据库

```java
package org.springframework.boot.autoconfigure.jdbc;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ DataSource.class, JdbcTemplate.class })
@ConditionalOnSingleCandidate(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(JdbcProperties.class)
@Import({ DatabaseInitializationDependencyConfigurer.class,
        JdbcTemplateConfiguration.class,
        NamedParameterJdbcTemplateConfiguration.class })
public class JdbcTemplateAutoConfiguration {
}
```

```java
package org.springframework.boot.autoconfigure.jdbc;
@ConfigurationProperties(prefix = "spring.jdbc")
public class JdbcProperties {}
```

```java
package org.springframework.boot.autoconfigure.jdbc;
@Configuration(proxyBeanMethods = false)
@ConditionalOnMissingBean(JdbcOperations.class)
class JdbcTemplateConfiguration {
    @Bean
    @Primary
    JdbcTemplate jdbcTemplate(DataSource dataSource, JdbcProperties properties) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcProperties.Template template = properties.getTemplate();
        jdbcTemplate.setFetchSize(template.getFetchSize());
        jdbcTemplate.setMaxRows(template.getMaxRows());
        if (template.getQueryTimeout() != null) {
            jdbcTemplate.setQueryTimeout((int) template.getQueryTimeout().getSeconds());
        }
        return jdbcTemplate;
    }
}
```

```yaml
spring:
  jdbc:
    template:
      query-timeout: 3
```

```java
@Slf4j
@SpringBootTest
class JdbcTemplateTest {
    @Resource
    JdbcTemplate jdbcTemplate;
    @Test
    void TestJdbcTemplate() {
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(
                "select user_id, money from account_tbl;");
        maps.forEach(System.out::println);
        Integer count = jdbcTemplate.queryForObject(
                "select count(id) from account_tbl;", Integer.class);
        log.info("account_tbl表中记录有 {} 条", count);
    }
}
```

## 3. 使用 Druid 数据源

整合第三方技术的两种方式

- 自定义
- 找 starter

### 3.1. 导入 Druid 依赖

```xml
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>druid</artifactId>
  <version>${druid.version}</version>
</dependency>
```

### 3.2. 使用 xml 配置 Druid 数据源

```xml
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource"
    destroy-method="close">
    <property name="url" value="${jdbc.url}" />
    <property name="username" value="${jdbc.username}" />
    <property name="password" value="${jdbc.password}" />
    <property name="maxActive" value="20" />
    <property name="initialSize" value="1" />
    <property name="maxWait" value="60000" />
    <property name="minIdle" value="1" />
    <property name="timeBetweenEvictionRunsMillis" value="60000" />
    <property name="minEvictableIdleTimeMillis" value="300000" />
    <property name="testWhileIdle" value="true" />
    <property name="testOnBorrow" value="false" />
    <property name="testOnReturn" value="false" />
    <property name="poolPreparedStatements" value="true" />
    <property name="maxOpenPreparedStatements" value="20" />
```

### 3.3. 使用配置类配置 Druid 数据源

```java
@Configuration
public class MyDataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }
}
```

- [https://github.com/alibaba/druid/wiki/常见问题](https://github.com/alibaba/druid/wiki/常见问题)

```java
@Configuration
public class MyDataSourceConfig {
    @Bean
    @ConfigurationProperties("spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }
    @Bean
    public ServletRegistrationBean<StatViewServlet> druidStatView() {
        StatViewServlet statViewServlet = new StatViewServlet();
        ServletRegistrationBean<StatViewServlet> registrationBean = new ServletRegistrationBean<>(statViewServlet, "/druid/*");
        registrationBean.addInitParameter("loginUsername", "druid");
        registrationBean.addInitParameter("loginPassword", "druid");
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidWebStatFilter() {
        WebStatFilter webStatFilter = new WebStatFilter();
        FilterRegistrationBean<WebStatFilter> registrationBean = new FilterRegistrationBean<>(webStatFilter);
        registrationBean.setUrlPatterns(Collections.singletonList("/*"));
        registrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return registrationBean;
    }
}
```

```yaml
spring:
  datasource:
    url: jdbc:mysql://192.168.9.16:44060/spring_boot2_data?useUnicode=true&characterEncoding=UTF-8
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    max-active: 10
    filters: stat,wall
```

### 3.4. 引入 starter

- [https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter](https://github.com/alibaba/druid/tree/master/druid-spring-boot-starter)

```xml
<dependency>
  <groupId>com.alibaba</groupId>
  <artifactId>druid-spring-boot-starter</artifactId>
  <version>${druid.version}</version>
</dependency>
```

- 扩展配置项
  - `spring.datasource.druid`
  - `spring.datasource`

```java
package com.alibaba.druid.spring.boot.autoconfigure;
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({DruidStatProperties.class, DataSourceProperties.class})
@Import({DruidSpringAopConfiguration.class,
    DruidStatViewServletConfiguration.class,
    DruidWebStatFilterConfiguration.class,
    DruidFilterConfiguration.class})
public class DruidDataSourceAutoConfigure {
    // ...
    @Bean(initMethod = "init")
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        return new DruidDataSourceWrapper();
    }
}
```

```java
package com.alibaba.druid.spring.boot.autoconfigure.properties;
@ConfigurationProperties("spring.datasource.druid")
public class DruidStatProperties {}
```

- `DruidSpringAopConfiguration.class`
  - 监控 `SpringBean` 的
  - 配置项 `spring.datasource.druid.aop-patterns`

```java
package com.alibaba.druid.spring.boot.autoconfigure.stat;
@ConditionalOnProperty("spring.datasource.druid.aop-patterns")
public class DruidSpringAopConfiguration {}
```

- `DruidStatViewServletConfiguration.class`
  - 监控页的配置 `spring.datasource.druid.stat-view-servlet` 默认开启

```java
package com.alibaba.druid.spring.boot.autoconfigure.stat;
@ConditionalOnWebApplication
@ConditionalOnProperty(
        name = "spring.datasource.druid.stat-view-servlet.enabled",
        havingValue = "true")
public class DruidStatViewServletConfiguration {}
```

- `DruidWebStatFilterConfiguration.class`
  - web 监控配置 `spring.datasource.druid.web-stat-filter` 默认开启

```java
package com.alibaba.druid.spring.boot.autoconfigure.stat;
@ConditionalOnWebApplication
@ConditionalOnProperty(
        name = "spring.datasource.druid.web-stat-filter.enabled",
        havingValue = "true")
public class DruidWebStatFilterConfiguration {}
```

- `DruidFilterConfiguration.class`
  - 所有 Druid 自己 filter 的配置

```java
package com.alibaba.druid.spring.boot.autoconfigure.stat;
public class DruidFilterConfiguration {
    // ...
    private static final String FILTER_STAT_PREFIX = "spring.datasource.druid.filter.stat";
    private static final String FILTER_CONFIG_PREFIX = "spring.datasource.druid.filter.config";
    private static final String FILTER_ENCODING_PREFIX = "spring.datasource.druid.filter.encoding";
    private static final String FILTER_SLF4J_PREFIX = "spring.datasource.druid.filter.slf4j";
    private static final String FILTER_LOG4J_PREFIX = "spring.datasource.druid.filter.log4j";
    private static final String FILTER_LOG4J2_PREFIX = "spring.datasource.druid.filter.log4j2";
    private static final String FILTER_COMMONS_LOG_PREFIX = "spring.datasource.druid.filter.commons-log";
    private static final String FILTER_WALL_PREFIX = "spring.datasource.druid.filter.wall";
    private static final String FILTER_WALL_CONFIG_PREFIX = FILTER_WALL_PREFIX + ".config";
}
```

### 3.5. 配置示例

```yaml
spring:
  datasource:
    url: jdbc:mysql://192.168.9.16:44060/spring_boot2_data?useUnicode=true&characterEncoding=UTF-8
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    druid:
      aop-patterns: com.alibaba.druid.*
      filters: stat,wall,slf4j
      stat-view-servlet:
        enabled: true
        login-username: admin
        login-password: admin
        resetEnable: false
      web-stat-filter:
        enabled: true
        urlPattern: /*
        exclusions: '*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*'
      filter:
        stat:
          slow-sql-millis: 1000
          logSlowSql: true
          enabled: true
        wall:
          enabled: true
          config:
            drop-table-allow: false
        slf4j:
          enabled: true
```

## 4. 整合 MyBatis 操作

### 4.1. 引入 starter

- SpringBoot 官方的 starter `spring-boot-starter-*`
- 第三方的 starter `*-spring-boot-starter`

```xml
<dependency>
  <groupId>org.mybatis.spring.boot</groupId>
  <artifactId>mybatis-spring-boot-starter</artifactId>
  <version>${mybatis.version}</version>
</dependency>
```

- `mybatis-spring-boot-starter` 的依赖

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
  </dependency>
  <dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-autoconfigure</artifactId>
  </dependency>
  <dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
  </dependency>
  <dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
  </dependency>
</dependencies>
```

### 4.2. 自动配置原理

- 全局配置文件
  - `SqlSessionFactory`
  - `SqlSessionTemplate` 组合了 `SqlSession`
  - `@Import(AutoConfiguredMapperScannerRegistrar.class)`
  - 只要我们写的操作 `MyBatis` 的接口标注了 `@Mapper` 注解，就会被自动扫描进来

```properties
# Auto Configure
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.mybatis.spring.boot.autoconfigure.MybatisLanguageDriverAutoConfiguration,\
org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration
```

```java
package org.mybatis.spring.boot.autoconfigure;
@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(MybatisProperties.class)
@AutoConfigureAfter({
        DataSourceAutoConfiguration.class,
        MybatisLanguageDriverAutoConfiguration.class })
public class MybatisAutoConfiguration implements InitializingBean {
    // ...
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        // ...
    }
    // ...
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        // ...
    }
    // ...
    @org.springframework.context.annotation.Configuration
    @Import(AutoConfiguredMapperScannerRegistrar.class)
    @ConditionalOnMissingBean({ MapperFactoryBean.class, MapperScannerConfigurer.class })
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {
        // ...
    }
}
```

```java
package org.mybatis.spring.boot.autoconfigure;
@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
public class MybatisProperties {
    public static final String MYBATIS_PREFIX = "mybatis";
    // ...
}
```

### 4.3. 配置模式

- 编写 mapper 接口
  - 方式一、在 Mapper 接口上标注 `@Mapper` 注解
  - 方式二、在启动类上加上 `@MapperScan` 注解

```java
@Mapper
public interface AccountMapper {
    Account getAccount(Integer id);
}
```

```java
@MapperScan
@SpringBootApplication
public class SpringBoot2DataApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBoot2DataApplication.class, args);
    }
}
```

- 编写 sql 映射文件并绑定 mapper 接口

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.example.spring_boot2.data.mapper.AccountMapper">
  <!-- Account getAccount(Integer id); -->
  <select id="getAccount" resultType="org.example.spring_boot2.data.bean.Account">
    select id, user_id, money
    from account_tbl
    where id = #{id};
  </select>
</mapper>
```

- 在 `application.yml` 中
  - 指定全局配置信息
    - 方式一、指定全局配置文件的信息
    - 方式二、配置在 `mybatis.configuration`
  - 指定 Mapper 配置文件的位置

```yml
mybatis:
  config-location: classpath:mybatis/mybatis-config.xml
  mapper-locations: classpath:mybatis/mapper/*.xml
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <settings>
    <setting name="mapUnderscoreToCamelCase" value="true"/>
  </settings>
</configuration>
```

```yaml
mybatis:
  mapper-locations: classpath:mybatis/mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
```

### 4.4. 注解模式

```java
public interface CityMapper {
    @Select("select id, name, state, country from city_tbl where id=#{id}")
    City getCity(Integer id);

    @Insert("insert into city_tbl(`name`, `state`, `country`) values (#{name}, #{state}, #{country});")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertCity(City city);
}
```

### 4.5. 混合模式

```java
public interface CityMapper {
    @Select("select id, name, state, country from city_tbl where id=#{id}")
    City getCity(Integer id);

    void insertCity(City city);
}
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.example.spring_boot2.data.mapper.CityMapper">
  <!-- City insertCity(City city); -->
  <insert id="insertCity" useGeneratedKeys="true" keyProperty="id">
    insert into city_tbl(`name`, `state`, `country`)
    values (#{name}, #{state}, #{country});
  </insert>
</mapper>
```

### 4.6. 最佳实战

- 引入 `mybatis-starter`
- 配置 `application.yaml` 中，指定 `mapper-location` 位置即可
- 编写 Mapper 接口并标注 `@Mapper` 注解
  - 简单方法直接注解方式
  - 复杂方法编写 `mapper.xml` 进行绑定映射
- `@MapperScan("package")` 简化，其他的接口就可以不用标注 `@Mapper` 注解

## 5. 整合 MyBatis Plus 完成 CRUD

### 5.1. 引入 starter

```xml
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version>${mybatis-plus.version}</version>
</dependency>
```

### 5.2. 自动配置原理

- `MybatisPlusAutoConfiguration` 配置类
  - `MybatisPlusProperties` 配置项绑定 `mybatis-plus`
  - `mapperLocations` 自动配置好的
  - 有默认值 `classpath*:/mapper/**/*.xml`
  - 任意包的类路径下的所有 mapper 文件夹下意路径下的所有 xml 都是 sql 映射文件
  - 建议以后 sql 映射文件，放在 mapper 下
- `SqlSessionFactory` 自动配置好
  - 底层是容器中默认的数据源
- 容器中也自动配置好了 `SqlSessionTemplate`
- `@Mapper` 标注的接口也会被自动扫描
  - 建议直接 `@MapperScan("package")` 批量扫描

```properties
# Auto Configure
org.springframework.boot.env.EnvironmentPostProcessor=\
  com.baomidou.mybatisplus.autoconfigure.SafetyEncryptProcessor
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.baomidou.mybatisplus.autoconfigure.IdentifierGeneratorAutoConfiguration,\
  com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration,\
  com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration
```

```java
package com.baomidou.mybatisplus.autoconfigure;
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(MybatisPlusProperties.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class})
public class MybatisPlusAutoConfiguration implements InitializingBean {
    // ...
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        // ...
    }
    // ...
    @Bean
    @ConditionalOnMissingBean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        // ...
    }
    // ...
    @Configuration
    @Import(AutoConfiguredMapperScannerRegistrar.class)
    @ConditionalOnMissingBean({MapperFactoryBean.class, MapperScannerConfigurer.class})
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {
        // ...
    }
}
```

```java
package com.baomidou.mybatisplus.autoconfigure;
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = Constants.MYBATIS_PLUS)
public class MybatisPlusProperties {
    // ...
    private String[] mapperLocations = new String[]{"classpath*:/mapper/**/*.xml"};
    // ...
}
```

### 5.3. 测试查询

```yaml
mybatis-plus:
  mapper-locations: classpath:mybatis/mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
```

```sql
drop table if exists user_tbl;
create table user_tbl
(
    id    int primary key auto_increment,
    name  varchar(30) default null,
    age   int         default null,
    email varchar(30) default null
);
insert into user_tbl(name, age, email)
values ('test1', 11, 'test1@example.com'),
       ('test2', 12, 'test2@example.com'),
       ('test3', 13, 'test3@example.com'),
       ('test4', 14, 'test4@example.com'),
       ('test5', 15, 'test5@example.com');
```

```java
@Data
@TableName("user_tbl")
public class User {
    private int id;
    private String name;
    private int age;
    private String email;
    @TableField(exist = false)
    private String address;
}
```

```java
public interface UserMapper extends BaseMapper<User> {
}
```

### 5.4. 实现 CRUD

```java
public interface UserService extends IService<User> {
}
```

```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
```

- 注入 Service 实现类

```java
@Slf4j
@Controller
@RequestMapping("/mybatis_plus")
public class MybatisPlusController {
    @Resource
    UserService userServiceImpl;
    // ...
}
```

- 查询

```java
@ResponseBody
@GetMapping("/list")
public List<User> listUsers() {
    List<User> list = userServiceImpl.list();
    log.info("查询到的数据 {}", list);
    return list;
}
```

- 分页
- Mybatis-Plus 分页插件

```java
@ResponseBody
@GetMapping("/page")
public Page<User> pageUsers(@RequestParam(value = "pn", defaultValue = "1") Integer pn) {
    Page<User> userPage = new Page<>(pn, 2);
    Page<User> page = userServiceImpl.page(userPage, null);
    log.info("查询到的数据 {}", page.getRecords());
    return page;
}
```

```java
@Configuration
public class MybatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }
}
```

- 删除

```java
@ResponseBody
@GetMapping("/delete/{id}")
public User deleteUser(@PathVariable("id") Integer id) {
    User user = userServiceImpl.getById(id);
    log.info("要删除的数据 {}", user);
    if (user != null && userServiceImpl.removeById(id)) {
        return user;
    }
    return null;
}
```

## 6. NoSQL

### 6.1. 引入 starter

- `spring-boot-starter-data-redis`

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 6.2. 自动配置原理

- `RedisAutoConfiguration` 自动配置类
- `RedisProperties` 配置项 `spring.redis.xxx`
- 自动注入连接工厂
  - `LettuceConnectionConfiguration、`
  - `JedisConnectionConfiguration`
- 自动注入了 `RedisTemplate<Object, Object>` `xxxTemplate`
- 自动注入了 `StringRedisTemplate` `k: v` 都是 `String`
- 操作 redis
  - `RedisTemplate`
  - `StringRedisTemplate`

```java
package org.springframework.boot.autoconfigure.data.redis;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
@Import({ LettuceConnectionConfiguration.class, JedisConnectionConfiguration.class })
public class RedisAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // ...
    }
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // ...
    }
}
```

```java
package org.springframework.boot.autoconfigure.data.redis;
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {}
```

### 6.3. 使用配置

```yaml
spring:
  redis:
    #    url: redis://192.168.9.16:63790
    host: 192.168.9.16
    port: 63790
    #    password: username:passowrd
```

- 默认使用 lettuce 客户端
- 切换 jedis 客户端

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
  <exclusions>
    <exclusion>
      <groupId>io.lettuce</groupId>
      <artifactId>lettuce-core</artifactId>
    </exclusion>
  </exclusions>
</dependency>
<dependency>
  <groupId>redis.clients</groupId>
  <artifactId>jedis</artifactId>
</dependency>
```

```yaml
spring:
  redis:
    client-type: jedis
```

### 6.4. 访问统计

```java
@Slf4j
@Component
public class UrlCountInterceptor implements HandlerInterceptor {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String uri = request.getRequestURI();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.increment(uri);
        log.info("获取到的数据 {}: {}", uri, operations.get(uri));
        return true;
    }
}
```

```java
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Resource
    UrlCountInterceptor urlCountInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(urlCountInterceptor)
                .addPathPatterns("/mybatis/**")
                .addPathPatterns("/mybatis_plus/**");
    }
}
```
