# DiscretePSO-TSP - WIP
Using discrete PSO to solve travel salesman problem

Implement the discrete version of the PSO applied to the traveling salesman problem described in the article:
**PARTICLE SWARM OPTIMIZATION FOR TRAVELING SALESMAN PROBLEM** by Huang

### Details
* The velocity is represented as a Swap Sequence of Swap operator i.e SS={(1,3);(2,4);(2,5)}
* Alfa and Beta define the probability of the global best and local best influence
* This code ignores previous velocity because we get more speed convergence and optimal results
* The branch hybrid-pso modify the concept of velocity and the calc of them


The benchmarks are from TSPLib and are located in the resources' folder. Some benchmarks are cyclic and acyclic

* Requirements:
    * Maven 3
    * JDK 11
    
### Next Steps
* combine genetic operator with DPSO 
* Find and use a best random number generator
    
