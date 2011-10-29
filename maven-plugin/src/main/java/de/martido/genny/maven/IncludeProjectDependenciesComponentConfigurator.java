package de.martido.genny.maven;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.configurator.AbstractComponentConfigurator;
import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.composite.ObjectWithFieldsConverter;
import org.codehaus.plexus.component.configurator.converters.special.ClassRealmConverter;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;

/**
 * A custom {@code ComponentConfigurator} which adds the project's runtime classpath elements to the
 * plugin's classpath. To activate it add {@code @configurator include-project-dependencies} to your
 * Mojo.
 * 
 * @see http://goo.gl/oPLKe (Stackoverflow)
 * @plexus.component role="org.codehaus.plexus.component.configurator.ComponentConfigurator"
 *                   role-hint="include-project-dependencies"
 * @plexus.requirement 
 *                     role="org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup"
 *                     role-hint="default"
 */
public class IncludeProjectDependenciesComponentConfigurator extends AbstractComponentConfigurator {

  @Override
  public void configureComponent(
      Object component,
      PlexusConfiguration configuration,
      ExpressionEvaluator expressionEvaluator,
      ClassRealm containerRealm,
      ConfigurationListener listener)
      throws ComponentConfigurationException {

    this.addProjectDependenciesToClassRealm(expressionEvaluator, containerRealm);

    this.converterLookup.registerConverter(new ClassRealmConverter(containerRealm));

    ObjectWithFieldsConverter converter = new ObjectWithFieldsConverter();

    converter.processConfiguration(
        this.converterLookup,
        component,
        containerRealm.getParentClassLoader(),
        configuration,
        expressionEvaluator,
        listener);
  }

  @SuppressWarnings("unchecked")
  private void addProjectDependenciesToClassRealm(
      ExpressionEvaluator expressionEvaluator,
      ClassRealm containerRealm)
      throws ComponentConfigurationException {

    List<String> runtimeClasspathElements;
    try {
      runtimeClasspathElements = (List<String>) expressionEvaluator
          .evaluate("${project.runtimeClasspathElements}");
    } catch (ExpressionEvaluationException ex) {
      throw new ComponentConfigurationException(
          "Could not evaluate ${project.runtimeClasspathElements}", ex);
    }

    // Add the project's dependencies to the ClassRealm.
    List<URL> urls = this.buildURLs(runtimeClasspathElements);
    for (URL url : urls) {
      containerRealm.addURL(url);
    }
  }

  private List<URL> buildURLs(List<String> runtimeClasspathElements)
      throws ComponentConfigurationException {

    List<URL> urls = new ArrayList<URL>(runtimeClasspathElements.size());

    for (String element : runtimeClasspathElements) {
      try {
        URL url = new File(element).toURI().toURL();
        System.out.println("Adding " + url + " to project class loader");
        urls.add(url);
      } catch (MalformedURLException ex) {
        throw new ComponentConfigurationException(
            "Could not access project dependency: " + element, ex);
      }
    }

    return urls;
  }

}
