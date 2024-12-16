package com.example.firstproject.connection;

import com.example.firstproject.jdbc.connection.DBConnectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class DBConnectionUtilTest {

    @Test
    void connection() {
        Connection connection = DBConnectionUtils.getConnection();
        assertThat(connection).isNotNull();
    }
}
