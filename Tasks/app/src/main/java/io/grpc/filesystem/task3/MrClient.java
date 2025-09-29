package io.grpc.filesystem.task3;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import com.task3.proto.AssignJobGrpc;
import com.task3.proto.MapInput;
import com.task3.proto.MapOutput;
import com.task3.proto.ReduceInput;
import com.task3.proto.ReduceOutput;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.grpc.filesystem.task2.MapReduce;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MrClient {
    Map<String, Integer> jobStatus = new HashMap<String, Integer>();


    public static void main(String[] args) throws Exception {
        String ip = args[0];
        Integer mapPort = Integer.parseInt(args[1]);
        Integer reducePort = Integer.parseInt(args[2]);
        String inputFilePath = args[3];
        String outputFilePath = args[4];

        MrClient client = new MrClient();
        int response;

        String chunkPath = MapReduce.makeChunks(inputFilePath);
        File dir = new File(chunkPath);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File f : directoryListing) {
                if (f.isFile()) {
                    client.jobStatus.put(f.getPath(), 1);
                }
            }
        }
        client.requestMap(ip, mapPort, inputFilePath, outputFilePath);

        Set<Integer> values = new HashSet<Integer>(client.jobStatus.values());
        if (values.size() == 1 && client.jobStatus.containsValue(2)) {
            response = client.requestReduce(ip, reducePort, chunkPath, outputFilePath);
            if (response == 2) {
                System.out.println("Reduce task completed!");
            } else {
                System.out.println("Try again! " + response);
            }
        }
    }


        /*
         1. Open a gRPC channel to the Map server using the IP and port.
         2. Create a non-blocking stub for map requests.
         3. Implement a StreamObserver to handle responses. 
         - Check if map tasks are completed (status == 2).
         - Print success or failure messages.
         4. Loop through jobStatus, send map requests, and update statuses.
         5. Call stream.onCompleted() to finish and wait for the server's response (for example, using a CountDownLatch).
         6. Close the gRPC channel after completion.
        */
        public void requestMap(String ip, Integer portNumber, String inputFilePath, String outputFilePath) throws InterruptedException {
            ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(ip, portNumber)
                    .usePlaintext()
                    .build();

            try {
                AssignJobGrpc.AssignJobStub stub = AssignJobGrpc.newStub(channel);
                CountDownLatch latch = new CountDownLatch(1);

                StreamObserver<MapOutput> responseObserver = new StreamObserver<MapOutput>() {
                    @Override
                    public void onNext(MapOutput value) {
                        int status = value.getJobstatus();
                        if (status == 2) {
                            for (Map.Entry<String, Integer> e : jobStatus.entrySet()) {
                                if (e.getValue() != null && e.getValue() == 1) {
                                    e.setValue(2);
                                }
                            }
                            System.out.println("Map tasks completed (status=2)");
                        } else {
                            System.out.println("Map task status: " + status);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        System.err.println("Map stream error: " + t.getMessage());
                        latch.countDown();
                    }

                    @Override
                    public void onCompleted() {
                        latch.countDown();
                    }
                };

                StreamObserver<MapInput> requestObserver = stub.map(responseObserver);

                for (Map.Entry<String, Integer> e : jobStatus.entrySet()) {
                    if (e.getValue() != null && e.getValue() == 1) {
                        MapInput req = MapInput.newBuilder()
                                .setIp(ip)
                                .setPort(portNumber)
                                .setInputfilepath(e.getKey())
                                .setOutputfilepath("")
                                .build();
                        requestObserver.onNext(req);
                    }
                }

                requestObserver.onCompleted();
                latch.await(10, TimeUnit.SECONDS);

            } finally {
                channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
            }
        }



        /*
         1. Open a gRPC channel to the Reduce server.
         2. Create a blocking stub for reduce requests.
         3. Build and send the reduce request.
         4. Check the job status from the server's response.
         5. Close the gRPC channel after completion.
         6. Return the job status (e.g., 2 for success).
        */
        public int requestReduce(String ip, Integer portNumber, String inputFilePath, String outputFilePath) {
            ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(ip, portNumber)
                    .usePlaintext()
                    .build();

            try {
                AssignJobGrpc.AssignJobBlockingStub stub = AssignJobGrpc.newBlockingStub(channel);

                ReduceInput req = ReduceInput.newBuilder()
                        .setIp(ip)
                        .setPort(portNumber)
                        .setInputfilepath(inputFilePath)   // Ordner mit den map-Ergebnissen (Chunks)
                        .setOutputfilepath(outputFilePath) // Endgültige Ausgabe
                        .build();

                ReduceOutput res = stub.reduce(req);
                int status = res.getJobstatus();
                System.out.println("Reduce task status: " + status);
                return status;

            } finally {
                try {
                    channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


}
