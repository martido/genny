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
 * Defines a generator of a source file.
 * 
 * @author Martin Dobmeier
 */
public class GeneratorDefinition {

  /** The source file to generate. [mandatory] */
  private String targetClass;

  /** The base directory of the generated source file. [mandatory] */
  private String baseDirectory;

  /** The {@link FieldProvider} used for this generator. [mandatory] */
  private FieldProvider fieldProvider;

  /** An optional {@link FieldMapper}. */
  private FieldMapper fieldMapper;

  /** An optional {@link FieldFilter}. */
  private FieldFilter fieldFilter;

  /** An optional {@link SourceFileGenerator}. */
  private SourceFileGenerator sourceFileGenerator;

  public String getTargetClass() {
    return this.targetClass;
  }

  public String getBaseDirectory() {
    return this.baseDirectory;
  }

  public FieldProvider getFieldProvider() {
    return this.fieldProvider;
  }

  public FieldMapper getFieldMapper() {
    return this.fieldMapper;
  }

  public FieldFilter getFieldFilter() {
    return this.fieldFilter;
  }

  public SourceFileGenerator getSourceFileGenerator() {
    return this.sourceFileGenerator;
  }

  public void setTargetClass(String targetClass) {
    this.targetClass = targetClass;
  }

  public void setBaseDirectory(String baseDirectory) {
    this.baseDirectory = baseDirectory;
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
        + "\tfieldProvider=" + this.fieldProvider + "\n"
        + "\tfieldMapper=" + this.fieldMapper + "\n"
        + "\tfieldFilter=" + this.fieldFilter + "\n"
        + "\tsourceFileGenerator=" + this.sourceFileGenerator + "]";
  }

}
