// Place in: com.xpressbees.health.DatabaseHealthIndicator
package com.xpressbees.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DatabaseHealthIndicator implements HealthIndicator {

  // ... existing implementation ...
  private final DataSource dataSource;

  public DatabaseHealthIndicator(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Health health() {
    try (Connection conn = dataSource.getConnection()) {
      return Health.up()
          .withDetail("database", conn.getMetaData().getDatabaseProductName())
          .build();
    } catch (Exception e) {
      return Health.down(e).build();
    }
  }
}