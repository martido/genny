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
package de.martido.genny.example.config;

import java.util.List;

import de.martido.genny.Field;
import de.martido.genny.FieldFilter;
import de.martido.genny.FieldMapper;
import de.martido.genny.GeneratorDefinition;
import de.martido.genny.GennyConfiguration;
import de.martido.genny.SourceFileGenerator;
import de.martido.genny.codegen.StringTemplateSourceFileGenerator;

public class PropertiesConfiguration implements GennyConfiguration {

  @Override
  public GeneratorDefinition configure(List<String> inputFiles) {
    return new GeneratorDefinition(inputFiles) {

      /* A custom FieldMapper: Exclude a prefix and make field names upper case. */
      @Override
      public FieldMapper getFieldMapper() {
        return new FieldMapper() {
          @Override
          public Field map(Field f) {
            String name = f.getName()
                .replaceFirst("prefix\\.", "")
                .replaceAll("\\.", "_")
                .toUpperCase();
            return new Field(name, f.getValue(), f.getJavadoc());
          }
        };
      }

      /* A custom FieldFilter: Exclude certain properties. */
      @Override
      public FieldFilter getFieldFilter() {
        return new FieldFilter() {
          @Override
          public boolean include(Field field) {
            return field.getName().equals("property.internal") ? false : true;
          }
        };
      }

      /* A custom StringTemplate template. */
      @Override
      public SourceFileGenerator getSourceFileGenerator() {
        return new StringTemplateSourceFileGenerator("customTemplate.stg");
      }
    };
  }

}
