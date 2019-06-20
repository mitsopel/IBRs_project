# Information Bound References (IBRs) Project #

Implementing a prototype IBR resolution service, based on [this](http://pages.cs.wisc.edu/~akella/papers/ibr-conext13.pdf) publication, 
for generating, storing and querying IBRs for similar images around the Web, to ensure high QoE to end-users dependent on their devices OS and network conditions.


## Services ##


+   IBR Service

Resolves IBRs to image locations. Various metadata are associated with each location(size, encoding e.t.c) 
LSH hashing based on [this](https://people.csail.mit.edu/indyk/p117-andoni.pdf)  {Page 3}


+   Crawler Service

Scan the web using Selenium library


+   Perceptual Hash Service

Custom perceptual hash as described in the IBR publication


+   Viewer Service

Simple client showcasing the ability for applications to implement their own logic on image selection through IBR resolution



# Developers #

Mitsopoulou Eleni, MSc AUEB Computer Science

Vitsas Nikolaos, MSc AUEB Computer Science

Koutsoulis Vasileios, MSc AUEB Computer Science
