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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import de.martido.genny.Field;
import de.martido.genny.FieldFilter;
import de.martido.genny.FieldMapper;
import de.martido.genny.FieldProvider;
import de.martido.genny.SourceFile;
import de.martido.genny.SourceFileGenerator;

/**
 * A very simple implementation of {@link SourceFileGenerator} that creates a Java class using a
 * StringBuilder; used as default generator if not otherwise specified.
 * 
 * @author Martin Dobmeier
 */
public class SimpleSourceFileGenerator extends AbstractSourceFileGenerator {

  @Override
  public void generate(SourceFile sourceFile,
      FieldProvider fieldProvider,
      FieldMapper fieldMapper,
      FieldFilter fieldFilter)
      throws Exception {

    StringBuilder sb = new StringBuilder();

    if (!sourceFile.isDefaultPackage()) {
      sb.append("package " + sourceFile.getPackageName() + ";\n\n");
    }

    sb.append("/**\n");
    sb.append(" * This class is generated. DO NOT MODIFY!\n");
    sb.append(" */\n");

    sb.append("public class " + sourceFile.getSimpleName() + "{\n");
    sb.append("\n");
    for (Field f : this.getFields(fieldProvider, fieldMapper, fieldFilter)) {
      sb.append("\tpublic static final String " + f.getName() + " = \"" + f.getValue() + "\";\n");
    }
    sb.append("\n");
    sb.append("}\n");

    Writer writer = null;
    boolean thrown = false;
    try {

      File file = this.createFile(sourceFile);
      FileOutputStream os = new FileOutputStream(file, false);
      writer = new OutputStreamWriter(os, Charset.forName("UTF-8"));
      writer.append(sb.toString());
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

  @Override
  public String toString() {
    return "SimpleSourceFileGenerator";
  }

}
