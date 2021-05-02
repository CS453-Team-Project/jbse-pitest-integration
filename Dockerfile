FROM ubuntu:18.04

# Install development packages
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update -y && \
    apt-get install -y --no-install-recommends \
    git z3 ca-certificates \
    build-essential zlib1g-dev \
    libncurses5-dev libgdbm-dev libnss3-dev \
    libssl-dev libreadline-dev libffi-dev wget

WORKDIR /root

# JAVA
RUN wget -O openJdk.tar.gz https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u292-b10/OpenJDK8U-jdk_x64_linux_hotspot_8u292b10.tar.gz && \
    mkdir openJdk8 && \
    tar -xvzf ./openJdk.tar.gz -C ./openJdk8 --strip-components 1 && \
    ln -s /root/openJdk8 /usr/lib/java
ENV JAVA_HOME /usr/lib/java
ENV PATH $JAVA_HOME/bin:$PATH

# Python
RUN wget -O python3.9.tar.gz https://www.python.org/ftp/python/3.9.4/Python-3.9.4.tgz && \
    tar -xvzf ./python3.9.tar.gz && \
    cd $(ls | grep Python) && \
    ./configure && \
    make && \
    make install

# Source code
RUN git clone https://github.com/CS453-Team-Project/jbse-pitest-integration

# MAVEN
RUN wget -O maven.tar.gz https://mirror.navercorp.com/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz && \
    mkdir maven && \
    tar -xvzf ./maven.tar.gz && \
    mv ./$(ls | grep apache-maven) /opt
ENV PATH /opt/$(ls /opt | grep apache-maven)/bin:$PATH

# Z3 symlink
RUN mkdir -p /opt/local/bin && \
    ln -s $(which z3) /opt/local/bin/z3

# JBSE
RUN git clone https://github.com/pietrobraione/jbse.git ~/jbse && \
    cd jbse && \
    ./gradlew build && \
    cp ./build/libs/jbse-0.10.0-SNAPSHOT-shaded.jar ~/jbse-pitest-integration//src/main/java/com/cs453/group5/examples
