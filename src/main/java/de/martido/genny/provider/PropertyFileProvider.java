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
package de.martido.genny.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import de.martido.genny.Field;
import de.martido.genny.FieldFilter;
import de.martido.genny.FieldProvider;
import de.martido.genny.util.Logger;

/**
 * A default {@link FieldProvider} that works with regular Java property files like
 * {@code java.util.Properties} and {@code java.util.PropertyResourceBundle}. Therefore, the name
 * and value of a provided {@link Field} will be the property's key. Filenames must be specified
 * relative to the current working directory. If not otherwise specified, files are assumed to be
 * UTF-8 encoded.
 * 
 * @author Martin Dobmeier
 */
public class PropertyFileProvider implements FieldProvider {

  /**
   * The property file's charset.
   * <p>
   * By default set to the platform's default character set.
   */
  private final Charset charset;

  /**
   * A list of property file names.
   */
  private final List<String> propertyFileNames;

  private PropertyFileProvider(Map<String, Object> context) {

    @SuppressWarnings("unchecked")
    List<String> propertyFileNames = (List<String>) context.get("propertyFileNames");
    this.propertyFileNames = propertyFileNames;

    Charset charset = (Charset) context.get("charset");
    this.charset = charset != null ? charset : Charset.forName("UTF-8");
  }

  public static AwaitingCharSet forFile(String propertyFileName) {
    return forFiles(propertyFileName);
  }

  /**
   * Specifies the list of property files from which to generate the class. The files will be loaded
   * in the order given, i.e. properties defined in files may overwrite previously defined
   * properties.
   */
  public static AwaitingCharSet forFiles(String... propertyFileNames) {
    String[] copy = new String[propertyFileNames.length];
    System.arraycopy(propertyFileNames, 0, copy, 0, propertyFileNames.length);
    Map<String, Object> context = new HashMap<String, Object>();
    context.put("propertyFileNames", Arrays.asList(copy));
    return new AwaitingCharSet(context);
  }

  /**
   * @author Martin Dobmeier
   */
  public static abstract class AbstractIntermediaryResult {

    final Map<String, Object> context;

    private AbstractIntermediaryResult(Map<String, Object> context) {
      this.context = context;
    }

    public PropertyFileProvider build() {
      return new PropertyFileProvider(this.context);
    }

  }

  /**
   * @author Martin Dobmeier
   */
  public static class AwaitingCharSet extends AbstractIntermediaryResult {

    private AwaitingCharSet(Map<String, Object> context) {
      super(context);
    }

    public AbstractIntermediaryResult withCharset(Charset charset) {
      this.context.put("charset", charset);
      return this;
    }

  }

  public List<Field> provide(FieldFilter fieldFilter) throws Exception {

    Properties all = new Properties();
    for (String fileName : this.propertyFileNames) {
      Properties props = this.load(fileName, this.charset);
      all.putAll(props);
    }

    if (all.isEmpty()) {
      return Collections.emptyList();
    }

    List<Field> fields = new ArrayList<Field>(all.size());
    for (Entry<Object, Object> entry : all.entrySet()) {
      String key = (String) entry.getKey();
      Field f = new Field(key, key);
      if (fieldFilter.include(f)) {
        fields.add(f);
      }
    }

    if (Logger.get().isVerbose()) {
      for (Field field : fields) {
        System.out.println("Generated field: " + field);
      }
    }

    return fields;
  }

  private Properties load(String propertyFileName, Charset charset) throws IOException {

    if (Logger.get().isVerbose()) {
      System.out.println("Loading " + propertyFileName);
    }

    Properties props = new Properties();

    InputStreamReader reader = null;
    try {
      InputStream in = new FileInputStream(new File(propertyFileName));
      reader = new InputStreamReader(in, charset);
      props.load(reader);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }

    return props;
  }

  @Override
  public String toString() {
    return "PropertyFileProvider [" //
        + "propertyFileNames=" + this.propertyFileNames //
        + ", charset=" + this.charset + "]";
  }

}
