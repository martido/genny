package de.martido.genny.maven;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import de.martido.genny.GeneratorDefinition;
import de.martido.genny.Genny;
import de.martido.genny.GennyConfiguration;
import de.martido.genny.provider.PropertyFileProvider;

/**
 * @goal genny
 * @requiresDependencyResolution runtime
 * @configurator include-project-dependencies
 * @author Martin Dobmeier
 */
public class GennyMojo extends AbstractMojo {

  /**
   * The target class to generate.
   * 
   * @parameter expression="${targetClass}"
   */
  private String targetClass;

  /**
   * The base directory of {@link #targetClass}.
   * 
   * @parameter expression="${baseDirectory}"
   */
  private String baseDirectory;

  /**
   * A list of property files from which to generate the {@link #targetClass}.
   * 
   * @parameter expression="${propertyFiles}"
   */
  private FileSet propertyFiles;

  /**
   * An optional fully qualified class name of a {@link GennyConfiguration}.
   * 
   * @parameter expression="${configurationClass}"
   */
  private String configurationClass;

  /**
   * Turns verbose logging on/off.
   * 
   * @parameter expression="${verbose}"
   */
  private boolean verbose;

  /**
   * Visible for testing.
   */
  protected boolean isValid() {
    if (this.targetClass == null ||
        this.baseDirectory == null ||
        this.propertyFiles.getIncludes().isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    if (!this.isValid()) {
      throw new MojoExecutionException(
          "You have to specify the 'targetClass' and 'baseDirectory' attributes " +
              "as well as a set of source files. If you need custom behaviour, " +
              "implement a GennyConfiguration and add the 'configurationClass' attribute");
    }

    GennyConfiguration conf;
    if (this.configurationClass != null) {
      conf = this.createConfiguration(this.configurationClass);
    }
    else {
      conf = this.createConfiguration();
    }

    try {
      GeneratorDefinition def = new GeneratorDefinition(
          this.targetClass, this.baseDirectory, this.propertyFiles.getIncludes());
      new Genny(this.verbose).generateFrom(conf, def);
    } catch (Exception ex) {
      throw new MojoExecutionException("", ex);
    }
  }

  /**
   * Reflectively creates an instance of {@code className} which must be assignable to a reference
   * of type {@link GennyConfiguration}.
   * 
   * @param className
   *          <i>mandatory</i> - the name of the Java class to instantiate.
   * @return A {@link GennyConfiguration}.
   * @throws MojoExecutionException
   *           If the configuration class could not be created.
   */
  private GennyConfiguration createConfiguration(String className) throws MojoExecutionException {

    try {
      Class<?> clazz = Class.forName(className);
      if (!GennyConfiguration.class.isAssignableFrom(clazz)) {
        throw new MojoExecutionException("A configuration class must implement "
            + GennyConfiguration.class.getSimpleName());
      }
      return (GennyConfiguration) clazz.newInstance();
    } catch (Exception ex) {
      throw new MojoExecutionException("", ex);
    }
  }

  /**
   * Creates a default {@link GennyConfiguration}.
   * 
   * @return A {@link GennyConfiguration}.
   */
  private GennyConfiguration createConfiguration() {

    return new GennyConfiguration() {
      public GeneratorDefinition configure(GeneratorDefinition def) {
        def.setFieldProvider(PropertyFileProvider.forFiles(def.getInputFiles()).build());
        return def;
      }
    };
  }

}
