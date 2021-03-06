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

import java.io.IOException;
import java.util.Properties;

public class MovieCharacters {

  private static final Properties properties = new Properties();

  public static void main(String[] args) throws IOException {
    load("example.properties");
    System.out.println(getProperty(Property.THE_GOOD.getKey()));
    System.out.println(getProperty(Property.THE_BAD.getKey()));
    System.out.println(getProperty(Property.THE_UGLY.getKey()));
  }

  public static void load(String fileName) throws IOException {
    properties.load(MovieCharacters.class.getClassLoader().getResourceAsStream(fileName));
  }

  public static String getProperty(String key) {
    return properties.getProperty(key);
  }

}
