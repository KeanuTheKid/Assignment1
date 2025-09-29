Assignment 1
------------

# Team Members
Flurina Sophie Baumbach, Keanu Belo da Silva
# GitHub link to your (forked) repository
https://github.com/KeanuTheKid/Assignment1.git
...

# Task 4

1. (0.25pt) What are Interface Definition Languages (IDL) used for? Name and explain the IDL that you use for this task.
   Ans: IDL describe what services/methods there are and how messages have  to look like. That way the client- and serverstubs and interfaces are generated in different languages (so both sides speak the same protocol)
        proto3 with Service AssignJob and Messages. it generates java classes, gRPC-mascs 
2. (0.25pt) In this implementation of gRPC, you use channels. What are they used for?
   Ans: a channel is basically the connection from client to server (IP+port), TLS/HTTP, pooling, Name-Res and if needed load balancing. You can build the stubs over the channel and send calls (no channel no calls)
3. (0.5pt)
   (0.25) Describe how the MapReduce algorithm works. Do you agree that the MapReduce programming model may have latency issues? What could be the cause of this?
   (0.25) Can this programming model be suitable (recommended) for iterative machine learning or data analysis applications? Please explain your argument.
   Ans: Map: Input in Chunks, Mapper parse text -> pairs (key, value). example: ("word",1)
        Shuffle/Sort: Groups values by key
        Reduce: Summarize every value for every key
        Latency issues: YES. big shuffle over network, Sorting, slow workers may slow down the others
        --
        ML/Data-Analysis: Iterative Algorithms that go over the same data multiple times suffer, because MapReduce typically write on disk after every step and reads it again in the next step. much overhead. I'd recommend Systems with memory-persistence oder caching.   