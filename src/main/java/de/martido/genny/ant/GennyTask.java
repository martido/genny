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
package de.martido.genny.ant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import de.martido.genny.Generator;
import de.martido.genny.GeneratorDefinition;
import de.martido.genny.GennyConfiguration;
import de.martido.genny.provider.PropertyFileProvider;

/**
 * An Ant task for Genny.
 * 
 * @author Martin Dobmeier
 */
public class GennyTask extends Task {

  /** The target class to generate. */
  private String targetClass;

  /** The base directory of {@link #targetClass}. */
  private String baseDirectory;

  /** A list of property files from which to generate the {@link #targetClass}. */
  private final List<FileSet> fileSets = new ArrayList<FileSet>();

  /**
   * The fully qualified class name of a {@link GennyConfiguration}. Specifying a configuration
   * class takes precedence over {@code #targetClass}, {@code #baseDirectory} and {@code #fileSets}.
   */
  private String configurationClass;

  /** Turns verbose logging on/off. */
  private boolean verbose;

  public void setTargetClass(String targetClass) {
    this.targetClass = targetClass;
  }

  public void setBaseDirectory(String baseDirectory) {
    this.baseDirectory = baseDirectory;
  }

  public void addFileSet(FileSet fileSet) {
    this.fileSets.add(fileSet);
  }

  public void setConfigurationClass(String configurationClass) {
    this.configurationClass = configurationClass;
  }

  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  /**
   * Visible for testing.
   */
  protected void validate() {

    boolean valid = true;

    if (this.configurationClass == null) {

      if (this.targetClass == null ||
          this.baseDirectory == null ||
          this.fileSets.isEmpty()) {
        valid = false;
      }
    }

    if (!valid) {
      throw new BuildException("You must either provide the name of a configuration class " +
          "or simply specify a 'targetClass', 'baseDirectory' and a set of source property files.");
    }
  }

  /**
   * Reflectively creates an instance of {@code className} which must be assignable to a reference
   * of type {@link GennyConfiguration}.
   * 
   * @param className <i>mandatory</i> - the name of the Java class to instantiate.
   * @return A {@link GennyConfiguration}.
   * @throws BuildException If the configuration class could not be created.
   */
  private GennyConfiguration createConfiguration(String className) throws BuildException {
    try {
      Class<?> clazz = Class.forName(className);
      if (!GennyConfiguration.class.isAssignableFrom(clazz)) {
        throw new BuildException("A configuration class must implement "
            + GennyConfiguration.class.getSimpleName());
      }
      return (GennyConfiguration) clazz.newInstance();
    } catch (Exception ex) {
      throw new BuildException(ex);
    }
  }

  /**
   * Creates a {@link GennyConfiguration} using {@code #targetClass}, {@code #baseDirectory} and
   * {@code #fileSets}.
   * 
   * @return A {@link GennyConfiguration}.
   */
  private GennyConfiguration createConfiguration() {

    final List<String> fileNames = new ArrayList<String>();
    for (FileSet fs : this.fileSets) {
      DirectoryScanner ds = fs.getDirectoryScanner(this.getProject());
      for (String fileName : ds.getIncludedFiles()) {
        fileNames.add(fileName);
      }
    }

    return new GennyConfiguration() {
      @Override
      public List<GeneratorDefinition> configure() {
        GeneratorDefinition def = new GeneratorDefinition();
        def.setTargetClass(GennyTask.this.targetClass);
        def.setBaseDirectory(GennyTask.this.baseDirectory);
        def.setFieldProvider(PropertyFileProvider.forFiles(
            fileNames.toArray(new String[fileNames.size()])).build());
        return Arrays.asList(def);
      }
    };
  }

  @Override
  public void execute() throws BuildException {

    this.validate();

    GennyConfiguration conf;
    if (this.configurationClass != null) {
      conf = this.createConfiguration(this.configurationClass);
    }
    else {
      conf = this.createConfiguration();
    }

    try {
      new Generator(this.verbose).generateFrom(conf);
    } catch (Exception ex) {
      throw new BuildException(ex);
    }
  }

}
