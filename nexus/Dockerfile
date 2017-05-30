#
# Creates a docker container with Nexus OSS
#

FROM centos:7

MAINTAINER Steffen Elste <steffen.elste@gmail.com>

LABEL vendor=Sonatype \
      description="Nexus Repository OSS image"

ARG NEXUS_VERSION=3.3.1-01
ARG NEXUS_DOWNLOAD_URL=https://download.sonatype.com/nexus/3/nexus-${NEXUS_VERSION}-unix.tar.gz

ENV SONATYPE_WORK /sonatype-work
ENV NEXUS_VERSION 3.3.1-01
ENV NEXUS_HOME /opt/sonatype/nexus/

RUN INSTALL_PKGS="java-1.8.0-openjdk.x86_64" && \
    yum -y --setopt=tsflags=nodocs install $INSTALL_PKGS && \
    rpm -V $INSTALL_PKGS && \
    yum clean all  && \
    localedef -f UTF-8 -i en_US en_US.UTF-8

# Install packages necessary
RUN yum install -y \
    curl tar \
    && yum clean all

# Configure nexus runtime
ENV SONATYPE_DIR=/opt/sonatype
ENV NEXUS_HOME=${SONATYPE_DIR}/nexus \
    NEXUS_DATA=/nexus-data \
    NEXUS_CONTEXT='' \
    SONATYPE_WORK=${SONATYPE_DIR}/sonatype-work

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

RUN useradd -r -u 200 -m -c "nexus role account" -d ${NEXUS_DATA} -s /bin/false nexus \
    && mkdir -p ${NEXUS_DATA}/etc ${NEXUS_DATA}/log ${NEXUS_DATA}/tmp ${SONATYPE_WORK} \
    && ln -s ${NEXUS_DATA} ${SONATYPE_WORK}/nexus3 \
    && chown -R nexus:nexus ${NEXUS_DATA}

VOLUME ${NEXUS_DATA}

EXPOSE 8081
USER nexus
WORKDIR ${NEXUS_HOME}

ENV INSTALL4J_ADD_VM_PARAMS="-Xms1200m -Xmx1200m"

CMD ["bin/nexus", "run"]
