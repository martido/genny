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
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.junit.Assert;
import org.junit.runners.Parameterized.Parameters;

import de.martido.genny.Generator;
import de.martido.genny.GeneratorDefinition;
import de.martido.genny.GennyConfiguration;

/**
 * @author Martin Dobmeier
 */
public abstract class AbstractTestCase {

  protected static final String BASE_DIRECTORY = "src/test/generated";

  /** The {@code TemplateEngine} to use for this test. */
  protected final TemplateEngine templateEngine;

  protected AbstractTestCase(TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  @Parameters
  public static List<Object[]> data() {
    List<Object[]> data = new ArrayList<Object[]>();
    data.add(new Object[] { TemplateEngine.STRINGTEMPLATE });
    data.add(new Object[] { TemplateEngine.VELOCITY });
    return data;
  }

  protected void generate(GeneratorDefinition def) {
    this.generate(def, null);
  }

  protected void generate(final GeneratorDefinition def, String template) {

    def.setSourceFileGenerator(this.templateEngine.getSourceFileGenerator(template));

    GennyConfiguration module = new GennyConfiguration() {
      @Override
      public List<GeneratorDefinition> configure() {
        return Arrays.asList(def);
      }
    };

    try {
      new Generator().generateFrom(module);
    } catch (Exception ex) {
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
    }
  }

  protected void assertField(Object obj, String fieldName, Object fieldValue) {
    try {
      Assert.assertEquals(obj.getClass().getField(fieldName).get(obj), fieldValue);
    } catch (Exception ex) {
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
    }
  }

  /**
   * A custom {@code URLClassLoader} to load and create the newly generated classes from.
   */
  private static final URLClassLoader CLASSLOADER;

  /*
   * The creation of the custom classloader is done in a static initializer block to handle a
   * possible MalformedURLException. In that case an ExceptionInInitializerError is thrown.
   */
  static {
    try {
      CLASSLOADER = new URLClassLoader(new URL[] { new File(BASE_DIRECTORY).toURI().toURL() });
    } catch (MalformedURLException ex) {
      throw new ExceptionInInitializerError(ex);
    }
  }

  /**
   * Creates an instance of the class that was generated based on the given
   * {@code GeneratorDefinition}.
   * 
   * @param def
   *          The {@code GeneratorDefinition}.
   * @return An {@code Object}.
   */
  protected Object getInstanceOfGeneratedClass(GeneratorDefinition def) {
    try {
      Class<?> clazz = this.compileGeneratedClass(BASE_DIRECTORY, def.getTargetClass());
      Constructor<?> constructor = clazz.getConstructor();
      return constructor.newInstance();
    } catch (Exception ex) {
      ex.printStackTrace();
      Assert.fail(ex.getMessage());
      return null;
    }
  }

  /**
   * Compiles a Java source file described by a base directory and a class name using the JSR-199
   * Compiler API and creates an instance using the {@code #CLASSLOADER}.
   * 
   * @param baseDir
   *          The base directory where the Java source file resides.
   * @param className
   *          The class name of the Java source file.
   * @return A {@code Class} instance.
   * @throws IOException
   *           If an I/O error occured.
   * @throws ClassNotFoundException
   *           If the class was not found.
   */
  private Class<?> compileGeneratedClass(String baseDir, String className) //
      throws IOException, ClassNotFoundException {

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

    StandardJavaFileManager fileManager = null;

    try {

      File source = new File(baseDir + "/" + className.replace('.', '/') + ".java");
      List<File> sources = Arrays.asList(source);

      fileManager = compiler.getStandardFileManager(null, null, Charset.forName("UTF-8"));
      Iterable<? extends JavaFileObject> files = fileManager.getJavaFileObjectsFromFiles(sources);
      DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
      CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, files);

      Assert.assertTrue("Compilation of class '" + className + "' failed.", task.call());

    } finally {
      if (fileManager != null) {
        fileManager.close();
      }
    }

    return CLASSLOADER.loadClass(className);
  }

}
