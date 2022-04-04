
export LD_LIBRARY_PATH=$PWD/lib:$LD_LIBRARY_PATH

java   -XX:+UseNUMA -XX:+UseParallelGC -XX:+UseCompressedOops    -Xss5m -Xms2000m -Xmx25000m -jar jShellLab-all.jar
