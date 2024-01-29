#!/bin/bash
# Compile the project using Maven
# mvn compile

# Determine the role based on the argument
case $1 in
    "registry")
        role=0
        ;;
    "server")
        role=1
        ;;
    "client")
        role=2
        ;;
    *)
        echo "Invalid role: $1"
        echo "Valid roles are: registry, server, client"
        exit 1
        ;;
esac

#sleep for 1s


# Run the application with the specified role
java -cp target/classes myrmi.tests.RMITest $role