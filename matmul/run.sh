#!/bin/bash

# Compile the code
mpic++ matmul.cpp -o matmul.out

output_file="test_results.txt"

echo "" > $output_file

echo "-----------------------------"
echo "| np  | Distributed |   BF   |"
echo "-----------------------------"

for np in 1 2 4 8 16 32; do
    result=$(mpirun -np $np --oversubscribe ./matmul.out 2>&1)
    np_space=$(printf "%-4s" $np)
    distributed_time=$(echo $result | awk '{print $2}')
    bf_time=$(echo $result | awk '{print $3}')
    distributed_space=$(printf "%-10s" $distributed_time)
    bf_space=$(printf "%-4s" $bf_time)

    # For console display in table format
    echo "| $np_space | $distributed_space | $bf_space |"
    echo "-----------------------------"

    # For log file in simpler format
    echo "$np $distributed_time $bf_time" >> $output_file
done

echo "Tests completed!"
