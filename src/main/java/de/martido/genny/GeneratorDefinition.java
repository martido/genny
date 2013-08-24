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

import java.util.Collections;
import java.util.List;

import de.martido.genny.codegen.SimpleSourceFileGenerator;
import de.martido.genny.provider.PropertyFileProvider;

/**
 * Defines the generator of a source file.
 * 
 * @author Martin Dobmeier
 */
public class GeneratorDefinition {

  private final List<String> inputFiles;

  public GeneratorDefinition(List<String> inputFiles) {
    this.inputFiles = inputFiles;
  }

  /**
   * @return An unmodifiable list of the paths to the input files from which the target class shall
   *         be generated.
   */
  public List<String> getInputFiles() {
    return Collections.unmodifiableList(this.inputFiles);
  }

  /**
   * @return The {@link FieldProvider} used for this generator.
   */
  public FieldProvider getFieldProvider() {
    return PropertyFileProvider.forFiles(this.getInputFiles()).build();
  }

  /**
   * @return The {@link FieldMapper}. Defaults to {@link FieldMapper#DEFAULT}.
   */
  public FieldMapper getFieldMapper() {
    return FieldMapper.DEFAULT;
  }

  /**
   * @return The {@link FieldFilter}. Defaults to {@link FieldFilter#INCLUDE_ALL}
   */
  public FieldFilter getFieldFilter() {
    return FieldFilter.INCLUDE_ALL;
  }

  /**
   * @return The {@link SourceFileGenerator}.
   */
  public SourceFileGenerator getSourceFileGenerator() {
    return new SimpleSourceFileGenerator();
  }

  @Override
  public String toString() {
    return "GeneratorDefinition [\n"
        + "\tinputFiles=" + this.getInputFiles() + "\n"
        + "\tfieldProvider=" + this.getFieldProvider() + "\n"
        + "\tfieldMapper=" + this.getFieldMapper() + "\n"
        + "\tfieldFilter=" + this.getFieldFilter() + "\n"
        + "\tsourceFileGenerator=" + this.getSourceFileGenerator() + "]";
  }

}
