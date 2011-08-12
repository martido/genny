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

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import de.martido.genny.Field;
import de.martido.genny.FieldFilter;
import de.martido.genny.FieldMapper;
import de.martido.genny.FieldProvider;
import de.martido.genny.SourceFileGenerator;

/**
 * An implementation of {@link SourceFileGenerator} that uses the CodeModel library from the JAXB
 * project to generate Java source files.
 * 
 * @author Martin Dobmeier
 */
public class CodeModelSourceFileGenerator extends AbstractSourceFileGenerator {

  @Override
  public void generate(TargetDefinition targetDefinition,
      FieldProvider fieldProvider,
      FieldMapper fieldMapper,
      FieldFilter fieldFilter)
      throws Exception {

    JCodeModel cm = new JCodeModel();
    JDefinedClass _class = cm._class(targetDefinition.getFullyQualifiedName());

    // Create some JavaDoc.
    JDocComment doc = _class.javadoc();
    doc.append("This class is generated. DO NOT MODIFY!");

    for (Field f : fieldProvider.provide(fieldFilter)) {
      System.out.println("Creating constant for: " + f);
      Field mapped = fieldMapper.map(f);
      this.addField(_class, mapped.getName(), mapped.getValue());
    }

    // Add a private/final "key" field.
    JFieldVar field = _class.field(JMod.PRIVATE | JMod.FINAL, String.class, "key");

    // Add a constructor to initialze the field "key".
    JMethod constructor = _class.constructor(JMod.PRIVATE);
    JVar param = constructor.param(JMod.NONE, String.class, "key");
    JBlock body = constructor.body();
    body.assign(JExpr.refthis("key"), param);

    // Add a "get()" method.
    JMethod method = _class.method(JMod.PUBLIC, String.class, "get");
    body = method.body();
    body._return(field);

    // Add a "toString()" method.
    method = _class.method(JMod.PUBLIC, String.class, "toString");
    method.annotate(Override.class);
    body = method.body();
    body._return(JExpr.refthis("key"));

    // Generate the Java source file.
    File file = this.createFile(targetDefinition);
    cm.build(file);
  }

  private void addField(JDefinedClass _class, String name, Object fieldValue) {
    JInvocation invoc = JExpr._new(_class);
    invoc.arg(fieldValue.toString());
    _class.field(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, _class, name, invoc);
  }

  @Override
  public String toString() {
    return "CodeModelSourceFileGenerator";
  }

}
