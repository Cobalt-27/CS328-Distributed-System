#include <iostream>
#include <vector>
#include <random>
#include <mpi.h>
#include <cassert>

const int MATRIX_SIZE = 8;

using Matrix = std::vector<double>;

Matrix initializeRandomMatrix(int rows, int cols) {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_real_distribution<> dis(0.0, 1.0);
    Matrix m(rows * cols);
    for (int i = 0; i < rows * cols; i++) {
        m[i] = dis(gen);
    }
    return m;
}

Matrix multiply(const Matrix &A, const Matrix &B, int rowsA, int rowsB = MATRIX_SIZE) {
    Matrix result(rowsA * MATRIX_SIZE, 0.0);
    for (int i = 0; i < rowsA; i++) {
        for (int j = 0; j < MATRIX_SIZE; j++) {
            for (int k = 0; k < MATRIX_SIZE; k++) {
                result[i * MATRIX_SIZE + j] += A[i * MATRIX_SIZE + k] * B[k * MATRIX_SIZE + j];
            }
        }
    }
    return result;
}

bool compareMatrices(const Matrix &A, const Matrix &B) {
    for (int i = 0; i < MATRIX_SIZE * MATRIX_SIZE; i++) {
        if (std::abs(A[i] - B[i]) > 1e-9) {
            return false;
        }
    }
    return true;
}

int main(int argc, char** argv) {
    int rank, size;
    MPI_Init(&argc, &argv);
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    MPI_Comm_size(MPI_COMM_WORLD, &size);

    Matrix A(MATRIX_SIZE * MATRIX_SIZE), B(MATRIX_SIZE * MATRIX_SIZE), result(MATRIX_SIZE * MATRIX_SIZE);
    
    if (rank == 0) {
        A = initializeRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
        B = initializeRandomMatrix(MATRIX_SIZE, MATRIX_SIZE);
    }
    
    MPI_Bcast(&B[0], MATRIX_SIZE * MATRIX_SIZE, MPI_DOUBLE, 0, MPI_COMM_WORLD);

    std::vector<int> sendRowCounts(size), displacements(size), sendCounts(size);
    int remainder = MATRIX_SIZE % size;
    int cur_row = 0;
    for (int i = 0; i < size; i++) {
        sendRowCounts[i] = MATRIX_SIZE / size;
        sendCounts[i] = sendRowCounts[i] * MATRIX_SIZE;
        if (remainder > 0) {
            sendRowCounts[i]++;
            remainder--;
        }
        displacements[i] = cur_row * MATRIX_SIZE;
        cur_row += sendRowCounts[i];
    }


    Matrix localA(sendRowCounts[rank] * MATRIX_SIZE);
    MPI_Scatterv(&A[0], &sendCounts[0], &displacements[0], MPI_DOUBLE, &localA[0], sendCounts[rank], MPI_DOUBLE, 0, MPI_COMM_WORLD);

    Matrix subResult = multiply(localA, B, sendRowCounts[rank]);


    MPI_Gatherv(&subResult[0], sendCounts[rank], MPI_DOUBLE, &result[0], &sendCounts[0], &displacements[0], MPI_DOUBLE, 0, MPI_COMM_WORLD);

    if (rank == 0) {
        // Check correctness
        Matrix bruteForceResult = multiply(A, B, MATRIX_SIZE);
        bool isEqual = compareMatrices(result, bruteForceResult);
        std::cout << (isEqual ? "Results are identical!" : "Results are different!") << std::endl;
    }

    if (rank == 0) {
        std::cout << size << " " << distributedDuration.count() << " " << bruteForceDuration.count() << std::endl;
    }

    MPI_Finalize();
    return 0;
}
