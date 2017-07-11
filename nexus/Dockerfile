#
# Creates a docker container with Nexus OSS
#

FROM centos:7

MAINTAINER Steffen Elste <steffen.elste@gmail.com>

LABEL vendor=Sonatype \
      description="Nexus Repository OSS image"

ARG NEXUS_VERSION=3.4.0-02
ARG NEXUS_DOWNLOAD_URL=https://download.sonatype.com/nexus/3/nexus-${NEXUS_VERSION}-unix.tar.gz

ENV NEXUS_VERSION 3.4.0-02
ENV SONATYPE_DIR=/opt/sonatype
ENV NEXUS_HOME=${SONATYPE_DIR}/nexus \
    NEXUS_DATA=/nexus-data \
    NEXUS_CONTEXT='' \
    SONATYPE_WORK=${SONATYPE_DIR}/sonatype-work

ENV NEXUS_SSL=${NEXUS_HOME}/etc/ssl
ENV PUBLIC_CERT=${NEXUS_SSL}/cacert.pem \
    PUBLIC_CERT_SUBJ=/CN=localhost \
    PRIVATE_KEY=${NEXUS_SSL}/cakey.pem \
    PRIVATE_KEY_PASSWORD=password

USER root

RUN INSTALL_PKGS="java-1.8.0-openjdk.x86_64" && \
    yum -y --setopt=tsflags=nodocs install $INSTALL_PKGS && \
    rpm -V $INSTALL_PKGS && \
    yum clean all  && \
    localedef -f UTF-8 -i en_US en_US.UTF-8

# Install packages necessary
RUN yum install -y \
    curl tar openssl \
    && yum clean all

# Extract Nexus
RUN mkdir -p ${NEXUS_HOME} \
    && curl --fail --silent --location --retry 3 \
    ${NEXUS_DOWNLOAD_URL} \
    | gunzip \
    | tar x -C ${NEXUS_HOME} --strip-components=1 nexus-${NEXUS_VERSION} \
    && chown -R root:root ${NEXUS_HOME}

# Configure nexus
RUN sed \
    -e '/^nexus-context/ s:$:${NEXUS_CONTEXT}:' \
    -i ${NEXUS_HOME}/etc/nexus-default.properties \
    && sed \
    -e '/^-Xms/d' \
    -e '/^-Xmx/d' \
    -i ${NEXUS_HOME}/bin/nexus.vmoptions

# Create self-signed certificate
RUN openssl req -nodes -new -x509 \
    -keyout ${PRIVATE_KEY} -out ${PUBLIC_CERT} -subj "${PUBLIC_CERT_SUBJ}"
RUN openssl pkcs12 -export -in ${PUBLIC_CERT} -inkey ${PRIVATE_KEY} \
    -out ${NEXUS_SSL}/jetty.key -passout pass:${PRIVATE_KEY_PASSWORD}

# Create new keystore for use with nexus
RUN $JAVA_HOME/bin/keytool -importkeystore -noprompt \
    -deststorepass ${PRIVATE_KEY_PASSWORD} -destkeypass ${PRIVATE_KEY_PASSWORD} \
    -destkeystore ${NEXUS_SSL}/keystore.jks -srckeystore ${NEXUS_SSL}/jetty.key \
    -srcstoretype PKCS12 -srcstorepass ${PRIVATE_KEY_PASSWORD}
RUN sed -r '/<Set name="(KeyStore|KeyManager|TrustStore)Password">/ s:>.*$:>'${PRIVATE_KEY_PASSWORD}'</Set>:' \
    -i ${NEXUS_HOME}/etc/jetty/jetty-https.xml

# Enable https in nexus
RUN sed \
    -e '/^nexus-args/ s:$:,${jetty.etc}/jetty-https.xml:' \
    -e '/^application-port/a application-port-ssl=8443\' \
    -i ${NEXUS_HOME}/etc/nexus-default.properties

RUN useradd -r -u 200 -m -c "nexus role account" -d ${NEXUS_DATA} -s /bin/false nexus \
    && mkdir -p ${NEXUS_DATA}/etc ${NEXUS_DATA}/log ${NEXUS_DATA}/tmp ${SONATYPE_WORK} \
    && ln -s ${NEXUS_DATA} ${SONATYPE_WORK}/nexus3 \
    && chown -R nexus:nexus ${NEXUS_DATA}

VOLUME ${NEXUS_DATA}

# Ports exposed, regular http and https, plus Docker registry
EXPOSE 8081
EXPOSE 8443
EXPOSE 8500
EXPOSE 8501
USER nexus
WORKDIR ${NEXUS_HOME}

ENV INSTALL4J_ADD_VM_PARAMS="-Xms1200m -Xmx1200m"

CMD ["bin/nexus", "run"]
