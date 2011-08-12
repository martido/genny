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
package de.martido.genny;


/**
 * A source file generator.
 * 
 * @author Martin Dobmeier
 */
public interface SourceFileGenerator {

  /**
   * @param sourceFile
   *          <i>mandatory</i> - the source file that should be generated.
   * @param field
   *          <i>mandatory</i> - a {@link FieldProvider} to provide the data for each field.
   * @param fieldMapper
   *          <i>mandatory</i> - a {@link FieldMapper} to optionally transform each field.
   * @param fieldFilter
   *          <i>mandatory</i> - a {@link FieldFilter} to optionally filter fields.
   * @throws Exception
   *           If the source file could not be generated.
   */
  void generate(SourceFile sourceFile,
      FieldProvider field,
      FieldMapper fieldMapper,
      FieldFilter fieldFilter)
      throws Exception;

}
