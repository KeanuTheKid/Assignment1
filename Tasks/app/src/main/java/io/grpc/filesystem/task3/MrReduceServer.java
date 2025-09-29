/*
 * gRPC server node to accept calls from the clients and serve based on the method that has been requested
 */

package io.grpc.filesystem.task3;

import com.task3.proto.AssignJobGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.filesystem.task2.MapReduce;

import com.task3.proto.ReduceInput;
import com.task3.proto.ReduceOutput;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MrReduceServer {

    private Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        final MrReduceServer mrServer = new MrReduceServer();
        for (String i : args) {
            mrServer.start(Integer.parseInt(i));
        }
        mrServer.server.awaitTermination();
    }

    private void start(int port) throws IOException {
        server = ServerBuilder.forPort(port).addService(new MrReduceServerImpl()).build().start();
        System.out.println("Listening on: " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Terminating the server at port: " + port);
            try {
                server.shutdown().awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }));
    }

    static class MrReduceServerImpl extends AssignJobGrpc.AssignJobImplBase {

        @Override
        public void reduce(ReduceInput request, StreamObserver<ReduceOutput> responseObserver) {
            try {
                // Extract paths from client request
                String inputDirPath = request.getInputfilepath();
                String outputFilePath = request.getOutputfilepath();

                System.out.println("[ReduceServer] Received reduce request:\n  inputDir=" + inputDirPath + "\n  outputFile=" + outputFilePath);

                // Perform reduce using Task 2 implementation
                MapReduce.reduce(inputDirPath, outputFilePath);

                System.out.println("[ReduceServer] Reduce finished successfully.");

                // Reply success (2 = completed)
                ReduceOutput out = ReduceOutput.newBuilder()
                        .setJobstatus(2)
                        .build();
                responseObserver.onNext(out);
                responseObserver.onCompleted();
            } catch (Exception e) {
                System.err.println("[ReduceServer] Reduce failed: " + e.getMessage());
                // Return failure status instead of onError to keep client-side clean
                ReduceOutput out = ReduceOutput.newBuilder()
                        .setJobstatus(1)
                        .build();
                responseObserver.onNext(out);
                responseObserver.onCompleted();
            }
        }

    }
}
