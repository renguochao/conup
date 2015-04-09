# Something Should Pay Attention When Running Our Samples #

Add your content here.


## Change ip of deployed component to host’s ip ##

For example, in AuthComponent we change auth.composite’s binding ip to host ip:

![https://conup.googlecode.com/svn/wiki/imgs/attentionrunningsamples/1.jpg](https://conup.googlecode.com/svn/wiki/imgs/attentionrunningsamples/1.jpg)

After change binding ip, please run mvn install to install the jar to ${TUSCANY\_HOME}/samples/auth/, then you can change to this folder to run this application.

If you want to update AuthComponent, please run jar in ${TUSCANY\_HOME}/samples/auth/conup-sample-auth.jar.(If you want to update target component, you should make sure that the target component jar is in ${TUSCANY\_HOME}/samples/xx)


## Change ip of conup-sample-configuration-client ##

![https://conup.googlecode.com/svn/wiki/imgs/attentionrunningsamples/2.jpg](https://conup.googlecode.com/svn/wiki/imgs/attentionrunningsamples/2.jpg)

Change target component’s host ip ConfServiceImpl.java
For example, if your target component’s ip is 192.168.2.199, you should change the 10.0.2.15 in following figure to 192.168.2.199, then you run this application then invoke update service.

![https://conup.googlecode.com/svn/wiki/imgs/attentionrunningsamples/3.jpg](https://conup.googlecode.com/svn/wiki/imgs/attentionrunningsamples/3.jpg)