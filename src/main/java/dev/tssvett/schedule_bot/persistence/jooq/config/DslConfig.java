package dev.tssvett.schedule_bot.persistence.jooq.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
@Configuration
public class DslConfig {

    @Bean
    public DSLContext dslContext() throws SQLException {
        String userName = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String url = System.getenv("DB_URL");
        Connection connection = DriverManager.getConnection(url, userName, password);

        DSLContext create = DSL.using(connection, SQLDialect.POSTGRES);
        log.info("Recognized {} tables", create.meta().getTables().size());

        return create;
    }
}
