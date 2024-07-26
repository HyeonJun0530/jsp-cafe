package com.woowa.cafe.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/woowa";
    private static final String USERNAME = "root";

    private final DataSource dataSource;

    public DataSourceConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(URL);
        config.setUsername(USERNAME);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(5);

        this.dataSource = new HikariDataSource(config);

        createTable(dataSource);
    }

    public static void createTable(final DataSource dataSource) {
        new Thread(() -> {
            String dropMemberTable = "DROP TABLE IF EXISTS members";
            String createMemberTable = "CREATE TABLE members (" +
                    "member_id VARCHAR(255) PRIMARY KEY, " +
                    "password VARCHAR(255), " +
                    "name VARCHAR(255), " +
                    "email VARCHAR(255))";

            String dropArticleTable = "DROP TABLE IF EXISTS articles";
            String createArticleTable = "CREATE TABLE articles (" +
                    "article_id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                    "writer_id VARCHAR(255), " +
                    "title VARCHAR(255), " +
                    "contents TEXT, " +
                    "create_at TIMESTAMP, " +
                    "modified_at TIMESTAMP)";

            try (var connection = dataSource.getConnection()) {
                connection.prepareStatement(dropMemberTable).execute();
                connection.prepareStatement(createMemberTable).execute();
                connection.prepareStatement(dropArticleTable).execute();
                connection.prepareStatement(createArticleTable).execute();
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}