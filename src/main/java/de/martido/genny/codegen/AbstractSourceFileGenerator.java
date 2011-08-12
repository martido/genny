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
package de.martido.genny.codegen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Files;

import de.martido.genny.Field;
import de.martido.genny.FieldFilter;
import de.martido.genny.FieldMapper;
import de.martido.genny.FieldProvider;
import de.martido.genny.SourceFileGenerator;

/**
 * An abstract base class for {@link SourceFileGenerator}s.
 * 
 * @author Martin Dobmeier
 */
public abstract class AbstractSourceFileGenerator implements SourceFileGenerator {

  /**
   * Gets the fields that should be generated.
   * 
   * @param field
   *          <i>not null</i> - a {@link FieldProvider} to provide the data for each field.
   * @param fieldMapper
   *          <i>not null</i> - a {@link FieldMapper} to optionally transform each field.
   * @param fieldFilter
   *          <i>not null</i> - a {@link FieldFilter} to optionally filter fields.
   * @return A list of {@link Field}s; may be empty, but never {@code null}.
   * @throws Exception
   *           If an error occured.
   */
  protected List<Field> getFields(FieldProvider fieldProvider, FieldMapper fieldMapper,
      FieldFilter fieldFilter) throws Exception {

    List<Field> fields = fieldProvider.provide(fieldFilter);

    if (fields.isEmpty()) {
      return Collections.emptyList();
    }

    List<Field> mapped = new ArrayList<Field>(fields.size());
    for (Field f : fields) {
      mapped.add(fieldMapper.map(f));
    }

    return mapped;
  }

  /**
   * Creates the actual source file and, if necessary, all parent directories.
   * 
   * @param targetDefinition
   *          <i>mandatory</i> - the {@link TargetDefinition}.
   * @return A {@code File}.
   * @throws IOException
   *           If an I/O error occured.
   */
  protected File createFile(TargetDefinition targetDefinition) throws IOException {
    File file = new File(targetDefinition.asPath());
    Files.createParentDirs(file);
    Files.touch(file);
    return file;
  }

}
