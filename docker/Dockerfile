FROM openjdk:17-jdk-buster

# Install necessary tools
RUN apt-get update && apt-get install -y ca-certificates-java curl tar

# Download and install Maven
RUN curl -O https://downloads.apache.org/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.tar.gz && \
    tar -xf apache-maven-3.9.4-bin.tar.gz -C /opt/ && \
    rm apache-maven-3.9.4-bin.tar.gz

# Set Maven environment variables
ENV MAVEN_HOME /opt/apache-maven-3.9.4
ENV PATH $MAVEN_HOME/bin:$PATH

# Create and set the working directory in the container
WORKDIR /rmi

# Copy the contents of the MyRMI directory into the container
# Assuming Dockerfile is outside the MyRMI directory
COPY ./MyRMI /rmi

# Expose the necessary port
EXPOSE 11099

RUN cd /rmi && mvn clean compile

# Define entrypoint to run the script with the environment variable
ENTRYPOINT ./run.sh server
#ENTRYPOINT ./run.sh client
#ENTRYPOINT ./run.sh registry
