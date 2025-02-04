package io.builders.demo.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class DatabaseCleaner {

    @Autowired
    List<JdbcTemplate> jdbcTemplates

    void cleanup() {
        jdbcTemplates.each { jdbcTemplate ->
            List<String> tables = getTableNames(jdbcTemplate)
            tables.removeAll { table -> table.containsIgnoreCase('changelog') }
            disableAllTriggers(tables, jdbcTemplate)
            truncateAllTables(tables, jdbcTemplate)
            enableAllTriggers(tables, jdbcTemplate)
        }
    }

    private static List<String> getTableNames(JdbcTemplate jdbcTemplate) {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public' AND table_type = 'BASE TABLE';"
        return jdbcTemplate.queryForList(sql, String)
    }

    private static void disableAllTriggers(List<String> tableNames, JdbcTemplate jdbcTemplate) {
        tableNames.each { String tableName ->
            String sql = "ALTER TABLE public.$tableName DISABLE TRIGGER ALL;"
            jdbcTemplate.execute(sql)
        }
    }

    private static void enableAllTriggers(List<String> tableNames, JdbcTemplate jdbcTemplate) {
        tableNames.each { tableName ->
            String sql = "ALTER TABLE public.$tableName ENABLE TRIGGER ALL;"
            jdbcTemplate.execute(sql)
        }
    }

    private static void truncateAllTables(List<String> tableNames, JdbcTemplate jdbcTemplate) {
        if (tableNames.empty) {
            return
        }
        String sql = 'TRUNCATE TABLE ' + tableNames.collect { tableName -> "public.$tableName" }.join(', ') + ' RESTART IDENTITY CASCADE;'
        jdbcTemplate.execute(sql)
    }

}
