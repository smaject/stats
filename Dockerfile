FROM smaject/wildfly
MAINTAINER smaject.com

COPY ./target/stats.war ${DEPLOYMENT_DIR}