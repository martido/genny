Genny is a simple library for generating Java classes out of text files. I'm using it mainly with 
plain old property files to avoid having to work with strings.

For example a resource bundle containing the following key/value pairs ...

    buzz.says=To infitity and beyond!
    pinky.says=Why? What are we going to do tomorrow night?
    brain.says=The same thing we do every night, Pinky - try to take over the world!

... would be converted to a Java class like this (by default):

    package com.example;

    /**
     * This class is generated. DO NOT MODIFY! 
     */
    public class Resources {    
      public static final String buzz_says = "buzz.says"; 
      public static final String pinky_says = "pinky.says"; 
      public static final String brain_says = "brain.says";
    }

Genny provides several ways to customize the generated class:

* A `FieldProvider` provides Genny with the data for each field of the generated class. Genny comes 
  with a ready-to-use `PropertyFileProvider` to work with plain old property files, but for example 
  you could also write your own `FieldProvider` that would read data from a custom XML file.
* Fields may be transformed using a `FieldMapper`. In the example above all dots have been replaced 
  with underscores to transform field names to valid Java identifiers.
* Fields may be filtered using a `FieldFilter`, e.g. you could exlude certain fields.
* Genny relies on an external template engine to generate source files. Currently there are 
  `SourceFileGenerator`s based on StringTemplate and Apache Velocity. The default templates generate 
  code as seen above, but you can also provide your own template. 

For some examples of how to use Genny have a look at the [example][1] project.

[1]: https://github.com/martido/genny/tree/master/example
