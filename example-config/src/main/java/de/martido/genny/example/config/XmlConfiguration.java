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
package de.martido.genny.example.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.DefaultHandler;

import de.martido.genny.Field;
import de.martido.genny.FieldFilter;
import de.martido.genny.FieldProvider;
import de.martido.genny.GeneratorDefinition;
import de.martido.genny.GennyConfiguration;

public class XmlConfiguration implements GennyConfiguration {

  @Override
  public GeneratorDefinition configure(GeneratorDefinition def) {
    def.setFieldProvider(new XmlProvider(def.getInputFiles().get(0)));
    return def;
  }

  /**
   * A custom {@link FieldProvider} that gets its data from a XML file.
   */
  public static class XmlProvider extends DefaultHandler implements FieldProvider {

    private final String xmlFileName;

    public XmlProvider(String xmlFileName) {
      this.xmlFileName = xmlFileName;
    }

    @Override
    public List<Field> provide(FieldFilter fieldFilter) throws Exception {

      InputStream in = null;
      try {
        in = new FileInputStream(new File(this.xmlFileName));
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document document = builder.parse(in);
        Element root = document.getDocumentElement();
        return this.parseConfig(root, fieldFilter);
      } finally {
        if (in != null) {
          in.close();
        }
      }
    }

    private List<Field> parseConfig(Element root, FieldFilter fieldFilter) {

      List<Field> res = new ArrayList<Field>();
      NodeList children = root.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        Node child = children.item(i);
        if (child instanceof Element) {
          Element element = (Element) child;
          if (element.getNodeName().equals("entry")) {
            Field f = this.parseEntry(element);
            if (fieldFilter.include(f)) {
              res.add(f);
            }
          }
        }
      }
      return res;
    }

    private Field parseEntry(Element element) {
      String name = element.getAttribute("key");
      String value = element.getTextContent();
      return new Field(name, value, null);
    }

  }

}
