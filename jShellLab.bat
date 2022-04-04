SET PATH=%PATH%;%cd%\lib\;%cd%\extraJarsForJShellClasspath\*

java   -cp %cd%\extraJarsForJShellClasspath\* -XX:+UseNUMA -XX:+UseParallelGC -XX:+UseCompressedOops    -Xss5m -Xms2000m -Xmx25000m -jar jShellLab.jar
