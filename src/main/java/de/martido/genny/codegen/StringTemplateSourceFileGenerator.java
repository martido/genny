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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.stringtemplate.language.DefaultTemplateLexer;

import de.martido.genny.Field;
import de.martido.genny.FieldFilter;
import de.martido.genny.FieldMapper;
import de.martido.genny.FieldProvider;
import de.martido.genny.SourceFileGenerator;

/**
 * An implementation of {@link SourceFileGenerator} that uses the StringTemplate library to generate
 * Java source files from a template. This implementation assumes the "Template-group-file mode" as
 * described in the StringTemplate documentation, i.e. templates are defined within a single
 * template group file.
 * <p>
 * By default the generator uses the template {@code stringTemplateDefault.stg}. However, callers
 * can provide their own template in the constructor. If doing so, keep in mind that resources are
 * loaded from the classpath. This implementation assumes the presence of the following variables:
 * <ul>
 * <li>The template containg the class definition must be called "classDefinition".</li>
 * <li>$packageName$: The name of the package that the generated Java class resides in.</li>
 * <li>$className$: The name of the generated Java class.</li>
 * <li>$fields$: The fields of the generated Java class.</li>
 * </ul>
 * 
 * @author Martin Dobmeier
 */
public class StringTemplateSourceFileGenerator extends AbstractSourceFileGenerator {

  /**
   * The name of a template group file.
   */
  private final String templateGroupFileName;

  public StringTemplateSourceFileGenerator() {
    this.templateGroupFileName = "stringTemplateDefault.stg";
  }

  public StringTemplateSourceFileGenerator(String templateGroupFileName) {
    this.templateGroupFileName = templateGroupFileName;
  }

  @Override
  public void generate(SourceFile sourceFile,
      FieldProvider fieldProvider,
      FieldMapper fieldMapper,
      FieldFilter fieldFilter)
      throws Exception {

    Reader reader = null;
    StringTemplate template = null;
    boolean thrown = false;
    try {

      ClassLoader classLoader = this.getClass().getClassLoader();
      InputStream in = classLoader.getResourceAsStream(this.templateGroupFileName);
      reader = new InputStreamReader(in, Charset.forName("UTF-8"));

      StringTemplateGroup templateGroup = new StringTemplateGroup( //
          reader, DefaultTemplateLexer.class);

      List<Field> fields = this.applyFieldMapper(fieldProvider, fieldMapper, fieldFilter);

      template = templateGroup.getInstanceOf("classDefinition");
      template.setAttribute("packageName", sourceFile.getPackageName());
      template.setAttribute("className", sourceFile.getSimpleName());
      template.setAttribute("fields", fields);

    } catch (Exception ex) {
      thrown = true;
      throw ex;
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException ex) {
        if (thrown) {
          ex.printStackTrace(); // Propagate original exception.
        } else {
          throw ex;
        }
      }
    }

    Writer writer = null;
    try {

      File file = this.createFile(sourceFile);
      FileOutputStream os = new FileOutputStream(file, false);
      writer = new OutputStreamWriter(os, Charset.forName("UTF-8"));
      writer.append(template.toString());
      writer.flush();

    } catch (Exception ex) {
      thrown = true;
      throw ex;
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException ex) {
        if (thrown) {
          ex.printStackTrace(); // Propagate original exception.
        } else {
          throw ex;
        }
      }
    }
  }

  private List<Field> applyFieldMapper(
      FieldProvider fieldProvider,
      FieldMapper fieldMapper,
      FieldFilter fieldFilter)
      throws Exception {

    List<Field> source = fieldProvider.provide(fieldFilter);
    List<Field> res = new ArrayList<Field>(source.size());

    for (Field f : source) {
      res.add(fieldMapper.map(f));
    }

    return res;
  }

  @Override
  public String toString() {
    return "StringTemplateSourceFileGenerator ["
        + "templateGroupFileName=" + this.templateGroupFileName + "]";
  }

}
