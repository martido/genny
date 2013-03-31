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

/**
 * Defines a generator of a source file.
 * 
 * @author Martin Dobmeier
 */
public class GeneratorDefinition {

  /** The source file to generate. */
  private final String targetClass;

  /** The base directory of the generated source file. */
  private final String baseDirectory;

  /** The names of the input files from which {@code targetClass} class shall be generated. */
  private final List<String> inputFiles;

  /** The {@link FieldProvider} used for this generator. */
  private FieldProvider fieldProvider;

  /** An optional {@link FieldMapper}. */
  private FieldMapper fieldMapper;

  /** An optional {@link FieldFilter}. */
  private FieldFilter fieldFilter;

  /** An optional {@link SourceFileGenerator}. */
  private SourceFileGenerator sourceFileGenerator;

  public GeneratorDefinition(String targetClass, String baseDirectory, String inputFile) {
    this(targetClass, baseDirectory, Collections.singletonList(inputFile));
  }

  public GeneratorDefinition(String targetClass, String baseDirectory, List<String> inputFiles) {
    this.targetClass = targetClass;
    this.baseDirectory = baseDirectory;
    this.inputFiles = inputFiles;
  }

  public String getTargetClass() {
    return this.targetClass;
  }

  public String getBaseDirectory() {
    return this.baseDirectory;
  }

  public SourceFile getSourceFile() {
    return new SourceFile(this.targetClass, this.baseDirectory);
  }

  public List<String> getInputFiles() {
    return Collections.unmodifiableList(this.inputFiles);
  }

  public FieldProvider getFieldProvider() {
    return this.fieldProvider;
  }

  public FieldMapper getFieldMapper() {
    return this.fieldMapper == null ? FieldMapper.DEFAULT : this.fieldMapper;
  }

  public FieldFilter getFieldFilter() {
    return this.fieldFilter == null ? FieldFilter.INCLUDE_ALL : this.fieldFilter;
  }

  public SourceFileGenerator getSourceFileGenerator() {
    return this.sourceFileGenerator == null
        ? new SimpleSourceFileGenerator()
        : this.sourceFileGenerator;
  }

  public void setFieldProvider(FieldProvider fieldProvider) {
    this.fieldProvider = fieldProvider;
  }

  public void setFieldMapper(FieldMapper fieldMapper) {
    this.fieldMapper = fieldMapper;
  }

  public void setFieldFilter(FieldFilter fieldFilter) {
    this.fieldFilter = fieldFilter;
  }

  public void setSourceFileGenerator(SourceFileGenerator sourceFileGenerator) {
    this.sourceFileGenerator = sourceFileGenerator;
  }

  @Override
  public String toString() {
    return "GeneratorDefinition [\n"
        + "\ttargetClass=" + this.targetClass + "\n"
        + "\tbaseDirectory=" + this.baseDirectory + "\n"
        + "\tinputFiles=" + this.inputFiles + "\n"
        + "\tfieldProvider=" + this.fieldProvider + "\n"
        + "\tfieldMapper=" + this.fieldMapper + "\n"
        + "\tfieldFilter=" + this.fieldFilter + "\n"
        + "\tsourceFileGenerator=" + this.sourceFileGenerator + "]";
  }

}
