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

/**
 * The source file that should be generated.
 * 
 * @author Martin Dobmeier
 */
public class SourceFile {

  private static final String SEPARATOR = System.getProperty("file.separator");

  /** The fully qualified name of a Java class. */
  private final String fullyQualifiedName;

  /** The simple name of a Java class. */
  private final String simpleName;

  /** The package in which the Java class called {@code simpleName} resides. */
  private final String packageName;

  /** The base directory of the Java source file. */
  private final String baseDirectory;

  public SourceFile(String fullyQualifiedName, String baseDirectory) {
    this.fullyQualifiedName = fullyQualifiedName;
    int lastDot = fullyQualifiedName.lastIndexOf(".");
    if (lastDot != -1) {
      this.simpleName = fullyQualifiedName.substring(lastDot + 1);
      this.packageName = fullyQualifiedName.substring(0, lastDot);
    }
    else {
      this.simpleName = fullyQualifiedName;
      this.packageName = null;
    }
    this.baseDirectory = baseDirectory;
  }

  public String getFullyQualifiedName() {
    return this.fullyQualifiedName;
  }

  public String getBaseDirectory() {
    return this.baseDirectory;
  }

  public String getSimpleName() {
    return this.simpleName;
  }

  public String getPackageName() {
    return this.packageName;
  }

  /**
   * @return A path based on the {@code baseDirectory} and the {@code fullyQualifiedName} of the
   *         source file. Currently, each source file ends with a ".java" suffix.
   */
  public String asPath() {
    String sourceFilePath = this.getFullyQualifiedName().replace(".", SEPARATOR) + ".java";
    return this.getBaseDirectory() + SEPARATOR + sourceFilePath;
  }

  /**
   * @return {@code True} if target source file is in the default package; {@code false} otherwise.
   */
  public boolean isDefaultPackage() {
    return this.packageName == null;
  }

  @Override
  public String toString() {
    return "TargetDefinition [" //
        + "fullyQualifiedName=" + this.fullyQualifiedName //
        + ", baseDirectory=" + this.baseDirectory + "]";
  }

}
