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
package de.martido.genny.example.generator;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import de.martido.genny.Field;
import de.martido.genny.FieldFilter;
import de.martido.genny.FieldMapper;
import de.martido.genny.GeneratorDefinition;
import de.martido.genny.GennyConfiguration;
import de.martido.genny.codegen.StringTemplateSourceFileGenerator;
import de.martido.genny.provider.PropertyFileProvider;

public class PropertiesConfiguration implements GennyConfiguration {

  public List<GeneratorDefinition> configure() {

    GeneratorDefinition def = new GeneratorDefinition();
    def.setTargetClass("de.martido.genny.example.generated.Property");
    def.setBaseDirectory("src/generated/java");
    def.setFieldProvider(PropertyFileProvider
        .forFile("src/main/resources/example.properties")
        .fromFileSystem()
        .withCharset(Charset.forName("utf-8"))
        .build());

    /* A custom FieldMapper: Exclude a prefix and make field names upper case. */
    def.setFieldMapper(new FieldMapper() {
      public Field map(Field f) {
        String name = f.getName()
            .replaceFirst("prefix\\.", "")
            .replaceAll("\\.", "_")
            .toUpperCase();
        return new Field(name, f.getValue(), f.getJavadoc());
      }
    });

    /* A custom FieldFilter: Exclude certain properties. */
    def.setFieldFilter(new FieldFilter() {
      public boolean include(Field field) {
        return field.getName().equals("property.internal") ? false : true;
      }
    });

    /* A custom StringTemplate template. */
    def.setSourceFileGenerator(new StringTemplateSourceFileGenerator("customTemplate.stg"));

    return Arrays.asList(def);
  }

}
