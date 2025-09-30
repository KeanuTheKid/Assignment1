Assignment 1
------------

# Team Members
Flurina Sophie Baumbach, Keanu Belo da Silva
# GitHub link to your (forked) repository
https://github.com/KeanuTheKid/Assignment1.git
...

# Task 4

1.⁠ ⁠(0.25pt) What are Interface Definition Languages (IDL) used for? Name and explain the IDL that you use for this task.
   
Ans: IDLs describe service interfaces and message formats in a language-neutral way. They allow automatic generation of client and server code across different programming languages, ensuring consistent communication. In this assignment we use Protocol Buffers (proto3) as the IDL. The `.proto` file defines the service methods and the message types, from which Java code is generated for gRPC.

2.⁠ ⁠(0.25pt) In this implementation of gRPC, you use channels. What are they used for?
   
Ans: A channel in gRPC represents a virtual connection to a specific server. All RPC calls are made through a channel. The client uses it through a stub to send requests and get responses. In this project, the client establishes one channel to the Map server and one to the Reduce server.

3.⁠ ⁠(0.5pt)
   (0.25) Describe how the MapReduce algorithm works. Do you agree that the MapReduce programming model may have latency issues? What could be the cause of this?

Ans:
- Map phase: The input data is split into chunks and each mapper produces intermediate key–value pairs
- Shuffle phase: The framework groups all values with the same key and transfers them to the corresponding reducers
- Reduce phase: Each reducer processes the grouped values to produce the final output

Yes, the model can suffer from latency. Causes include: slow “straggler” tasks that delay completion, network overhead during the shuffle phase, and repeated disk I/O for intermediate results.

   (0.25) Can this programming model be suitable (recommended) for iterative machine learning or data analysis applications? Please explain your argument.
   
Ans: MapReduce is generally ot good for iterative tasks like machine learning or graph algorithms. Each iteration requires launching a new job and reading/writing data to disk again, which introduces significant overhead. Frameworks like Apache Spark are more efficient because they keep intermediate data in memory and allow faster iterations.

4.⁠ ⁠(From PDF) Explain, in your own words, what RPC is. When and why is it useful?
   
Ans: RPC (Remote Procedure Call) allows a program to call a procedure on a remote machine as if it were local. The system transparently handles communication, serialization, and error handling. It is useful in distributed systems because it hides low-level networking details and makes it easier to build modular applications where services run on different nodes.

5.⁠ ⁠(From PDF) Data locality is often discussed as a way to improve MapReduce performance. Explain what data locality is and how it affects efficiency.

Ans: Data locality means scheduling computation close to where the data is stored. This reduces the amount of data that must be transferred over the network, lowers communication overhead and speeds up processing, which is especially important for large datasets.