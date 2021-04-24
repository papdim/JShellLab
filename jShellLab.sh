
export LD_LIBRARY_PATH=$PWD/lib:$PWD/extraJarsForJShellClasspath/\*:$LD_LIBRARY_PATH
java   -cp .:$PWD/extraJarsForJShellClasspath/\* -XX:+UseNUMA -XX:+UseParallelGC -XX:+UseCompressedOops    -Xss5m -Xms2000m -Xmx25000m -jar jShellLab.jar
