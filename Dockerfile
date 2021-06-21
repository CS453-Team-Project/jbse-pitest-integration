FROM ubuntu:18.04

# Install development packages
ARG DEBIAN_FRONTEND=noninteractive
RUN apt-get update -y && \
    apt-get install -y --no-install-recommends \
    git unzip ca-certificates \
    build-essential zlib1g-dev \
    libncurses5-dev libgdbm-dev libnss3-dev \
    libssl-dev libreadline-dev libffi-dev wget

WORKDIR /root

# JAVA
RUN wget -O openJdk.tar.gz https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u292-b10/OpenJDK8U-jdk_x64_linux_hotspot_8u292b10.tar.gz && \
    mkdir openJdk8 && \
    tar -xvzf ./openJdk.tar.gz -C ./openJdk8 --strip-components 1 && \
    ln -s /root/openJdk8 /usr/lib/java && \
    rm ~/openJdk.tar.gz

# Python
RUN wget -O python3.9.tar.gz https://www.python.org/ftp/python/3.9.4/Python-3.9.4.tgz && \
    tar -xvzf ./python3.9.tar.gz && \
    cd $(ls | grep Python) && \
    ./configure && \
    make && \
    make install && \
    rm ~/python3.9.tar.gz

# MAVEN
RUN wget -O maven.tar.gz https://mirror.navercorp.com/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz && \
    tar -xvzf ./maven.tar.gz && \
    mv ./$(ls | grep apache-maven) /opt && \
    rm ~/maven.tar.gz

# Z3
RUN wget -O z3.zip https://github.com/Z3Prover/z3/releases/download/z3-4.8.10/z3-4.8.10-x64-ubuntu-18.04.zip && \
    unzip z3.zip && \
    rm ~/z3.zip && \
    mkdir -p /opt/local/bin && \
    ln -s /root/$(ls | grep z3)/bin/z3 /opt/local/bin/z3

# Python Z3
RUN python3 -m pip install z3 z3-solver

# ENV
ENV JAVA_HOME /usr/lib/java
ENV PATH /opt/apache-maven-3.8.1/bin:$JAVA_HOME/bin:$PATH

# Source code
RUN git clone https://github.com/CS453-Team-Project/jbse-pitest-integration
RUN cd jbse-pitest-integration && git clone https://github.com/CS453-Team-Project/parse-jbse-output.git

ENV CS453_PROJECT_HOME /root/jbse-pitest-integration

RUN echo 'alias mytool="java -cp target/classes:$CS453_PROJECT_HOME/target/classes:$CS453_PROJECT_HOME/res/javassist.jar:$CS453_PROJECT_HOME/res/jbse-0.10.0-SNAPSHOT-shaded.jar:$CS453_PROJECT_HOME/res/picocli-4.6.1.jar:$CS453_PROJECT_HOME/res/asm-all-3.3.1.jar:$CS453_PROJECT_HOME/res/json-20210307.jar com.cs453.group5.symbolic.SymMain"' >> .bashrc
RUN cd jbse-pitest-integration && mvn test