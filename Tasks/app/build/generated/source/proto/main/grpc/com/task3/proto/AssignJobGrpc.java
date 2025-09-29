package com.task3.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.57.2)",
    comments = "Source: communicate.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AssignJobGrpc {

  private AssignJobGrpc() {}

  public static final java.lang.String SERVICE_NAME = "filesystem.AssignJob";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.task3.proto.MapInput,
      com.task3.proto.MapOutput> getMapMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "map",
      requestType = com.task3.proto.MapInput.class,
      responseType = com.task3.proto.MapOutput.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.task3.proto.MapInput,
      com.task3.proto.MapOutput> getMapMethod() {
    io.grpc.MethodDescriptor<com.task3.proto.MapInput, com.task3.proto.MapOutput> getMapMethod;
    if ((getMapMethod = AssignJobGrpc.getMapMethod) == null) {
      synchronized (AssignJobGrpc.class) {
        if ((getMapMethod = AssignJobGrpc.getMapMethod) == null) {
          AssignJobGrpc.getMapMethod = getMapMethod =
              io.grpc.MethodDescriptor.<com.task3.proto.MapInput, com.task3.proto.MapOutput>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "map"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.task3.proto.MapInput.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.task3.proto.MapOutput.getDefaultInstance()))
              .setSchemaDescriptor(new AssignJobMethodDescriptorSupplier("map"))
              .build();
        }
      }
    }
    return getMapMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.task3.proto.ReduceInput,
      com.task3.proto.ReduceOutput> getReduceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "reduce",
      requestType = com.task3.proto.ReduceInput.class,
      responseType = com.task3.proto.ReduceOutput.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.task3.proto.ReduceInput,
      com.task3.proto.ReduceOutput> getReduceMethod() {
    io.grpc.MethodDescriptor<com.task3.proto.ReduceInput, com.task3.proto.ReduceOutput> getReduceMethod;
    if ((getReduceMethod = AssignJobGrpc.getReduceMethod) == null) {
      synchronized (AssignJobGrpc.class) {
        if ((getReduceMethod = AssignJobGrpc.getReduceMethod) == null) {
          AssignJobGrpc.getReduceMethod = getReduceMethod =
              io.grpc.MethodDescriptor.<com.task3.proto.ReduceInput, com.task3.proto.ReduceOutput>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "reduce"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.task3.proto.ReduceInput.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.task3.proto.ReduceOutput.getDefaultInstance()))
              .setSchemaDescriptor(new AssignJobMethodDescriptorSupplier("reduce"))
              .build();
        }
      }
    }
    return getReduceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AssignJobStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AssignJobStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AssignJobStub>() {
        @java.lang.Override
        public AssignJobStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AssignJobStub(channel, callOptions);
        }
      };
    return AssignJobStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AssignJobBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AssignJobBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AssignJobBlockingStub>() {
        @java.lang.Override
        public AssignJobBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AssignJobBlockingStub(channel, callOptions);
        }
      };
    return AssignJobBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AssignJobFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AssignJobFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AssignJobFutureStub>() {
        @java.lang.Override
        public AssignJobFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AssignJobFutureStub(channel, callOptions);
        }
      };
    return AssignJobFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<com.task3.proto.MapInput> map(
        io.grpc.stub.StreamObserver<com.task3.proto.MapOutput> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getMapMethod(), responseObserver);
    }

    /**
     */
    default void reduce(com.task3.proto.ReduceInput request,
        io.grpc.stub.StreamObserver<com.task3.proto.ReduceOutput> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReduceMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AssignJob.
   */
  public static abstract class AssignJobImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AssignJobGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AssignJob.
   */
  public static final class AssignJobStub
      extends io.grpc.stub.AbstractAsyncStub<AssignJobStub> {
    private AssignJobStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AssignJobStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AssignJobStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.task3.proto.MapInput> map(
        io.grpc.stub.StreamObserver<com.task3.proto.MapOutput> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getMapMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void reduce(com.task3.proto.ReduceInput request,
        io.grpc.stub.StreamObserver<com.task3.proto.ReduceOutput> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReduceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AssignJob.
   */
  public static final class AssignJobBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AssignJobBlockingStub> {
    private AssignJobBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AssignJobBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AssignJobBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.task3.proto.ReduceOutput reduce(com.task3.proto.ReduceInput request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReduceMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AssignJob.
   */
  public static final class AssignJobFutureStub
      extends io.grpc.stub.AbstractFutureStub<AssignJobFutureStub> {
    private AssignJobFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AssignJobFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AssignJobFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.task3.proto.ReduceOutput> reduce(
        com.task3.proto.ReduceInput request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReduceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REDUCE = 0;
  private static final int METHODID_MAP = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REDUCE:
          serviceImpl.reduce((com.task3.proto.ReduceInput) request,
              (io.grpc.stub.StreamObserver<com.task3.proto.ReduceOutput>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_MAP:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.map(
              (io.grpc.stub.StreamObserver<com.task3.proto.MapOutput>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getMapMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              com.task3.proto.MapInput,
              com.task3.proto.MapOutput>(
                service, METHODID_MAP)))
        .addMethod(
          getReduceMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.task3.proto.ReduceInput,
              com.task3.proto.ReduceOutput>(
                service, METHODID_REDUCE)))
        .build();
  }

  private static abstract class AssignJobBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AssignJobBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.task3.proto.Communicate.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AssignJob");
    }
  }

  private static final class AssignJobFileDescriptorSupplier
      extends AssignJobBaseDescriptorSupplier {
    AssignJobFileDescriptorSupplier() {}
  }

  private static final class AssignJobMethodDescriptorSupplier
      extends AssignJobBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AssignJobMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AssignJobGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AssignJobFileDescriptorSupplier())
              .addMethod(getMapMethod())
              .addMethod(getReduceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
