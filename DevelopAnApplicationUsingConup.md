# Download files from google code #

svn checkout https://conup.googlecode.com/svn/tags/conup-0.9.0-DU


# Install from download files #

Run mvn install in home folder of download files like following figure:

![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/1.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/1.jpg)

# Develop an application in eclipse #
Create a maven project

![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/2.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/2.jpg)

Here we choose create a simple project, do not choose archetype, because tuscany’s archetype import so many useless dependency for us. For simplicity, we start from a clean maven project.

![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/3.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/3.jpg)

Next, input group id and artifact id as you like.

![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/4.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/4.jpg)

After this step, you get the following project:

![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/5.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/5.jpg)


  * The first thing is add dependencies into pom.xml

> ![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/6.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/6.jpg)

The artifactId start with “conup” is our conup projects.

  * write the business logic of your component

> ![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/7.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/7.jpg)

In DBService.java, it looks like other Tuscany service declaration.

> ![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/8.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/8.jpg)

In DBServiceImpl.java, we add @ConupTransaction annotation on dbOperation method to indicate that dbOperation is a transaction in our algorithm.

> ![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/9.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/9.jpg)


  * We should add db.composite like other Tuscany application, but we need add our policysets and intent on our service binding.

> ![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/10.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/10.jpg)

Here trace is our defined intent, traceBindingPolicySet is our defined policyset. In the binding uri, you should change the ip to your host computer!

  * We should add definition.xml in META-INF folder

> ![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/11.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/11.jpg)

In definitions.xml, we add our defined intent and policySets like following figure.

> ![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/12.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/12.jpg)

  * Add sca-contribution.xml to src/main/resources folder
This file declares which composite file is deployable, here we should pay attention to the namespace, this namespace and composite name should be the same as defined in your db.composite file. From the following two figures

> ![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/13.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/13.jpg)

> ![https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/14.jpg](https://conup.googlecode.com/svn/wiki/imgs/devanappusingconup/14.jpg)

  * Run mvn install in this project, we can get conup-sample-test.jar in our target folder
  * Using Tuscany.sh conup.sample.test.jar to run this application