/*
 * Copyright 2011 Martin Dobmeier
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
 */
package de.martido.genny.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.martido.genny.Field;
import de.martido.genny.FieldFilter;
import de.martido.genny.FieldProvider;
import de.martido.genny.GeneratorDefinition;
import de.martido.genny.Genny;
import de.martido.genny.GennyConfiguration;
import de.martido.genny.SourceFile;

/**
 * Properties are read from a Derby database. This is a somewhat stupid example that's probably
 * never gonna happen in the real world. It's there anyway.
 */
public class DbConfiguration implements GennyConfiguration {

  public static void main(String[] args) throws Exception {
    DbUtil.loadDatabaseDriver();
    try {
      List<String> statements = DbUtil.readSQLScript("src/main/resources/example.sql");
      DbUtil.execute(statements);
      new Genny().generateFrom(
          new DbConfiguration(),
          Collections.<String> emptyList(),
          new SourceFile("de.martido.genny.example.Db", "src/generated/java"));
    } finally {
      DbUtil.shutdown();
    }
  }

  @Override
  public GeneratorDefinition configure(List<String> inputFiles) {
    return new GeneratorDefinition(inputFiles) {
      @Override
      public FieldProvider getFieldProvider() {
        return new DbProvider();
      }
    };
  }

  /**
   * A custom {@link FieldProvider} that reads properties from a database.
   */
  public static class DbProvider implements FieldProvider {

    @Override
    public List<Field> provide(FieldFilter fieldFilter) {

      List<Field> fields = new ArrayList<Field>();

      Connection conn = null;
      PreparedStatement ps = null;
      ResultSet rs = null;

      boolean executedSuccessfully = false;

      try {
        try {
          conn = DbUtil.getConnection();
          ps = conn.prepareStatement("SELECT * FROM Property");
          rs = ps.executeQuery();

          while (rs.next()) {
            String key = rs.getString("PROPERTY_KEY");
            String value = rs.getString("PROPERTY_VALUE");
            String doc = rs.getString("PROPERTY_DOC");
            Field f = new Field(key, value, doc);
            if (fieldFilter.include(f)) {
              fields.add(f);
            }
          }

          conn.commit();
          executedSuccessfully = true;
        } finally {
          try {
            if (!executedSuccessfully) {
              conn.rollback();
            }
          } finally {
            try {
              if (rs != null) {
                rs.close();
              }
            } finally {
              try {
                if (ps != null) {
                  ps.close();
                }
              } finally {
                if (conn != null) {
                  conn.close();
                }
              }
            }
          }
        }
      } catch (SQLException ex) {
        ex.printStackTrace();
      }

      return fields;
    }

  }

  /**
   * A database utility class.
   */
  public static class DbUtil {

    public static List<String> readSQLScript(String fileName) throws IOException {

      FileInputStream fis = new FileInputStream(new File(fileName));
      InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
      BufferedReader br = new BufferedReader(isr);

      String line;
      StringBuilder statement = new StringBuilder();
      List<String> statements = new ArrayList<String>();
      while ((line = br.readLine()) != null) {
        statement.append(line.trim());
        if (line.endsWith(";")) {
          statements.add(statement.substring(0, statement.length() - 1));
          statement = new StringBuilder();
        }
      }
      return statements;
    }

    public static void loadDatabaseDriver() throws ClassNotFoundException {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    }

    public static void shutdown() {
      try {
        DriverManager.getConnection("jdbc:derby:db;shutdown=true");
      } catch (SQLException ex) {
        // Expected; Derby throws a SQLException on shutdown.
      }
    }

    public static Connection getConnection() throws SQLException {
      Connection conn = DriverManager.getConnection("jdbc:derby:Test");
      conn.setAutoCommit(false);
      return conn;
    }

    public static void execute(List<String> statements) throws SQLException {

      Connection conn = null;
      Statement stmt = null;

      boolean executedSuccessfully = false;

      try {
        conn = DbUtil.getConnection();
        stmt = conn.createStatement();
        for (String statement : statements) {
          stmt.execute(statement);
        }
        conn.commit();
        executedSuccessfully = true;
      } finally {
        try {
          if (!executedSuccessfully) {
            conn.rollback();
          }
        } finally {
          try {
            if (stmt != null) {
              stmt.close();
            }
          } finally {
            if (conn != null) {
              conn.close();
            }
          }
        }
      }
    }

  }

}
