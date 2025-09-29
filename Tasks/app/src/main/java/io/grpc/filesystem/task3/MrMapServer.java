/*
 * gRPC server node to accept calls from the clients and serve based on the method that has been requested
 */

package io.grpc.filesystem.task3;

import com.task3.proto.AssignJobGrpc;
import com.task3.proto.MapInput;
import com.task3.proto.MapOutput;
import io.grpc.stub.StreamObserver;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.filesystem.task2.MapReduce;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MrMapServer {

    private Server server;

    public static void main(String[] args) throws IOException, InterruptedException {
        final MrMapServer mrServer = new MrMapServer();
        for (String i : args) {
            mrServer.start(Integer.parseInt(i));
        }
        mrServer.server.awaitTermination();
    }

    private void start(int port) throws IOException {
        server = ServerBuilder.forPort(port).addService(new MrMapServerImpl()).build().start();
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

    static class MrMapServerImpl extends AssignJobGrpc.AssignJobImplBase {
        @Override
        public StreamObserver<MapInput> map(final StreamObserver<MapOutput> responseObserver) {
            // track if any chunk failed
            final java.util.concurrent.atomic.AtomicBoolean failed = new java.util.concurrent.atomic.AtomicBoolean(false);

            return new StreamObserver<MapInput>() {
                @Override
                public void onNext(MapInput request) {
                    try {
                        // process each incoming chunk file path
                        String chunkFilePath = request.getInputfilepath();
                        MapReduce.map(chunkFilePath);
                    } catch (Exception e) {
                        // mark failure but do not respond yet (single response is sent in onCompleted)
                        failed.set(true);
                        System.err.println("Map task failed for chunk: " + request.getInputfilepath() + " -> " + e.getMessage());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    System.err.println("Map stream error: " + t.getMessage());
                    // If the client cancels/errs we simply stop; no response is sent
                }

                @Override
                public void onCompleted() {
                    // Send exactly one response after the client finished sending all MapInput messages
                    MapOutput out = MapOutput.newBuilder()
                            .setJobstatus(failed.get() ? 1 : 2) // 2 = success, 1 = failure
                            .build();
                    responseObserver.onNext(out);
                    responseObserver.onCompleted();
                }
            };
        }
    }
}
