#!/bin/bash

# Compile the code
mpic++ matmul.cpp -o matmul.out

# Output file
output_file="test_results.log"

# Argument parsing for number of experiments and cluster mode
n=1
cluster=false

while getopts ":n:c" opt; do
  case $opt in
    n) n="$OPTARG";;
    c) cluster=true;;
    \?) echo "Invalid option -$OPTARG" >&2;;
  esac
done

if $cluster; then
    echo "Running in CLUSTER mode"
else
    echo "Running in LOCAL mode"
fi
echo "Number of experiments: $n"

# Clean the output file
> $output_file

# Run the entire experiment for n times
for exp in $(seq 1 $n); do
    echo "Experiment run: $exp"
    
    # Print table header for console output
    echo "-----------------------------"
    echo "| np  | Distributed |   BF   |"
    echo "-----------------------------"

    # Run tests for np = 1, 2, 4, ..., 32
    for np in 1 2 4 8 16 32; do
        if $cluster; then
            mpi_command="mpirun -np $np --oversubscribe --allow-run-as-root --host localhost,nodeB ./matmul.out"
        else
            mpi_command="mpirun -np $np --oversubscribe --allow-run-as-root ./matmul.out"
        fi
        result=$($mpi_command 2>&1)
        np_space=$(printf "%-4s" $np)
        distributed_time=$(echo $result | awk '{print $2}')
        bf_time=$(echo $result | awk '{print $3}')
        distributed_space=$(printf "%-10s" $distributed_time)
        bf_space=$(printf "%-4s" $bf_time)
        echo "| $np_space | $distributed_space | $bf_space |"
        echo "-----------------------------"
        
        # If it's the first run, log the results
        if [ "$exp" -eq 1 ]; then
            echo "$np $distributed_time $bf_time" >> $output_file
        fi

        sleep 0.2
    done
done

echo "Tests completed!"
