package de.martido.genny.maven;

import java.util.Arrays;
import java.util.List;

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
   * The fully qualified class name of a {@link GennyConfiguration}. Specifying a configuration
   * class takes precedence over {@code #targetClass}, {@code #baseDirectory} and {@code #fileSets}.
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
  protected boolean validate() {

    boolean valid = true;

    if (this.configurationClass == null) {

      if (this.targetClass == null ||
          this.baseDirectory == null ||
          this.propertyFiles.getIncludes().isEmpty()) {
        valid = false;
      }
    }

    return valid;
  }

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    if (!this.validate()) {
      throw new MojoExecutionException("You must provide the name of a configuration class "
          + "or specify a 'targetClass', 'baseDirectory' and a set of source property files.");
    }

    GennyConfiguration conf;
    if (this.configurationClass != null) {
      conf = this.createConfiguration(this.configurationClass);
    }
    else {
      conf = this.createConfiguration();
    }

    try {
      new Genny(this.verbose).generateFrom(conf);
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
   * Creates a {@link GennyConfiguration} using {@code #targetClass}, {@code #baseDirectory} and
   * {@code #fileSets}.
   * 
   * @return A {@link GennyConfiguration}.
   */
  private GennyConfiguration createConfiguration() {

    final List<String> fileNames = this.propertyFiles.getIncludes();
    final String[] array = fileNames.toArray(new String[fileNames.size()]);

    return new GennyConfiguration() {

      @Override
      public List<GeneratorDefinition> configure() {
        GeneratorDefinition def = new GeneratorDefinition();
        def.setTargetClass(GennyMojo.this.targetClass);
        def.setBaseDirectory(GennyMojo.this.baseDirectory);

        /**
         * The plugin must be associated with a build phase that is executed prior to 'compile'. The
         * preferred phase would be 'generate-sources'. However, the projects' resources won't be
         * copied to the output directory until 'process-resources' and therefore won't be available
         * to the plugin's classloader. So we could either use that phase together with a custom
         * {@code ComponentConfigurer} or we could just load property files from the file system. We
         * chose the latter.
         */
        def.setFieldProvider(PropertyFileProvider.forFiles(array).fromFileSystem().build());
        return Arrays.asList(def);
      }
    };
  }

}
